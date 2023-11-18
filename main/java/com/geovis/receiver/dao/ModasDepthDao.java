package com.geovis.receiver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geovis.receiver.pojo.vo.DepthCoraVo;
import com.geovis.receiver.pojo.vo.DepthModasVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ModasDepthDao extends BaseMapper<DepthModasVo> {
}
