package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.EddyCoraDao;
import com.geovis.receiver.pojo.vo.EddyCoraVo;
import org.springframework.stereotype.Service;

@Service
public class EddyCoraServiceImpl extends ServiceImpl<EddyCoraDao, EddyCoraVo> {
    public void saveOrUpdateObj(EddyCoraVo vo) {
        QueryWrapper<EddyCoraVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_name", vo.getFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
