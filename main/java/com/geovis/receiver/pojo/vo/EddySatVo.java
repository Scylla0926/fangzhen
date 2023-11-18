package com.geovis.receiver.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("ldb_dat_dg_sat_eddy")
public class EddySatVo {
    private Long id;
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
    @TableField("ele_name")
    private String eleName;
    @TableField("region_type")
    private String regionType;
    private String method;
    @TableField("insert_time")
    private Date insertTime;
    @TableField("update_time")
    private Date updateTime;
    @TableField("data_time")
    private Date dataTime;

}
