package com.geovis.receiver.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.geovis.receiver.tools.SnowflakeIdGeneratorUtils;
import lombok.Data;

import java.util.Date;
import java.util.Random;

/**
 * @param <T>
 */
@Data
public class MultiSourceBaseEntity<T> {
    @TableField(exist = false)
    Random rand = new Random();
    @TableField(exist = false)
    SnowflakeIdGeneratorUtils snowflakeIdGeneratorUtils = new SnowflakeIdGeneratorUtils(rand.nextInt(1001));
    @TableId
    private long id = snowflakeIdGeneratorUtils.getNextId();
    // 数据时间
    @TableField("data_time")
    private Date dataTime;

    @TableField("update_time")
    private Date updateTime = new Date();
    @TableField("insert_time")
    private Date insertTime = new Date();

    @TableField("src_file_relative_path")
    private String srcFileRelativePath;
    // 原文件名称
    @TableField("src_file_name")
    private String srcFileName;
    // 原文件大小
    @TableField("src_file_size")
    private Double srcFileSize;
    // 原文件 类型
    @TableField("src_file_type")
    private String srcFileType;
    // 源文件绝对路径
    @TableField("src_file_absolute_path")
    private String srcFileAbsolutePath;
}
