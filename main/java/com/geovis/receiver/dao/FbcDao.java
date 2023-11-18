package com.geovis.receiver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geovis.receiver.pojo.vo.CzStatVo;
import com.geovis.receiver.pojo.vo.FbcVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FbcDao extends BaseMapper<FbcVo> {
}
