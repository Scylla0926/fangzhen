package com.geovis.receiver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geovis.receiver.pojo.vo.InwModisVo;
import com.geovis.receiver.pojo.vo.YcArgoVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InwModisDao extends BaseMapper<InwModisVo> {
}
