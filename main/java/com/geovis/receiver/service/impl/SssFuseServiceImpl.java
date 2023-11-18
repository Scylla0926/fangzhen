package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.SshSatFuseDao;
import com.geovis.receiver.dao.SssSatFuseDao;
import com.geovis.receiver.pojo.vo.SshFuseVo;
import com.geovis.receiver.pojo.vo.SssFuseVo;
import org.springframework.stereotype.Service;

@Service
public class SssFuseServiceImpl extends ServiceImpl<SssSatFuseDao, SssFuseVo> {
    public void saveOrUpdateObj(SssFuseVo vo) {

        QueryWrapper<SssFuseVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
