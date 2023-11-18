package com.geovis.receiver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geovis.receiver.pojo.vo.TideVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TideDao extends BaseMapper<TideVo> {
}
