package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.CoraDepthDao;
import com.geovis.receiver.pojo.vo.CurrentCoraVo;
import com.geovis.receiver.pojo.vo.DepthCoraVo;
import org.springframework.stereotype.Service;

@Service
public class CoraDepthServiceImpl extends ServiceImpl<CoraDepthDao, DepthCoraVo> {
    public void saveOrUpdateObj(DepthCoraVo vo) {

        QueryWrapper<DepthCoraVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
