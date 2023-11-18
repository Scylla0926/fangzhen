package com.geovis.receiver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geovis.receiver.pojo.vo.CurrentCoraVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CoraCurrentDao extends BaseMapper<CurrentCoraVo> {
}
