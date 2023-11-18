package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.FrontNwpDao;
import com.geovis.receiver.pojo.vo.FrontCoraVo;
import com.geovis.receiver.pojo.vo.FrontNwpVo;
import org.springframework.stereotype.Service;

@Service
public class FrontForecastImpl extends ServiceImpl<FrontNwpDao, FrontNwpVo> {
    public void saveOrUpdateObj(FrontNwpVo vo) {

        QueryWrapper<FrontNwpVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
