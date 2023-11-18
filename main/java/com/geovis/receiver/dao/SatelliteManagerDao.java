package com.geovis.receiver.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/***
 * 卫星数据/产品 数据库操作类
 */
@Mapper
@Repository
public interface SatelliteManagerDao {

    public void deleteSatellite(@Param("sql") String sql);

    public boolean insertSatellite(@Param("sql") String sql);
}
