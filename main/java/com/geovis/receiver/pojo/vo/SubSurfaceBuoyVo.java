package com.geovis.receiver.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("ldb_dat_obs_subsf_buoy")
public class SubSurfaceBuoyVo {
    private String id;

    @TableField("data_time")
    private Date dataTime;

    @TableField("cs")
    private double cs;

    @TableField("lat")
    private double lat;

    @TableField("lon")
    private double lon;

    @TableField("depth")
    private double depth;

    @TableField("rho")
    private double rho;

    @TableField("s")
    private double s;

    @TableField("t")
    private double t;

    @TableField("u")
    private double u;

    @TableField("v")
    private double v;

    @TableField("insert_time")
    private Date insertTime;

    @TableField("serial_no")
    private String serialNo;

}
