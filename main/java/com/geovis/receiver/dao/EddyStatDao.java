package com.geovis.receiver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geovis.receiver.pojo.vo.EddyStatVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EddyStatDao extends BaseMapper<EddyStatVo> {
}
