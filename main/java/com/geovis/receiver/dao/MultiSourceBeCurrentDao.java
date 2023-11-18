package com.geovis.receiver.dao;


import com.geovis.receiver.pojo.vo.MultiSourceBeCurrentBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MultiSourceBeCurrentDao {

    int insertMultiSourceBeCurrent(@Param("multiSourceBeCurrentBeanList") List<MultiSourceBeCurrentBean> multiSourceBeCurrentBeanList);

}
