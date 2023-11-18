package com.geovis.receiver.factory;

import com.geovis.receiver.pojo.model.ConfigElement;
import com.geovis.receiver.pojo.model.Downtown;
import com.geovis.receiver.receiver.ReceiverBase;
import com.geovis.receiver.thread.ReceiverConsumeThread;
import com.geovis.receiver.thread.ReceiverProducerThread;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/***
 * 类描述: 初始化后台引接程序工程
 * @author yxl
 * @Date: 10:01 2020/9/19
 */
@Data
@Slf4j
@Component
@Async("asyncServiceExecutor")
@Scope("prototype")
public class ReceiverFactory {

    @Autowired
    private ReceiverConsumeThread receiverConsumeThread;

    @Autowired
    private ReceiverProducerThread receiverProducerThread;

    @Autowired
    private ApplicationContext applicationContext;



    public void CSHReceiver(ConfigElement configElement) {

//        synchronized (this){
            Thread thread = Thread.currentThread();
            log.info("处理" + configElement.getEleName() + "类型数据的线程:" + thread.getName());
            if (configElement.getDataResourceDir().equals("") || configElement.getDataResourceDir() == null) {
                log.error(configElement.getEleType() + "类型数据地址不存在:" + configElement.getDataResourceDir());
                return;
            }

            ReceiverBase receiverBase = createClass(configElement);

            //如果地址存在那么就创建两个线程，一个为生产者一个消费者。
            //启动两个服务
            if (configElement.getEleName() != null) {
                Downtown downtown = new Downtown();
                ReceiverProducerThread receiverProducer = receiverProducerThread.getProducerInstance(configElement, receiverBase);//生产者
                ReceiverConsumeThread receiverConsume = receiverConsumeThread.getConsumeInstance(configElement, receiverBase);//消费者
                receiverProducer.start();
                receiverConsume.start();
                downtown.setName(configElement.getEleName());
                downtown.setProducer(receiverProducer);
                downtown.setConsumer(receiverConsume);
                ReceiverInit.downtownList.add(downtown);
            }
        }
//    }

    /***
     *方法描述:
     * @param: [configElement]
     * @author: yangxl
     * @Date: 2022/7/27 22:38
     * @Return com.geovis.receiver.receiver.ReceiverBase
     *
     */
    private ReceiverBase createClass(ConfigElement configElement) {
        ReceiverBase receiverBase = null;
        try {
            String clazz = configElement.getRecClass();
            Class<?> cls = Class.forName(clazz);
            receiverBase = (ReceiverBase) applicationContext.getBean(cls);
            receiverBase.setConfigElement(configElement);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receiverBase;
    }
}
