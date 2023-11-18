package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.YcCoraDao;
import com.geovis.receiver.dao.YcModasDao;
import com.geovis.receiver.pojo.vo.YcCoraVo;
import com.geovis.receiver.pojo.vo.YcModasVo;
import org.springframework.stereotype.Service;

@Service
public class YcModasServiceImpl extends ServiceImpl<YcModasDao, YcModasVo> {
    public void saveOrUpdateObj(YcModasVo vo) {

        QueryWrapper<YcModasVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
