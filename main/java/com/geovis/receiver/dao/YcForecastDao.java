package com.geovis.receiver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geovis.receiver.pojo.vo.SstFuseVo;
import com.geovis.receiver.pojo.vo.YcForecastVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface YcForecastDao extends BaseMapper<YcForecastVo> {
}
