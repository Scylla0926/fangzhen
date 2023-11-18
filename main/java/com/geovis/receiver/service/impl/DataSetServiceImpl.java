package com.geovis.receiver.service.impl;

import com.geovis.receiver.dao.ProductTableDao;
import com.geovis.receiver.pojo.bean.DataSetTableBean;
import com.geovis.receiver.pojo.bean.ProductTableBean;
import com.geovis.receiver.pojo.vo.DataSetVo;
import com.geovis.receiver.pojo.vo.DataSourceVo;
import com.geovis.receiver.service.DataSetService;
import com.geovis.receiver.tools.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DataSetServiceImpl implements DataSetService {

    @Resource
    private ProductTableDao productTableDao;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 数据引接存库后，更新最新数据源为本次处理的数据类型
     * @param dataSourceName
     */
    @Override
    public void updateRedisDataSource(String dataSourceName){
        DataSetVo dataSetVo=new DataSetVo();
        dataSetVo.setDataSource(dataSourceName.toLowerCase());
        List<DataSetTableBean> dataSets=productTableDao.selectProductType(dataSetVo);
        if(dataSets!=null){
            for(int i=0;i<dataSets.size();i++){
                ////还需要再次过滤，有的数据类型关键字包含另一种数据类型
                DataSetTableBean setTableBean=dataSets.get(i);

                String[] dsArray=setTableBean.getDataSource().split(",");
                for(int j=0;j<dsArray.length;j++){
                    if(dsArray[j].equals(dataSourceName)){/////////此类数据源包含当前数据源
                        redisUtil.set(setTableBean.getProductType(),dataSourceName);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public ProductTableBean getTableBean(String eleType, String timeType) {
        DataSourceVo dataSourceVo=new DataSourceVo();
        dataSourceVo.setEleType(eleType.toLowerCase());
        dataSourceVo.setTimeType(timeType);
        List<ProductTableBean> tableBeanList = productTableDao.selectTableByFilename(dataSourceVo);
        if (tableBeanList.size() > 0)
            return tableBeanList.get(0);
        else return null;
    }
}
