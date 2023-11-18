package com.geovis.receiver.pojo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author liusong
 * @Date 2022/9/6 15:52
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Nwp extends Product{

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件大小--KB形式
     */
    private String fileSize;

    /**
     * 文件日期
     */
    private String fileDate;

    /**
     * 年
     */
    private String year;

    /**
     * 月
     */
    private String month;

    /**
     * 日
     */
    private String day;

    /**
     * 时
     */
    private String hour;

    /**
     * 分
     */
    private String minute;

    /**
     * 秒
     */
    private String second;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 网络路径
     */
    private String netPath;

    /**
     * 插入时间
     */
    private String insertDate;

    /**
     * 起报时间
     */
    private String startTime;

    /**
     * 预报时间
     */
    private String forecastTime;

    /**
     * 起止结束时间
     */
    private String beginTime;
    private String endTime;


    @Override
    public String toStringAttribute() {
        return null;
    }

    @Override
    public String toStringFormat() {
        return null;
    }

    @Override
    public String toStringValue() {
        return null;
    }
}
