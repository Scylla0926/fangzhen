package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.FrontModasDao;
import com.geovis.receiver.pojo.vo.FrontModasVo;
import com.geovis.receiver.pojo.vo.FrontNwpVo;
import org.springframework.stereotype.Service;

@Service
public class FrontModasServiceImpl extends ServiceImpl<FrontModasDao, FrontModasVo> {
    public void saveOrUpdateObj(FrontModasVo vo) {

        QueryWrapper<FrontModasVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
