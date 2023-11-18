package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.FrontCoraDao;
import com.geovis.receiver.pojo.vo.FbcVo;
import com.geovis.receiver.pojo.vo.FrontCoraVo;
import org.springframework.stereotype.Service;

@Service
public class FrontCoraServiceImpl extends ServiceImpl<FrontCoraDao, FrontCoraVo> {
    public void saveOrUpdateObj(FrontCoraVo vo) {

        QueryWrapper<FrontCoraVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
