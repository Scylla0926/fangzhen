package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.CoraCurrentDao;
import com.geovis.receiver.dao.InwPngDao;
import com.geovis.receiver.pojo.vo.CurrentCoraVo;
import com.geovis.receiver.pojo.vo.InwPngVo;
import org.springframework.stereotype.Service;

@Service
public class InwPngServiceImpl extends ServiceImpl<InwPngDao, InwPngVo> {
    public void saveOrUpdateObj(InwPngVo vo) {

        QueryWrapper<InwPngVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }

}
