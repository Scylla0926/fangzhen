package com.geovis.receiver.receiver.decode;

import com.alibaba.fastjson.JSONObject;
import com.geovis.receiver.Config.LockSqlite;
import com.geovis.receiver.factory.ReceiverInit;
import com.geovis.receiver.pojo.model.Produce;
import com.geovis.receiver.pojo.vo.SshFuseVo;
import com.geovis.receiver.pojo.vo.SssFuseVo;
import com.geovis.receiver.receiver.ReceiverBase;
import com.geovis.receiver.service.impl.SssFuseServiceImpl;
import com.geovis.receiver.tools.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
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
 * 海面高度融合数据引接解析类
 */
@Data
@Component
@Slf4j
@Scope("prototype")
public class ReceiverSatFuseSss extends ReceiverBase {
    private String msg = "";
    private String eleType="";
    @Autowired
    private CleanNameTools clean;

    @Autowired
    private ReadConfig rc;

    @Autowired
    private RedisPublish redisPublish;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    SssFuseServiceImpl sssSatFuseDao;

    private int finishNum = 0;
    private static int totalNum;

    /**
     * 数据解析
     *
     * @param pro
     * @return
     */
    @Override
    public boolean produce(Produce pro) {
        FileUtil fileUtil = new FileUtil();
        File pngFile = pro.getFile();
        String outFilePath = pngFile.getParent() + File.separator + pngFile.getName().split("\\.")[0];
        List<Object> dataList = new ArrayList<>();
        List<SssFuseVo> decode = new ArrayList<>();
        try {
            eleType = configElement.getEleType();
            SssFuseVo sssFuseVo=new SssFuseVo();
            sssFuseVo.setId(System.currentTimeMillis());
            // 取出算法类型
            sssFuseVo.setMethod(fileUtil.getFileMethod(pngFile.getName(), configElement.getRegexList().get(0)));
            ///日期
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");
            try {
                String dataTimeStr=fileUtil.getFileDate(pngFile.getName(), configElement.getRegexList().get(0));
                sssFuseVo.setDateTime(sdf.parse(dataTimeStr));
                sssFuseVo.setYear(dataTimeStr.substring(0,4));
                sssFuseVo.setMonth(dataTimeStr.substring(4,6));
                sssFuseVo.setDay(dataTimeStr.substring(6,8));
                sssFuseVo.setHour(dataTimeStr.substring(8,10));
                sssFuseVo.setMinute(dataTimeStr.substring(10,12));
                sssFuseVo.setSecond(dataTimeStr.substring(12,14));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            //区域
            sssFuseVo.setRegionType("GLB");
            //源文件信息
            File srcFile=new File(pngFile.getAbsolutePath().replace("png","HDF"));
            sssFuseVo.setSrcFileAbsolutePath(srcFile.getAbsolutePath());
            sssFuseVo.setSrcFileName(srcFile.getName());
            sssFuseVo.setSrcFileSize(srcFile.length());
            sssFuseVo.setSrcFileType("hdf");
            ///png文件信息
            sssFuseVo.setPngFileRelativePath(pngFile.getAbsolutePath().substring(ReceiverInit.pds.length(), pngFile.getAbsolutePath().length()));
            sssFuseVo.setPngFileAbsolutePath(pngFile.getAbsolutePath());
            sssFuseVo.setPngFileSize(pngFile.length());
            sssFuseVo.setPngFileName(pngFile.getName());
            //要素
            sssFuseVo.setEleName("sss");
            sssFuseVo.setInsertTime(new Date());
            decode.add(sssFuseVo);
        } catch (Exception e) {
            System.out.println("处理文件类型为：" + configElement.getEleType() + " 文件绝对路径为 ：" + pngFile.getAbsolutePath());
            e.printStackTrace();
        }
        dataList.add(decode);
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
            List<SssFuseVo> beans = (List<SssFuseVo>) objectList.get(0);
            for (SssFuseVo bean : beans) {
                sssSatFuseDao.saveOrUpdateObj(bean);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据入库失败 ！");
            return false;
        }
    }
}
