package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.FrontStatDao;
import com.geovis.receiver.dao.YcStatDao;
import com.geovis.receiver.pojo.vo.FrontSatVo;
import com.geovis.receiver.pojo.vo.FrontStatVo;
import com.geovis.receiver.pojo.vo.YcStatVo;
import org.springframework.stereotype.Service;

@Service
public class FrontStatServiceImpl extends ServiceImpl<FrontStatDao, FrontStatVo> {
    public void saveOrUpdateObj(FrontStatVo vo) {

        QueryWrapper<FrontStatVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
