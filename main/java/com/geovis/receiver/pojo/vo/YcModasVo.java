package com.geovis.receiver.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 跃层诊断产品
 */
@Data
@TableName("ldb_dat_dg_modas_thermocline")
public class YcModasVo {
    private Long id;
    @TableField("png_file_name")
    private String pngFileName;
    @TableField("png_file_size")
    private Long pngFileSize;
    @TableField("src_file_name")
    private String srcFileName;
    @TableField("src_file_size")
    private Long srcFileSize;
    @TableField("src_file_type")
    private String srcFileType;
    @TableField("png_file_absolute_path")
    private String pngFileAbsolutePath;
    @TableField("png_file_relative_path")
    private String pngFileRelativePath;
    @TableField("src_file_absolute_path")
    private String srcFileAbsolutePath;
    @TableField("ele_name")
    private String eleName;
    @TableField("method")
    private String dgMethod;
    @TableField("region_type")
    private String regionType;
    @TableField("insert_time")
    private Date insertTime;
    @TableField("update_time")
    private Date updateTime;
    @TableField("number_x")
    private Integer numberX;
    @TableField("number_y")
    private Integer numberY;

    @TableField("data_time")
    private Date dataTime;

    @TableField("level")
    private Double level;
    //起报时次
    private String forecastTime;

    @TableField("timer")
    private String timer;

//    private String productType;////产品类型，对应数据库中ldb_dat_sys_data_source表的product_type字段
}
