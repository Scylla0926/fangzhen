package com.geovis.receiver.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geovis.receiver.tools.SnowflakeIdGeneratorUtils;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.Random;

@Data
@TableName(value = "ldb_dat_obs_argo_fub")
@Accessors(chain = true)
public class ObsArgoFubBean {

    @TableField(exist = false)
    Random rand = new Random();
    @TableField(exist = false)
    SnowflakeIdGeneratorUtils snowflakeIdGeneratorUtils = new SnowflakeIdGeneratorUtils(rand.nextInt(1001));
    @TableId
    private long id = snowflakeIdGeneratorUtils.getNextId();

    @TableField("update_time")
    private Date updateTime = new Date();
    @TableField("insert_time")
    private Date insertTime = new Date();

    @TableField("src_file_relative_path")
    private String srcFileRelativePath;
    // 原文件名称
    @TableField("src_file_name")
    private String srcFileName;
    // 原文件大小
    @TableField("src_file_size")
    private Double srcFileSize;
    // 原文件 类型
    @TableField("src_file_type")
    private String srcFileType;
    // 源文件绝对路径
    @TableField("src_file_absolute_path")
    private String srcFileAbsolutePath;

    @TableField("data_time")
    private Date dataTime;

    // 平台编号
    @TableField("platform_number")
    private String platformNumber;

    // 周期编号
    @TableField("cycle_number")
    private String cycleNumber;

    // 创建日期
    @TableField("date_creation")
    private String dateCreation;

    //修改日期
    @TableField("date_update")
    private String dateUpdate;

    //项目 || 工程名称
    @TableField("projcet_name")
    private String projectName;

    //站名
    @TableField("pi_name")
    private String piName;

    @TableField("instrument_type")
    private String instrumentType;
    // 浮标序列号
    @TableField("float_serial_no")
    private String floatSerialNo;

    @TableField("firmware_version")
    private String firmwareVersion;

    // 仪器名称
    @TableField("wmo_instrument_type")
    private String wmoInstrumentType;

    // 传输方式 协议
    @TableField("transmission_system")
    private String transmissionSystem;

    // 定位系统
    @TableField("positioning_system")
    private String positioningSystem;


    private Double lat;

    private Double lon;

    // 气压
    private Double lp;
    @TableField("corrected_pressure")
    private Double correctedPressure;
    @TableField("quality_pressure")
    private Double qualityPressure;

    // 温度
    private Double at;

    @TableField("corrected_temperature")
    private Double correctedTemperature;

    @TableField("quality_temperature")
    private Double qualityTemperature;

    // 盐度
    private Double salt;

    @TableField("corrected_salinity")
    private Double correctedSalinity;

    @TableField("quality_salinity")
    private Double qualitySalinity;

    @TableField("station_chn")
    private String stationChn;

}
