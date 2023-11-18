package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.FrontSatDao;
import com.geovis.receiver.pojo.vo.FrontModasVo;
import com.geovis.receiver.pojo.vo.FrontSatVo;
import org.springframework.stereotype.Service;

@Service
public class FrontSatServiceImpl extends ServiceImpl<FrontSatDao, FrontSatVo> {
    public void saveOrUpdateObj(FrontSatVo vo) {

        QueryWrapper<FrontSatVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
