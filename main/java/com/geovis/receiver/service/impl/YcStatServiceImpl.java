package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.YcStatDao;
import com.geovis.receiver.pojo.vo.YcStatVo;
import org.springframework.stereotype.Service;

@Service
public class YcStatServiceImpl extends ServiceImpl<YcStatDao, YcStatVo> {
    public void saveOrUpdateObj(YcStatVo vo) {

        QueryWrapper<YcStatVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
