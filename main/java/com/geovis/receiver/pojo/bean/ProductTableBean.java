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
@TableName(value = "\"ldb_dat_sys_data_source\"")
public class    ProductTableBean {

    private Integer id;

    @TableField(value = "\"product_type\"")
    private String productType;

    @TableField(value = "\"table_name\"")
    private String tableName;

    @TableField(value = "\"data_source\"")
    private String dataSource;

    @TableField(value = "\"last_source\"")
    private String lastSource;

    @TableField(value = "\"data_time\"")
    private String dataTime;

    @TableField(value = "\"time_type\"")
    private String timeType;

    @TableField(value = "\"level\"")
    private String level;

}
