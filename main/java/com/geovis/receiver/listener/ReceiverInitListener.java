package com.geovis.receiver.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geovis.receiver.dao.CnfManagerDao;
import com.geovis.receiver.dao.PrdManagerDao;
import com.geovis.receiver.dao.SendTaskManagerDao;
import com.geovis.receiver.factory.ReceiverFactory;
import com.geovis.receiver.factory.ReceiverInit;
import com.geovis.receiver.pojo.model.ConfigElement;
import com.geovis.receiver.pojo.model.TableConfigElement;
import com.geovis.receiver.tools.ReadConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/***
 * 类描述: 程序的入口
 * @author yxl
 * @Date: 10:01 2020/9/19
 */
@Slf4j
@Component
public class ReceiverInitListener implements ServletContextListener {

    @Autowired
    private CnfManagerDao cnfManagerDao;

    @Autowired
    private SendTaskManagerDao sendTaskManagerDao;

    @Autowired
    private ReceiverFactory receiverFactory;

    @Autowired
    private ReadConfig readConfig;

    @Autowired
    private PrdManagerDao prdManagerDao;

//    private Timer t = new Timer();

    // 本机ip
    @Value("${custom.hostIp}")
    public String ip;

    @Value("${server.port}")
    public String port;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("资料引接初始化开始!");
        receiverInit();
        log.info("资料引接初始化完成!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("销毁监听器!");
    }

    /***
     *方法描述:
     * 初始化整个程序
     * @param: []
     * @author: yangxl
     * @Date: 2022/7/27 23:54
     * @Return void
     */
    private void receiverInit() {
        InitReceiverStatic();
        // 去读取configXml 配置文件信息
        List<ConfigElement> configElements = initConfigElements();

        //根据config的size 解报数据的数据类型数量 不同的线程去进行扫描和处理
        if (configElements == null || configElements.size() <= 0) {
            log.error("在配置文件中暂无可用配置!");
            return;
        }
        for (ConfigElement configElement : configElements) {
            receiverFactory.CSHReceiver(configElement);
        }

    }

    /**
     * 初始化 ReceiverInit 类中的静态变量
     */
    private void InitReceiverStatic() {
        //首先初始化 ReceiverInit 中的静态变量
        ReceiverInit.ns = cnfManagerDao.queryCnf();
        ReceiverInit.ip = ip;
        ReceiverInit.port = port;
        ReceiverInit.pds = readConfig.readTable("PDS", ReceiverInit.ip);
        ReceiverInit.wsp = readConfig.readTable("WORKSPACE", ReceiverInit.ip);
        //如果存在CONFIG_XML_PATH的路径，说明走的是配置文件的方式
        ReceiverInit.prd = prdManagerDao.queryPrd();
        List<JSONObject> dataList = sendTaskManagerDao.queryTask();
        List<JSONObject> realTimeTask = new ArrayList<JSONObject>();
        List<JSONObject> timerTask = new ArrayList<JSONObject>();
        if (dataList != null && dataList.size() != 0) {
            for (JSONObject json : dataList) {
                if ("0".equals(json.getString("timer"))) {
                    realTimeTask.add(json);
                } else {
                    timerTask.add(json);
                }
            }
            ReceiverInit.taskMap.put("realTime", realTimeTask);
            ReceiverInit.taskMap.put("timer", timerTask);
        } else {
            ReceiverInit.taskMap = null;
        }

    }


    private List<ConfigElement> initConfigElements() {
        List<ConfigElement> result = new ArrayList<>();
        //获取数据库中所有的配置信息
        List<TableConfigElement> configElements = cnfManagerDao.queryConfig();
        if (configElements == null || configElements.isEmpty()) {
            log.error("数据库配置信息为空!");
        } else {

            for (TableConfigElement tableConfigElement : configElements) {
                List<String> regexList = new ArrayList<>();
                ConfigElement configElement = new ConfigElement();
                //设置ip
                String ip = tableConfigElement.getIp();
                String port = tableConfigElement.getPort();
                if (ip.equals(ReceiverInit.ip) && port.equals(ReceiverInit.port)) {
                    configElement.setIp(ip);

                    //设置英文名
                    configElement.setEleName(tableConfigElement.getEleName());

                    //设置类型
                    configElement.setEleType(tableConfigElement.getEleType());

                    //设置cycleType
                    configElement.setCycleType(tableConfigElement.getCycleType());

                    //设置cycleNum
                    configElement.setCycleNum(tableConfigElement.getCycleNum());


                    //设置所属目录的值
                    String resource = tableConfigElement.getResource();

                    //设置文件名后缀
                    configElement.setEndWith(tableConfigElement.getEndWith());

                    //设置痕迹目录
                    String backupFile = ReceiverInit.wsp + File.separator + tableConfigElement.getBackupFile();
                    configElement.setBackupFile(backupFile);

                    //设置映射的类
                    configElement.setRecClass(tableConfigElement.getEleClass());

                    //设置是否缓存
                    configElement.setIsCache(tableConfigElement.getIsCache());

                    //设置中文名称
                    configElement.setCname(tableConfigElement.getCname());

                    //设置端口号
                    configElement.setPort(tableConfigElement.getPort());

                    //设置源目录
                    String dataResourceDir = readConfig.readTable(resource, ip) + File.separator + tableConfigElement.getResourceDir();
                    configElement.setDataResourceDir(dataResourceDir);

                    //设置缓存目录
                    String dataCacheDir = ReceiverInit.wsp + File.separator + tableConfigElement.getCacheDir();
                    configElement.setDataCacheDir(dataCacheDir);

                    //设置归档目录
                    String dataStorageDir = ReceiverInit.pds + File.separator + tableConfigElement.getStorageDir();
                    configElement.setDataStorageDir(dataStorageDir);

                    //设置正则
                    String regex = tableConfigElement.getRegex();
                    JSONArray jsonArray = (JSONArray) JSONArray.parse(regex);
                    for (Object object : jsonArray) {
                        regexList.add(object.toString());
                    }
                    //设置正则
                    configElement.setRegexList(regexList);
                    //设置totalNum
                    configElement.setTotalNum(tableConfigElement.getTotalNum());
                    result.add(configElement);
                }

            }
        }
        log.info("初始化字典完成！");
        return result;
    }
}
