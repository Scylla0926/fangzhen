package com.geovis.receiver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geovis.receiver.pojo.vo.CurrentNwpVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NwpSeaCurrentDao extends BaseMapper<CurrentNwpVo> {
}
