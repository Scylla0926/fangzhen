package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.CurrentHyComDao;
import com.geovis.receiver.dao.CurrentOscarDao;
import com.geovis.receiver.pojo.vo.CurrentHycomVo;
import com.geovis.receiver.pojo.vo.CurrentOscarVo;
import org.springframework.stereotype.Service;

@Service
public class CurrentOscarServiceImpl extends ServiceImpl<CurrentOscarDao, CurrentOscarVo> {
    public void saveOrUpdateObj(CurrentOscarVo seaCurrentVo) {
        QueryWrapper<CurrentOscarVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", seaCurrentVo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(seaCurrentVo);
    }
}
