package com.geovis.receiver.pojo.model;

import lombok.Data;

/**
 * @Author liusong
 * @Date 2023/1/5 19:02
 * @Description 跃层解释应用实体类
 **/
@Data
public class ThermocLine {
    private String key;
    private String value;
    private String description;
    private String keyType;
}
