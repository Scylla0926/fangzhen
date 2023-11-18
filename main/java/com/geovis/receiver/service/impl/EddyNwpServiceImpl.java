package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.EddyNwpDao;
import com.geovis.receiver.pojo.vo.EddyNwpVo;
import org.springframework.stereotype.Service;

@Service
public class EddyNwpServiceImpl extends ServiceImpl<EddyNwpDao, EddyNwpVo> {
    public void saveOrUpdateObj(EddyNwpVo vo) {
        QueryWrapper<EddyNwpVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_name", vo.getFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
