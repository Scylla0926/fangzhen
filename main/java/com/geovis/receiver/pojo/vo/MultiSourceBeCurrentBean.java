package com.geovis.receiver.pojo.vo;

import com.geovis.receiver.tools.SnowflakeIdGeneratorUtils;
import lombok.Data;

import java.util.Date;

/**
 * 多源数据 通用入库实体类
 */
@Data
public class MultiSourceBeCurrentBean extends MultiSourceBaseEntity<MultiSourceBeCurrentBean> {
    // 表名
    private String tableName;
    private String fileName;
    // 文件大小
    private Double fileSize;
    // 文件类型
    private String fileType;
    // 要素名称
    private String eleName;
    // 层级
    private Double level;
    /// 层级类型
    private String levelType;
    // 经度开始
    private Double lonStart;
    // 经度结束
    private Double lonEnd;
    // 纬度开始
    private Double latStart;
    // 纬度结束
    private Double latEnd;
    // 文件绝对路径
    private String fileAbsolutePath;
    // 文件相对路径
    private String fileRelativePath;

    // x轴个数
    private Double numberX;
    // y轴个数
    private Double numberY;

    private String region;



}
