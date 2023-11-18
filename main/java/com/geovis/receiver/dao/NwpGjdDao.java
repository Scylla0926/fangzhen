package com.geovis.receiver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geovis.receiver.pojo.vo.GjdNwpVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NwpGjdDao extends BaseMapper<GjdNwpVo> {
}
