package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.Woa18Dao;
import com.geovis.receiver.pojo.vo.GjdNwpVo;
import com.geovis.receiver.pojo.vo.Woa18Vo;
import org.springframework.stereotype.Service;

@Service
public class Woa18ServiceImpl extends ServiceImpl<Woa18Dao, Woa18Vo> {
    public void saveOrUpdateObj(Woa18Vo vo) {

        QueryWrapper<Woa18Vo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
