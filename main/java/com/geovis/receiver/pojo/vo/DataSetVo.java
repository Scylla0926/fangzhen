package com.geovis.receiver.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ldb_dat_sys_dataset")
public class DataSetVo {
    @TableField("product_type")
    private String productType;

    @TableField("data_source")
    private String dataSource;
}
