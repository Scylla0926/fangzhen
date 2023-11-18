package com.geovis.receiver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geovis.receiver.pojo.vo.YcCoraVo;
import com.geovis.receiver.pojo.vo.YcModasVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface YcCoraDao extends BaseMapper<YcCoraVo> {

}
