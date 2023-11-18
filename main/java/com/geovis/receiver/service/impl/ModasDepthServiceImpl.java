package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.CoraDepthDao;
import com.geovis.receiver.dao.ModasDepthDao;
import com.geovis.receiver.pojo.vo.DepthCoraVo;
import com.geovis.receiver.pojo.vo.DepthModasVo;
import org.springframework.stereotype.Service;

@Service
public class ModasDepthServiceImpl extends ServiceImpl<ModasDepthDao, DepthModasVo> {
    public void saveOrUpdateObj(DepthModasVo vo) {

        QueryWrapper<DepthModasVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
