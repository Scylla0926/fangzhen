package com.geovis.receiver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geovis.receiver.pojo.vo.FbcVo;
import com.geovis.receiver.pojo.vo.HjSzVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HjszDao extends BaseMapper<HjSzVo> {
}
