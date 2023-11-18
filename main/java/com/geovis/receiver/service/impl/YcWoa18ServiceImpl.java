package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.YcWoa18Dao;
import com.geovis.receiver.pojo.vo.YcStatVo;
import com.geovis.receiver.pojo.vo.YcWoa18Vo;
import org.springframework.stereotype.Service;

@Service
public class YcWoa18ServiceImpl extends ServiceImpl<YcWoa18Dao, YcWoa18Vo> {
    public void saveOrUpdateObj(YcWoa18Vo vo) {

        QueryWrapper<YcWoa18Vo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
