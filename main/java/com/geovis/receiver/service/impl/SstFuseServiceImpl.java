package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.FrontCoraDao;
import com.geovis.receiver.dao.SstSatFuseDao;
import com.geovis.receiver.pojo.vo.FrontCoraVo;
import com.geovis.receiver.pojo.vo.SstFuseVo;
import org.springframework.stereotype.Service;

@Service
public class SstFuseServiceImpl extends ServiceImpl<SstSatFuseDao, SstFuseVo> {
    public void saveOrUpdateObj(SstFuseVo vo) {

        QueryWrapper<SstFuseVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
