package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.CurrentHyComDao;
import com.geovis.receiver.pojo.vo.CurrentHycomVo;
import org.springframework.stereotype.Service;

@Service
public class CurrentHyComServiceImpl extends ServiceImpl<CurrentHyComDao, CurrentHycomVo> {
    public void saveOrUpdateObj(CurrentHycomVo seaCurrentVo) {
        QueryWrapper<CurrentHycomVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", seaCurrentVo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(seaCurrentVo);
    }
}
