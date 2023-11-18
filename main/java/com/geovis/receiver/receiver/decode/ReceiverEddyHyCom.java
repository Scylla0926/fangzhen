package com.geovis.receiver.receiver.decode;

import com.alibaba.fastjson.JSONObject;
import com.geovis.receiver.Config.LockSqlite;
import com.geovis.receiver.factory.ReceiverInit;
import com.geovis.receiver.pojo.model.Produce;
import com.geovis.receiver.pojo.vo.EddyCoraVo;
import com.geovis.receiver.pojo.vo.EddyHyComVo;
import com.geovis.receiver.readData.eddy.DecodeNcFile;
import com.geovis.receiver.receiver.ReceiverBase;
import com.geovis.receiver.service.impl.EddyCoraServiceImpl;
import com.geovis.receiver.service.impl.EddyHyComServiceImpl;
import com.geovis.receiver.tools.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Component
@Slf4j
@Scope("prototype")
public class ReceiverEddyHyCom extends ReceiverBase {
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
    private EddyHyComServiceImpl eddyHyComService;

    private int finishNum = 0;
    private static int totalNum;


    /**
     * 数据解析 生成 png json bin hdr
     *
     * @param pro
     * @return
     */
    @Override
    public boolean produce(Produce pro) {
        File srcFile = pro.getFile();
        System.out.println(pro.getFile().getAbsolutePath());
        String jsonPath = "";
        if (configElement.getEndWith().equals("nc")) {
            jsonPath = DecodeNcFile.decode(srcFile.getAbsolutePath());
        } else if (configElement.getEndWith().equals("json")) {
            jsonPath = srcFile.getAbsolutePath();
        }

        File file = new File(jsonPath);

        List<Object> dataList = new ArrayList<>();
        ArrayList<EddyHyComVo> eddySatVos = new ArrayList<EddyHyComVo>();
        FileUtil fileUtil = new FileUtil();
        //  AVISO_EDDY_SLA_20220401.json
        try {
            EddyHyComVo eddySatVo = new EddyHyComVo();
            eddySatVo.setId(System.currentTimeMillis());
            eddySatVo.setFileName(file.getName());
            eddySatVo.setFileSize(file.length() / 1024 + "");
            eddySatVo.setFileType("json");
            eddySatVo.setFileRelativePath(file.getAbsolutePath().substring(ReceiverInit.pds.length(), file.getAbsolutePath().length()));
            eddySatVo.setFileAbsolutePath(file.getAbsolutePath());
            eddySatVo.setEleName("eddy");
            eddySatVo.setRegionType("NSI");
            eddySatVo.setMethod(srcFile.getName().split("_")[2]);
            eddySatVo.setInsertTime(new Date());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            eddySatVo.setDataTime(sdf.parse(fileUtil.getFileDate(srcFile.getName(), configElement.getRegexList().get(0))));
            eddySatVos.add(eddySatVo);
        } catch (Exception e) {
            System.out.println("处理文件类型为：" + configElement.getEleType() + " 文件绝对路径为 ：" + srcFile.getAbsolutePath());
            e.printStackTrace();
        }
        dataList.add(eddySatVos);
        pro.setObjectList(dataList);
        return true;
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
                destFile = new File(configElement.getDataStorageDir() + File.separator + dateString + File.separator + newFn);
                if ("true".equalsIgnoreCase(configElement.getIsCache())) {
                    if (FileCopyUtils.fileCopy(srcFile, destFile, msg)) {
                        pro.addInfo(msg);
                        System.out.println(destFile.getAbsolutePath());
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
        File srcFile = pro.getFile();
        List<EddyHyComVo> list = (List<EddyHyComVo>) pro.getObjectList().get(0);
        try {
            for (EddyHyComVo eddySatVo : list) {
                eddyHyComService.saveOrUpdateObj(eddySatVo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据入库失败 ！");
            return false;
        }
        return true;
    }
}
