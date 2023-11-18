package com.geovis.receiver.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("ldb_dat_sat_modis_inw")
public class InwModisVo {
    private Long id;
    @TableField("data_time")
    private Date dataTime;

    @TableField("insert_time")
    private Date insertTime;
    @TableField("update_time")
    private Date updateTime;
    @TableField("file_name")
    private String fileName;
    @TableField("file_size")
    private String fileSize;
    @TableField("file_type")
    private String fileType;
    @TableField("file_relative_path")
    private String fileRelativePath;
    @TableField("file_absolute_path")
    private String fileAbsolutePath;
}
