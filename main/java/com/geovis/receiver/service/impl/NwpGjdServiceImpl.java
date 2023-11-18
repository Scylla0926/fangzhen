package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.NwpGjdDao;
import com.geovis.receiver.pojo.vo.GjdNwpVo;
import org.springframework.stereotype.Service;

@Service
public class NwpGjdServiceImpl extends ServiceImpl<NwpGjdDao, GjdNwpVo> {
    public void saveOrUpdateObj(GjdNwpVo vo) {

        QueryWrapper<GjdNwpVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
