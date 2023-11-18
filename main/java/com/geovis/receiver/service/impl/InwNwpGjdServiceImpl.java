package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.InwGjdNwpDao;
import com.geovis.receiver.pojo.vo.HjSzVo;
import com.geovis.receiver.pojo.vo.InwGjdNwpVo;
import org.springframework.stereotype.Service;

@Service
public class InwNwpGjdServiceImpl extends ServiceImpl<InwGjdNwpDao, InwGjdNwpVo> {
    public void saveOrUpdateObj(InwGjdNwpVo vo) {

        QueryWrapper<InwGjdNwpVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
