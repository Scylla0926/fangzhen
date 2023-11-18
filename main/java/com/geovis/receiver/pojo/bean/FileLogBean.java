package com.geovis.receiver.pojo.bean;

import lombok.Data;

/**
 *  文件处理日志表  实体类
 */
@Data
public class FileLogBean {

    private String tableName;
    // 文件名称
    private String fileName;
    // 文件大小
    private String fileSize;
    //入库时间
    private String createTime;
    // 入库开始时间
    private String startTime;
    // 入库结束时间
    private String endTime;
    // 文件处理状态
    private String status;
    // 文件类型
    private String fileType;
    //文件处理总耗时
    private String runTime;

}
