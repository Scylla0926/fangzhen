package com.geovis.receiver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geovis.receiver.pojo.bean.DataSetTableBean;
import com.geovis.receiver.pojo.bean.ProductTableBean;
import com.geovis.receiver.pojo.vo.DataSetVo;
import com.geovis.receiver.pojo.vo.DataSourceVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author zyy
 * @version 1.0.0
 * @Package com.geovis.dataservice.dao
 * @ClassName ProductTableDao
 * @date 2023/8/20 18:18
 * @description TODO
 */
@Mapper
//@DS("tsfuse")
public interface ProductTableDao extends BaseMapper<ProductTableBean> {
    /**
     * 根据数据名称查找对应的表
     */
    List<ProductTableBean> selectTableByFilename(DataSourceVo dataSourceVo);

    /**
     * 根据数据名称查找对应的产品类型有哪些
     */
    List<DataSetTableBean> selectProductType(DataSetVo dataSetVo);
}
