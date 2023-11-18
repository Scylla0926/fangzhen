package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.EddyHyComDao;
import com.geovis.receiver.pojo.vo.EddyHyComVo;
import org.springframework.stereotype.Service;

@Service
public class EddyHyComServiceImpl extends ServiceImpl<EddyHyComDao, EddyHyComVo> {
    public void saveOrUpdateObj(EddyHyComVo vo) {
        QueryWrapper<EddyHyComVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_name", vo.getFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
