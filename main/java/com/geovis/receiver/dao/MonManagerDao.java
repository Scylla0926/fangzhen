package com.geovis.receiver.dao;

import com.geovis.receiver.pojo.bean.MonCurrencyBean;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
/**
 *  卫星产品 入库信息类
 */
public interface MonManagerDao {
    public boolean insetMonDb(MonCurrencyBean monCurrencyBean);
}
