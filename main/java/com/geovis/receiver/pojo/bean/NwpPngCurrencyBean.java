package com.geovis.receiver.pojo.bean;

import lombok.Data;

import java.util.Date;


/**
 * 数值预报数据入库通用类
 */

@Data
public class NwpPngCurrencyBean {

    // 表名
    private String tableName;
    // 文件名称
    private String fileName;
    //文件大小
    private String fileSize;
    //文件类型
    private String fileType;
    //绝对路径
    private String fileAbsolutePath;
    //相对目录
    private String fileRelativePath;
    //要素
    private String eleName;
    //层级
    private String level;
    //文件时间
    private String dataTime;
    private String maxLon;

    private String minLon;
    private String maxLat;
    private String minLat;
    //起报时次
    private String forecastTime;
    //预报时效
    private String timer;
    // png 文件的输入源文件相对目录
    private String srcPath;

    // 文件首次入库时间
    private Date createTime;
    //文件修改时间
    private Date updateTime;
}
