package com.geovis.receiver.thread;

import com.geovis.receiver.pojo.model.ConfigElement;
import com.geovis.receiver.receiver.ReceiverBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

/***
 * 类描述: 生产者线程
 * @author yxl
 * @Date: 10:01 2020/9/19
 */
@Scope("prototype")
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiverProducerThread extends Thread {

    private ConfigElement configElement;

    @Autowired
    private ReceiverBase receiverBase;




    public boolean isRunning = true;

    @Override
    public void run() {
        Timer timer = new Timer();

        TimerTask scanTask = new TimerTask() {
            @Override
            public void run() {
                if (isRunning) {
                    long st = System.currentTimeMillis();
                    receiverBase.receiverProducer();
                    long et = System.currentTimeMillis();
//                    System.out.println("文件扫描间隔："+(et-st)/1000+"-秒");
                }
            }
        };

        timer.schedule(scanTask, 0, 3000);
    }

    @Bean
    public ReceiverProducerThread getProducerInstance(ConfigElement configElement, ReceiverBase receiverBase) {
        ReceiverProducerThread receiverProducerThread = new ReceiverProducerThread();
        receiverProducerThread.setName(configElement.getEleName()+"_producer");
        receiverProducerThread.configElement = configElement;
        receiverProducerThread.receiverBase = receiverBase;
        return receiverProducerThread;
    }

}
