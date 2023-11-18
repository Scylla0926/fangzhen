package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.YcCoraDao;
import com.geovis.receiver.pojo.vo.YcCoraVo;
import org.springframework.stereotype.Service;

@Service
public class YcCoraServiceImpl extends ServiceImpl<YcCoraDao, YcCoraVo> {
    public void saveOrUpdateObj(YcCoraVo vo) {

        QueryWrapper<YcCoraVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
