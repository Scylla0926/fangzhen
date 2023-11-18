package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.MldWoa18Dao;
import com.geovis.receiver.pojo.vo.MldWoa18Vo;
import com.geovis.receiver.pojo.vo.YcWoa18Vo;
import org.springframework.stereotype.Service;

@Service
public class MldWoa18ServiceImpl extends ServiceImpl<MldWoa18Dao, MldWoa18Vo> {
    public void saveOrUpdateObj(MldWoa18Vo vo) {

        QueryWrapper<MldWoa18Vo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
