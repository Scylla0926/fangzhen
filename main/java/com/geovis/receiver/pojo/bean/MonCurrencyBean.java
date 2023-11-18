package com.geovis.receiver.pojo.bean;

import lombok.Data;

import java.util.Date;

/**
 * 卫星产品 mon类 入库信息实体类
 */
@Data
public class MonCurrencyBean {
    private String tableName;
    private String satelliteid;
    private String sensorid;
    private Date shootstarttime;
    private String filepath;
    private String dataname;
    private double lefttoplon;
    private double lefttoplat;
    private double rightbuttonlon;
    private double rightbuttonlat;
    private String productid;
    private Date insertdate;
    private double filesize;
    private String srcpath;
    private String insertDate;
}
