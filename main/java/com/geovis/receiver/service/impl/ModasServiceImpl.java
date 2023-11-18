package com.geovis.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geovis.receiver.dao.ModasDao;
import com.geovis.receiver.pojo.vo.InwGjdNwpVo;
import com.geovis.receiver.pojo.vo.ModasVo;
import org.springframework.stereotype.Service;

@Service
public class ModasServiceImpl extends ServiceImpl<ModasDao, ModasVo> {
    public void saveOrUpdateObj(ModasVo vo) {

        QueryWrapper<ModasVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("png_file_name", vo.getPngFileName());
        remove(queryWrapper);
        saveOrUpdate(vo);
    }
}
