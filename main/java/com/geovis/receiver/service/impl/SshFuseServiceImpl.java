package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.SshSatFuseDao;
import com.geovis.receiver.pojo.vo.SshFuseVo;
import org.springframework.stereotype.Service;

@Service
public class SshFuseServiceImpl extends ServiceImpl<SshSatFuseDao, SshFuseVo> {
    public void saveOrUpdateObj(SshFuseVo vo) {

        QueryWrapper<SshFuseVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
