package com.geovis.receiver.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geovis.receiver.pojo.bean.WorkFlowBean;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("workflow_db")
public interface WorkFlowDao extends BaseMapper<WorkFlowBean> {
}
