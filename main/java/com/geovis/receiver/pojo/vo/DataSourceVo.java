package com.geovis.receiver.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ldb_dat_sys_data_source")
public class DataSourceVo {
    @TableField("ele_type")
    private String eleType;
    @TableField("time_type")
    private String timeType;
}
