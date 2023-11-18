package com.geovis.receiver.dao;

import com.geovis.receiver.pojo.bean.ClimateBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author zls
 * @version 1.0.0
 * @Package com.geovis.receiver.dao
 * @ClassName NwpClimateDao
 * @date 2023/3/13 14:28
 * @description TODO
 * 历史统计数据入库
 */
@Mapper
@Repository
public interface NwpClimateDao {
    // 所有数值预报产品入库接口
    boolean insertNwpCurrentPng(ClimateBean climateBean);

    // 查询文件名称 判断是否已经入库
    int selectNwpPngName(@Param("tableName") String tableName, @Param("fileName") String fileName);

    // 第二次入库时 文件更新时间
    boolean updataNwpPng(ClimateBean climateBean);
}
