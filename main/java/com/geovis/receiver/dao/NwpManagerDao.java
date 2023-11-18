package com.geovis.receiver.dao;

import com.geovis.receiver.pojo.model.Nwp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author liusong
 * @Date 2022/9/6 18:02
 * @Description
 **/
@Mapper
@Repository
public interface NwpManagerDao {
    /**
     * 插入NWP数据
     * @param nwp
     * @return
     */
    boolean insertNwpData(@Param("tableName") String tableName, @Param("nwp") Nwp nwp);

    /**
     * 删除一样的文件
     * @param fileName
     * @return
     */
    boolean deleteNwpData(@Param("tableName") String tableName, @Param("fileName") String fileName);



}
