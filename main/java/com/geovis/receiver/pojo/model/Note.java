package com.geovis.receiver.pojo.model;

import lombok.Data;
import org.springframework.stereotype.Component;

/***
 * @Package: com.geovis.receiver.pojo
 * @ClassName: Note
 * @author     ：yangxl
 * @date       ：Created in 2022/7/27 23:30
 * @description：
 * @modified By：cnf 数据库配置信息表
 * @version:
 */
@Data
@Component
public class Note {

    // ip 节点ip
    private String ip;

    // 对应节点 key
    private String cnfKey;

    // 对应节点 value
    private String cnfValue;
}
