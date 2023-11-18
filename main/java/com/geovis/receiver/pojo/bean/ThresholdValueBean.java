package com.geovis.receiver.pojo.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ldb_dat_sys_threshold_value")
public class ThresholdValueBean {
    @TableField("ele_type")
    private String eleType;

    @TableField("threshold_value")
    private String value;

    @TableField("ele_name")
    private String eleName;
}
