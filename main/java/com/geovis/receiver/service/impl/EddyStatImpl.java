package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.EddyStatDao;
import com.geovis.receiver.pojo.vo.EddyStatVo;
import org.springframework.stereotype.Service;

@Service
public class EddyStatImpl extends ServiceImpl<EddyStatDao, EddyStatVo> {
    public void saveOrUpdateObj(EddyStatVo vo) {

        QueryWrapper<EddyStatVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
