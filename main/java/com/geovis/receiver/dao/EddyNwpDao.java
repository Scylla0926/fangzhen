package com.geovis.receiver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geovis.receiver.pojo.vo.EddyNwpVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EddyNwpDao extends BaseMapper<EddyNwpVo> {
}
