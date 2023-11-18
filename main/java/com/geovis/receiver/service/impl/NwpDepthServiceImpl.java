package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.CoraDepthDao;
import com.geovis.receiver.dao.NwpDepthDao;
import com.geovis.receiver.pojo.vo.DepthCoraVo;
import com.geovis.receiver.pojo.vo.DepthNwpVo;
import org.springframework.stereotype.Service;

@Service
public class NwpDepthServiceImpl extends ServiceImpl<NwpDepthDao, DepthNwpVo> {

    public void saveOrUpdateObj(DepthNwpVo vo) {
        QueryWrapper<DepthNwpVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
