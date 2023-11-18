package com.geovis.receiver.pojo.model;

import com.geovis.receiver.thread.ReceiverConsumeThread;
import com.geovis.receiver.thread.ReceiverProducerThread;
import lombok.Data;
import org.springframework.stereotype.Component;

/***
 * @Package: com.geovis.receiver.pojo
 * @ClassName: Downtown
 * @author     ：yangxl
 * @date       ：Created in 2022/7/31 9:32
 * @description：线程整体管理工具
 * @modified By：
 * @version:
 */
@Data
public class Downtown {

    private String name;
    private ReceiverProducerThread producer;
    private ReceiverConsumeThread consumer;
}
