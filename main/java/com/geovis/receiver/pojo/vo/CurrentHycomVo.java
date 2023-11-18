package com.geovis.receiver.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
    @TableName("ldb_dat_dg_hycom_current")
public class CurrentHycomVo {
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
    private String method;
    @TableField("data_time")
    private Date date;
    @TableField("forecast_time")
    private String forecastTime;
    private String timer;
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
    private Double level;

}
