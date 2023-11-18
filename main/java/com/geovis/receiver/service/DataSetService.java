package com.geovis.receiver.service;

import com.geovis.receiver.pojo.bean.ProductTableBean;

public interface DataSetService {
    public void updateRedisDataSource(String dataSourceName);
    public ProductTableBean getTableBean(String eleType,String timeType);
}
