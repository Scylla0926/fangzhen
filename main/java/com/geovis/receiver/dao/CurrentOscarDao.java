package com.geovis.receiver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geovis.receiver.pojo.vo.CurrentHycomVo;
import com.geovis.receiver.pojo.vo.CurrentOscarVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CurrentOscarDao extends BaseMapper<CurrentOscarVo> {
}
