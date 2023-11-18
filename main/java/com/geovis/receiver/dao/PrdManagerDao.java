package com.geovis.receiver.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PrdManagerDao {

    public List<JSONObject> queryPrd();
}
