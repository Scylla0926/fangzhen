package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.CoraCurrentDao;
import com.geovis.receiver.pojo.vo.CurrentCoraVo;
import org.springframework.stereotype.Service;

@Service
public class CoraCurrentServiceImpl extends ServiceImpl<CoraCurrentDao, CurrentCoraVo> {
    public void saveOrUpdateObj(CurrentCoraVo vo) {

        QueryWrapper<CurrentCoraVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
