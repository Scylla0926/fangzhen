package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.FbcDao;
import com.geovis.receiver.dao.TideDao;
import com.geovis.receiver.pojo.vo.FbcVo;
import com.geovis.receiver.pojo.vo.TideVo;
import org.springframework.stereotype.Service;

@Service
public class TideServiceImpl extends ServiceImpl<TideDao, TideVo> {
    public void saveOrUpdateObj(TideVo vo) {

        QueryWrapper<TideVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
