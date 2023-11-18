package com.geovis.receiver.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("ldb_dat_dg_argo_thermocline")
public class YcArgoVo {
    private Long id;
    @TableField("data_time")
    private Date dataTime;
    private String lat;
    private String lon;
    private String depth;
    private String salt;
    private String temp;
    private String sv;
    private String rho;

    @TableField("style_s")
    private String styleS;
    @TableField("style_t")
    private String styleT;
    @TableField("style_v")
    private String styleV;
    @TableField("style_r")
    private String styleR;
    @TableField("S")
    private String S;
    @TableField("T")
    private String T;
    @TableField("R")
    private String R;
    @TableField("V")
    private String V;
    @TableField("insert_time")
    private Date insertTime;
    @TableField("update_time")
    private Date updateTime;
    @TableField("json_path")
    private String jsonPath;
    @TableField("method")
    private String methodType;

    @TableField("platform_number")
    private String platformNumber;
    @TableField("cycle_number")
    private String cycleNumber;

}
