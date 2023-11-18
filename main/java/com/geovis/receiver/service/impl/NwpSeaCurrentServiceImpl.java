package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.NwpSeaCurrentDao;
import com.geovis.receiver.pojo.vo.CurrentNwpVo;
import org.springframework.stereotype.Service;

@Service
public class NwpSeaCurrentServiceImpl extends ServiceImpl<NwpSeaCurrentDao, CurrentNwpVo> {
    public void saveOrUpdateObj(CurrentNwpVo vo) {

        QueryWrapper<CurrentNwpVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
