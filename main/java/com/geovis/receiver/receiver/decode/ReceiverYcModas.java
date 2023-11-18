package com.geovis.receiver.receiver.decode;


import com.alibaba.fastjson.JSONObject;
import com.geovis.receiver.Config.LockSqlite;
import com.geovis.receiver.dao.YcCoraDao;
import com.geovis.receiver.factory.ReceiverInit;
import com.geovis.receiver.pojo.bean.ProductTableBean;
import com.geovis.receiver.pojo.model.Produce;
import com.geovis.receiver.pojo.vo.YcCoraVo;
import com.geovis.receiver.pojo.vo.YcModasVo;
import com.geovis.receiver.readData.thermocline.yc.DecodeYuecengNcFile;
import com.geovis.receiver.receiver.ReceiverBase;
import com.geovis.receiver.service.DataSetService;
import com.geovis.receiver.service.impl.YcForecastImpl;
import com.geovis.receiver.service.impl.YcModasServiceImpl;
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

/**
 * 实况类跃层数据的引接解析。包括：Cora、modas
 */
@Data
@Component
@Slf4j
@Scope("prototype")
public class ReceiverYcModas extends ReceiverBase {
    private String msg = "";
    private String eleType = "";
    @Autowired
    private CleanNameTools clean;

    @Autowired
    private ReadConfig rc;

    @Autowired
    private RedisPublish redisPublish;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    DecodeYuecengNcFile decodeYcNcFile;


    @Resource
    YcModasServiceImpl ycModasService;

    @Autowired
    private DataSetService dataSetService;

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
        String outFilePath = srcFile.getParent() + File.separator + srcFile.getName().split("\\.")[0];
        outFilePath = outFilePath.substring(0, outFilePath.lastIndexOf(File.separator) + 1);
        // 如果不是 海浪
        List<Object> dataList = new ArrayList<>();
        List<YcCoraVo> decode = null;
        try {
            eleType = configElement.getEleType();
            decode = decodeYcNcFile.decode(srcFile.getAbsolutePath(), outFilePath, configElement.getRegexList().get(0));

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
            List<YcCoraVo> voList = (List<YcCoraVo>) objectList.get(0);
            ProductTableBean tableBean = dataSetService.getTableBean(eleType, "2");
            if (voList != null && tableBean != null) {
                /////----1.赋值存库
                for (YcCoraVo ycCoraVo : voList) {
                    File PngFile = new File(ycCoraVo.getPngFileAbsolutePath());
                    YcModasVo ycModasVo = new YcModasVo();
                    ycModasVo.setId(System.currentTimeMillis());
                    ycModasVo.setLevel(ycCoraVo.getLevel());
                    ycModasVo.setPngFileAbsolutePath(ycCoraVo.getPngFileAbsolutePath());
                    ycModasVo.setSrcFileAbsolutePath(ycCoraVo.getSrcFileAbsolutePath());
                    ycModasVo.setSrcFileType(ycCoraVo.getSrcFileType());
                    ycModasVo.setEleName(ycCoraVo.getEleName());
                    ycModasVo.setDgMethod(ycCoraVo.getDgMethod());
                    ycModasVo.setInsertTime(new Date());
                    ycModasVo.setNumberX(ycCoraVo.getNumberY());
                    ycModasVo.setNumberY(ycCoraVo.getNumberY());
                    ycModasVo.setRegionType(ycCoraVo.getRegionType());
                    ycModasVo.setPngFileName(PngFile.getName());
                    ycModasVo.setPngFileSize(PngFile.length());
                    ycModasVo.setPngFileRelativePath(PngFile.getAbsolutePath().substring(ReceiverInit.pds.length(), PngFile.getAbsolutePath().length()));
                    File srcFile = new File(ycCoraVo.getSrcFileAbsolutePath());
                    ycModasVo.setSrcFileName(srcFile.getName());
                    ycModasVo.setSrcFileSize(srcFile.length());
                    ycModasVo.setSrcFileName(srcFile.getName());
                    String prefixFileName = new File(srcFile.getName()).getName();
                    // 根据 正则 从文件名上取出 文件诺没有文件名上 没有 文件时间则取 文件最后修改时间
                    String fileDate = new FileUtil().getFileDate(prefixFileName, configElement.getRegexList().get(0));
                    Date date = new SimpleDateFormat("yyyyMMdd").parse(fileDate.substring(0, 8));
                    ycModasVo.setDataTime(date);
                    ycModasService.saveOrUpdateObj(ycModasVo);
                }
                /////--2.更新redis
                if (voList.size() > 0) {
                    dataSetService.updateRedisDataSource(tableBean.getProductType());
                }
                return true;
            } else {///没解出东西，按入库失败处理
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据入库失败 ！");
            return false;
        }
    }
}
