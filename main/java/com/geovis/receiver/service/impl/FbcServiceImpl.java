package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.FbcDao;
import com.geovis.receiver.pojo.vo.FbcVo;
import org.springframework.stereotype.Service;

@Service
public class FbcServiceImpl extends ServiceImpl<FbcDao, FbcVo> {
    public void saveOrUpdateObj(FbcVo vo) {

        QueryWrapper<FbcVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
