package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.CzStatDao;
import com.geovis.receiver.pojo.vo.CzStatVo;
import org.springframework.stereotype.Service;

@Service
public class CzStatServiceImpl extends ServiceImpl<CzStatDao, CzStatVo> {

    public void saveOrUpdateObj(CzStatVo czStatVo) {
        QueryWrapper<CzStatVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", czStatVo.getPngFileName());

        boolean remove = remove(queryWrapper);
        saveOrUpdate(czStatVo);
    }
}
