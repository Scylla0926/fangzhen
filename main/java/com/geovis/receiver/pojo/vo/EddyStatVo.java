package com.geovis.receiver.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 基于cora数据的跃层概率统计
 */
@Data
@TableName("ldb_dat_stat_avios_eddy")
public class EddyStatVo {
    private Long id;
    @TableField("png_file_name")
    private String pngFileName;
    @TableField("png_file_size")
    private Double pngFileSize;
    @TableField("src_file_name")
    private String srcFileName;
    @TableField("src_file_size")
    private Double srcFileSize;
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
}
