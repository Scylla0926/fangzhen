package com.geovis.receiver.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 *  分发数据库表控制接口
 */
@Mapper
@Repository
public interface SendTaskManagerDao {

    public List<JSONObject> queryTask();

}
