package com.geovis.receiver.thread;

import com.geovis.receiver.pojo.model.ConfigElement;
import com.geovis.receiver.receiver.ReceiverBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

/***
 * 类描述: 消费者线程
 * @author yxl
 * @Date: 10:01 2020/9/19
 */
@Scope("prototype")
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class ReceiverConsumeThread extends Thread{

    private ConfigElement configElement;

    private ReceiverBase receiverBase;

    public boolean isRunning = true;

    @Override
    public void run() {
        Timer timer = new Timer();
        TimerTask scanTask = new TimerTask() {
            @Override
            public void run() {
                if(isRunning){
                    receiverBase.receiverConsumer();
                }
            }
        };
        timer.schedule(scanTask, 0, 1000);
    }

    @Bean
    public ReceiverConsumeThread getConsumeInstance(ConfigElement configElement, ReceiverBase receiverBase){
        ReceiverConsumeThread receiverConsumeThread = new ReceiverConsumeThread();
        receiverConsumeThread.setName(configElement.getEleName()+"_consumer");
        receiverConsumeThread.configElement = configElement;
        receiverConsumeThread.receiverBase = receiverBase;
        return receiverConsumeThread;
    }


}
