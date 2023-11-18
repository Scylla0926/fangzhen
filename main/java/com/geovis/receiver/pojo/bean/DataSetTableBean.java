package com.geovis.receiver.pojo.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qhy
 * @version 1.0.0
 * @Package com.geovis.dataservice.bean
 * @ClassName ProductTable
 * @date 2022/9/20 18:17
 * @description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "\"ldb_dat_sys_dataset\"")
public class DataSetTableBean {

    private Integer id;

    @TableField(value = "\"product_type\"")
    private String productType;

    @TableField(value = "\"data_source\"")
    private String dataSource;

}
