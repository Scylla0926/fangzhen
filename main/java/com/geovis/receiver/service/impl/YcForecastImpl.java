package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.SstSatFuseDao;
import com.geovis.receiver.dao.YcForecastDao;
import com.geovis.receiver.pojo.vo.SstFuseVo;
import com.geovis.receiver.pojo.vo.YcForecastVo;
import org.springframework.stereotype.Service;

@Service
public class YcForecastImpl extends ServiceImpl<YcForecastDao, YcForecastVo> {
    public void saveOrUpdateObj(YcForecastVo vo) {
        QueryWrapper<YcForecastVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
