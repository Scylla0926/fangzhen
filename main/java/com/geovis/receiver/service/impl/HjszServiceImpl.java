package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.HjszDao;
import com.geovis.receiver.pojo.vo.FrontStatVo;
import com.geovis.receiver.pojo.vo.HjSzVo;
import org.springframework.stereotype.Service;

@Service
public class HjszServiceImpl extends ServiceImpl<HjszDao, HjSzVo> {
    public void saveOrUpdateObj(HjSzVo vo) {

        QueryWrapper<HjSzVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
