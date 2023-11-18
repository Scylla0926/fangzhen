package com.geovis.receiver.tools;

import com.geovis.receiver.dao.CnfManagerDao;
import com.geovis.receiver.factory.ReceiverInit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPubSub;

import javax.annotation.Resource;

/**
 * @Author liusong
 * @Date 2023/1/5 20:07
 * @Description
 **/
@Slf4j
public class RedisSubListener extends JedisPubSub {
    @Resource
    CnfManagerDao cnfManagerDao;

    @Autowired
    private ReadConfig readConfig;

    @Override
    public void onMessage(String channel, String message) {
        log.info("监听到通道:{} 的消息:{}",channel,message);
        if (message.contains("true")){
            //ReceiverInit.thermocLineList = cnfManagerDao.queryThemocLine();
            ReceiverInit.thresholdShallowS = readConfig.readConfigTable("thresholdShallowS");
            ReceiverInit.thresholdShallowT = readConfig.readConfigTable("thresholdShallowT");
            ReceiverInit.thresholdDeepT = readConfig.readConfigTable("thresholdDeepT");
            ReceiverInit.thresholdShallowR = readConfig.readConfigTable("thresholdShallowR");
            ReceiverInit.thresholdDeepS = readConfig.readConfigTable("thresholdDeepS");
            ReceiverInit.thresholdDeepR = readConfig.readConfigTable("thresholdDeepR");
            log.info("修改之后的所有的值是{}",""+ReceiverInit.thresholdShallowS+","+ReceiverInit.thresholdShallowT+","+ReceiverInit.thresholdDeepT+","+ReceiverInit.thresholdShallowR+","+ReceiverInit.thresholdDeepS+","+ReceiverInit.thresholdDeepR);
        }
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        log.info("订阅:{} 通道!", channel);
    }
}
