package com.geovis.receiver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geovis.receiver.pojo.vo.FbcVo;
import com.geovis.receiver.pojo.vo.Woa18Vo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Woa18Dao extends BaseMapper<Woa18Vo> {
}
