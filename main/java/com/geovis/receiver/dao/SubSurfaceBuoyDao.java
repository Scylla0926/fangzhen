package com.geovis.receiver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geovis.receiver.pojo.vo.FbcVo;
import com.geovis.receiver.pojo.vo.SubSurfaceBuoyVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SubSurfaceBuoyDao extends BaseMapper<SubSurfaceBuoyVo> {
}
