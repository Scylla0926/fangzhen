package com.geovis.receiver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geovis.receiver.pojo.vo.SssFuseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SssSatFuseDao extends BaseMapper<SssFuseVo> {
}
