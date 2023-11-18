package com.geovis.receiver.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 基于cora数据的海洋锋统计
 */
@Data
@TableName("ldb_dat_stat_cora_front")
public class FrontStatVo {
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
    @TableField("stat_type")
    private String statType;
    private String year;
    private String month;
    private String ten;
    private String hou;
    private String season;
    @TableField("region_type")
    private String regionType;
    @TableField("method")
    private String dgMethod;
    @TableField("insert_time")
    private Date insertTime;
    @TableField("update_time")
    private Date updateTime;
    @TableField("number_x")
    private Integer numberX;
    @TableField("number_y")
    private Integer numberY;
    private Double level;

    private String tableName;
    private String productType;////产品类型，对应数据库中ldb_dat_sys_data_source表的product_type字段

}
