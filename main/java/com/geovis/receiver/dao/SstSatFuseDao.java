package com.geovis.receiver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geovis.receiver.pojo.vo.SstFuseVo;
import com.geovis.receiver.pojo.vo.SubSurfaceBuoyVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SstSatFuseDao extends BaseMapper<SstFuseVo> {
}
