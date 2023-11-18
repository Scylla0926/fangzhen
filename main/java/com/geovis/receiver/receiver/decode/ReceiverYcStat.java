package com.geovis.receiver.receiver.decode;


import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.geovis.receiver.Config.LockSqlite;
import com.geovis.receiver.dao.YcStatDao;
import com.geovis.receiver.factory.ReceiverInit;
import com.geovis.receiver.pojo.bean.ProductTableBean;
import com.geovis.receiver.pojo.model.Produce;
import com.geovis.receiver.pojo.vo.YcStatVo;
import com.geovis.receiver.readData.grib.DecodeGribSimpleData;
import com.geovis.receiver.readData.thermocline.ycStat.DecodeYuecengStatFile;
import com.geovis.receiver.receiver.ReceiverBase;
import com.geovis.receiver.service.DataSetService;
import com.geovis.receiver.service.impl.YcStatServiceImpl;
import com.geovis.receiver.tools.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析入库统计类产品
 */
@Data
@Component
@Slf4j
@Scope("prototype")
public class ReceiverYcStat extends ReceiverBase {
    private String msg = "";

    @Autowired
    private CleanNameTools clean;

    @Autowired
    private ReadConfig rc;

    @Autowired
    private RedisPublish redisPublish;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    DecodeYuecengStatFile decodeYuecengStatFile;

    @Autowired
    DecodeGribSimpleData decodeGrib;

    @Autowired
    YcStatServiceImpl ycStatDao;

    @Autowired
    private DataSetService dataSetService;

    private int finishNum = 0;
    private static int totalNum;
    private String eleType;

    /**
     * 数据解析 生成 png json bin hdr
     *
     * @param pro
     * @return
     */
    @Override
    public boolean produce(Produce pro) {

        File srcFile = pro.getFile();
        String outFilePath = srcFile.getParent() + File.separator + srcFile.getName().split("\\.")[0];
        outFilePath = outFilePath.substring(0, outFilePath.lastIndexOf(File.separator) + 1);
        // 如果不是 海浪
        List<Object> dataList = new ArrayList<>();
        List<YcStatVo> decode = null;
        try {
            eleType = configElement.getEleType();
            decode = decodeYuecengStatFile.decode(srcFile.getAbsolutePath(), outFilePath, configElement.getRegexList().get(0));
//            decode = decodeGrib.decode(srcFile.getAbsolutePath(), outFilePath, configElement.getRegexList().get(0));
        } catch (Exception e) {
            System.out.println("处理文件类型为：" + configElement.getEleType() + " 文件绝对路径为 ：" + srcFile.getAbsolutePath());
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
            List<YcStatVo> statBeans = (List<YcStatVo>) objectList.get(0);
            List<String> regxList = configElement.getRegexList();
            String year = "";
            String month = "";
            String region = "";
            String statType = "";
            /////-----1.从正则表达式中取出年月区域等信息
            if (regxList.size() > 0) {
                String regx = regxList.get(0);
                Pattern regxFile = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
                if (statBeans.size() > 0) {
                    Matcher match = regxFile.matcher(statBeans.get(0).getSrcFileName());
                    if (match.find()) {
                        if (regx.contains("<YEAR>")) {
                            year = match.group("YEAR");
                        }
                        if (regx.contains("<MONTH>")) {
                            month = match.group("MONTH");
                        }
                        if (regx.contains("<Region>")) {
                            region = match.group("Region");
                        }
                        if (regx.contains("<STATTYPE>")) {
                            statType = match.group("STATTYPE");
                        }
                    }
                }
            }
            /////----2.赋值存库
            for (YcStatVo bean : statBeans) {
                YcStatVo vo = new YcStatVo();
                File PngFile = new File(bean.getPngFileAbsolutePath());
                vo.setPngFileName(PngFile.getName());
                vo.setPngFileSize(PngFile.length());
                vo.setEleName(bean.getEleName());
                vo.setSrcFileAbsolutePath(bean.getSrcFileAbsolutePath());
                vo.setPngFileAbsolutePath(bean.getPngFileAbsolutePath());
                vo.setPngFileRelativePath(PngFile.getAbsolutePath().substring(ReceiverInit.pds.length(), PngFile.getAbsolutePath().length()));
                vo.setSrcFileType(FileUtil.getSuffix(bean.getSrcFileAbsolutePath()));
                vo.setYear(year);
                vo.setMonth(month);
                vo.setDgMethod(bean.getDgMethod());
                vo.setRegionType(region);
                vo.setStatType(statType);
                File srcFile = new File(bean.getSrcFileAbsolutePath());
                vo.setSrcFileName(srcFile.getName());
                vo.setSrcFileSize(srcFile.length());
                vo.setSrcFileName(srcFile.getName());
//                vo.setTableName(bean.getTableName());
                vo.setInsertTime(new Date());

                ycStatDao.saveOrUpdateObj(vo);
            }
            /////--3.更新redis
            ProductTableBean tableBean = dataSetService.getTableBean(eleType, "3");
            if (statBeans.size() > 0 && tableBean != null) {
                dataSetService.updateRedisDataSource(tableBean.getProductType());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据入库失败 ！");
            return false;
        }
        return true;
    }
}
