package com.geovis.receiver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geovis.receiver.pojo.vo.FrontStatVo;
import com.geovis.receiver.pojo.vo.YcStatVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 基于cora数据的跃层概率统计
 */
@Mapper
public interface FrontStatDao extends BaseMapper<FrontStatVo> {

}
