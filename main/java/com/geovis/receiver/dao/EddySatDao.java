package com.geovis.receiver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.geovis.receiver.pojo.vo.EddySatVo;
import com.geovis.receiver.pojo.vo.EddyStatVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EddySatDao extends BaseMapper<EddySatVo> {
}
