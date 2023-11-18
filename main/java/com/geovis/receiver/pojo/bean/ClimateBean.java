package com.geovis.receiver.pojo.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author zls
 * @version 1.0.0
 * @Package com.geovis.receiver.pojo
 * @ClassName ClimateBean
 * @date 2023/3/10 16:01
 * @description TODO
 * 统计气候分析
 */
@Data
public class ClimateBean {
    private String tableName;
    private String fileName;
    private String fileSize;
    private String fileType;
    private String fileAbsolutePath;
    private String fileRelativePath;
    private String eleName;
    private String level;
    private String maxLon;
    private String minLon;
    private String maxLat;
    private String minLat;
    private String srcPath;
    private String statTime;
    private String year;
    private String month;
    private String statType;
    private Date createTime;
    private Date updateTime;
    private String day;

}
