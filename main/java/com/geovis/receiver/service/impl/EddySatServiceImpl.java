package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.EddySatDao;
import com.geovis.receiver.pojo.vo.EddyNwpVo;
import com.geovis.receiver.pojo.vo.EddySatVo;
import com.geovis.receiver.pojo.vo.EddyStatVo;
import org.springframework.stereotype.Service;

@Service
public class EddySatServiceImpl extends ServiceImpl<EddySatDao, EddySatVo> {

    public void saveOrUpdateObj(EddySatVo vo) {

        QueryWrapper<EddySatVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_name", vo.getFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
