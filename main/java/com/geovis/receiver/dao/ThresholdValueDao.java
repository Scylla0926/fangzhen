package com.geovis.receiver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geovis.receiver.pojo.bean.ThresholdValueBean;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ThresholdValueDao extends BaseMapper<ThresholdValueBean> {
}
