package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.DepthStatDao;
import com.geovis.receiver.pojo.vo.CurrentStatVo;
import org.springframework.stereotype.Service;

@Service
public class CurrentStatServiceImpl extends ServiceImpl<DepthStatDao, CurrentStatVo> {
    public void saveOrUpdateObj(CurrentStatVo vo) {

        QueryWrapper<CurrentStatVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
