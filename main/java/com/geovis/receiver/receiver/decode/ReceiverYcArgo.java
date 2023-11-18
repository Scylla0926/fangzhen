package com.geovis.receiver.receiver.decode;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geovis.receiver.Config.LockSqlite;
import com.geovis.receiver.dao.YcArgoDao;
import com.geovis.receiver.pojo.bean.NwpPngCurrencyBean;
import com.geovis.receiver.pojo.model.Produce;
import com.geovis.receiver.pojo.vo.YcArgoVo;
import com.geovis.receiver.pojo.vo.YcStatVo;
import com.geovis.receiver.receiver.ReceiverBase;
import com.geovis.receiver.tools.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析 跃层诊断  argo数据源 产品
 */
@Data
@Component
@Slf4j
@Scope("prototype")
public class ReceiverYcArgo extends ReceiverBase {

    private String msg = "";

    @Autowired
    private CleanNameTools clean;

    @Autowired
    private ReadConfig rc;

    @Autowired
    private RedisPublish redisPublish;

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private YcArgoDao ycArgoDao;

    private int finishNum = 0;
    private static int totalNum;

    @Override
    public boolean produce(Produce pro) {

        File srcFile = pro.getFile();
        String absolutePath = srcFile.getAbsolutePath();
        FileUtil fileUtil = new FileUtil();
        ArrayList<Object> objects = new ArrayList<>();

        ArrayList<YcArgoVo> ycArgoVos = new ArrayList<>();

        YcArgoVo ycArgoVo = new YcArgoVo();

        ycArgoVo.setId(System.currentTimeMillis());
        // 取出算法类型
        ycArgoVo.setMethodType(fileUtil.getFileMethod(srcFile.getName(), configElement.getRegexList().get(0)));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            ycArgoVo.setDataTime(sdf.parse(fileUtil.getFileDate(srcFile.getName(), configElement.getRegexList().get(0))));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        String jsonString = null;
        try {
            jsonString = new String(Files.readAllBytes(Paths.get(absolutePath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        ycArgoVo.setLat(jsonObject.getString("lat"));
        ycArgoVo.setLon(jsonObject.getString("lon"));

        JSONArray jsonArray = jsonObject.getJSONArray("depth");
        ycArgoVo.setDepth(JsonArrayToString(jsonArray));

        jsonArray = jsonObject.getJSONArray("salt");
        ycArgoVo.setSalt(JsonArrayToString(jsonArray));

        jsonArray = jsonObject.getJSONArray("temp");
        ycArgoVo.setTemp(JsonArrayToString(jsonArray));

        jsonArray = jsonObject.getJSONArray("sv");
        ycArgoVo.setSv(JsonArrayToString(jsonArray));

        jsonArray = jsonObject.getJSONArray("rho");
        ycArgoVo.setRho(JsonArrayToString(jsonArray));

        ycArgoVo.setStyleS(jsonObject.getString("styleS"));
        ycArgoVo.setStyleT(jsonObject.getString("styleT"));
        ycArgoVo.setStyleV(jsonObject.getString("styleV"));
        ycArgoVo.setStyleR(jsonObject.getString("styleR"));

        JSONObject jsonObjectYC = jsonObject.getJSONObject("S");
        String s = jsonObjectToJsonArray(jsonObjectYC).toString();
        ycArgoVo.setS(s);


        jsonObjectYC = jsonObject.getJSONObject("R");
        String r = jsonObjectToJsonArray(jsonObjectYC).toString();
        ycArgoVo.setR(r);

        jsonObjectYC = jsonObject.getJSONObject("T");
        String t = jsonObjectToJsonArray(jsonObjectYC).toString();
        ycArgoVo.setT(t);

        jsonObjectYC = jsonObject.getJSONObject("V");
        String v = jsonObjectToJsonArray(jsonObjectYC).toString();
        ycArgoVo.setV(v);

        ycArgoVo.setInsertTime(new Date());
        ycArgoVo.setJsonPath(absolutePath);
        ycArgoVo.setPlatformNumber(Double.valueOf(jsonObject.getString("PLATFORM_NUMBER")).toString());
        ycArgoVo.setCycleNumber(jsonObject.getString("CYCLE_NUMBER"));

        ycArgoVos.add(ycArgoVo);
        objects.add(ycArgoVos);
        pro.setObjectList(objects);
        return true;
    }

    public static String JsonArrayToString(JSONArray jsonArray) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < jsonArray.size(); i++) {
            sb.append(jsonArray.getDouble(i) + ",");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }

    public static JSONObject jsonObjectToJsonArray(JSONObject jsonObject) {
        Object updepth = jsonObject.get("updepth");
        if (updepth != null) {
            if (!(updepth instanceof JSONArray)) {
                JSONArray array = new JSONArray();
                array.add(updepth);
                jsonObject.put("updepth", array);
            }
        }

        Object intensity = jsonObject.get("intensity");
        if (intensity != null) {
            if (!(intensity instanceof JSONArray)) {
                JSONArray array = new JSONArray();
                array.add(intensity);
                jsonObject.put("intensity", array);
            }
        }
        Object thickness = jsonObject.get("thickness");
        if (thickness != null) {
            if (!(thickness instanceof JSONArray)) {
                JSONArray array = new JSONArray();
                array.add(thickness);
                jsonObject.put("thickness", array);
            }
        }
        Object dndepth = jsonObject.get("dndepth");
        if (dndepth != null) {
            if (!(dndepth instanceof JSONArray)) {
                JSONArray array = new JSONArray();
                array.add(dndepth);
                jsonObject.put("dndepth", array);
            }
        }
        return jsonObject;
    }

    @Override
    public boolean patt(File f) {
        return super.PattCommon(f, this);
    }

    /**
     * 将文件 从专线拷贝到 缓存目录
     *
     * @param pro
     * @return
     */
    @Override
    public boolean cacheFile(Produce pro) {
        String eleName = configElement.getEleName();
        //准备处理某个文件 向redis 发送数据
        // 准备 json
        // 准备 json
        //将数据存储到redis中
        totalNum = redisUtil.totalNum(configElement);
        //redisUtil.addRedis(finishNum, totalNum, configElement);
        JSONObject eleJson = JSONObject.parseObject(JSONObject.toJSONString(configElement));
        eleJson.put("queueSize", this.recQueue.size());
        eleJson.put("finishNum", this.finishNum);
        eleJson.put("totalNum", totalNum);
        System.out.println(JSONObject.toJSONString(eleJson));
        //redisPublish.sendMessage(eleName, JSONObject.toJSONString(eleJson));
        if (configElement.getDataResourceDir().equals(configElement.getDataStorageDir())) {
            return true;
        }
        File srcFile = pro.getFile();
        if (!srcFile.renameTo(srcFile)) {
            System.out.println("文件尚未完全到齐，稍后重试!");
            return false;
        }
        File cutFile = srcFile;
        File destFile = new File(configElement.getDataCacheDir() + cutFile.getName());
        if (FileCopyUtils.fileCopy(srcFile, destFile, msg)) {
            pro.addInfo(msg);
            pro.setFile(destFile);
            pro.setDataCachePath(destFile.getAbsolutePath());
            return true;
        } else {
            pro.addError(msg);
            return false;
        }
    }

    @Override
    public boolean checkFile(Produce pro) {
        return true;
    }

    @Override
    public boolean archiveFile(Produce pro) {
        if (configElement.getDataResourceDir().equals(configElement.getDataStorageDir())) {
            return true;
        }
        File srcFile = new File(pro.getDataCachePath());
        File destFile = null;
        List<String> regxList = configElement.getRegexList();
        for (String regx : regxList) {
            Pattern regxFile = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
            Matcher match = regxFile.matcher(srcFile.getName());
            if (match.find()) {
                pro.setChooseRegex(regx);
                String dateString = fileBackupInfoUtils.getFileTimeInfo(srcFile, regx, match, "day");
                String newFn = clean.cleanSatName(srcFile.getName(), regx, configElement.getEleName());
                destFile = new File(configElement.getDataStorageDir() + dateString + File.separator + newFn);
                if ("true".equalsIgnoreCase(configElement.getIsCache())) {
                    if (FileCopyUtils.fileCopy(srcFile, destFile, msg)) {
                        pro.addInfo(msg);
                        pro.setFile(destFile);
                        return true;
                    } else {
                        pro.addError(msg);
                        return false;
                    }
                } else {
                    File destParentFile = destFile.getParentFile();
                    if (!destParentFile.exists()) {
                        destParentFile.mkdirs();
                    }

                    if (destFile.exists()) {
                        destFile.delete();
                    }

                    if (srcFile.renameTo(destFile)) {
                        pro.addInfo(msg);
                        pro.setFile(destFile);
                        return true;
                    } else {
                        pro.addError(msg);
                        return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 数据 痕迹记录 记录是否处理过 该文件
     *
     * @param pro
     * @return
     */
    @Override
    public boolean recordBackup(Produce pro) {
        Map<String, Lock> lockSqlite = LockSqlite.fileNameLock;
        String fileName = pro.getFile().getName();
        Lock fileNameLock = null;
        if (lockSqlite.containsKey(fileName)) {
            fileNameLock = lockSqlite.get(fileName);
        } else {
            fileNameLock = new ReentrantLock();
            lockSqlite.put(fileName, fileNameLock);
        }
        boolean flag;
        fileNameLock.lock();
        try {
            flag = fileBackupInfoUtils.fileCompletedWriter(super.configElement.getBackupFile(), new File(pro.getSrcFilePath().trim()), super.configElement.getRegexList(), super.configElement.getEleName());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            fileNameLock.unlock();
        }
        return flag;
    }

    @Override
    public boolean dataStore(Produce pro) {
        try {
            List<Object> objectList = pro.getObjectList();
            List<YcArgoVo> ycStatVos = (List<YcArgoVo>) objectList.get(0);
            for (YcArgoVo ycStatVo : ycStatVos) {
                ycArgoDao.insert(ycStatVo);
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据入库失败 ！");
            return false;
        }
        return true;
    }
}
