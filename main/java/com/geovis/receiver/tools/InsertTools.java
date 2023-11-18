package com.geovis.receiver.tools;

import com.geovis.receiver.dao.SatelliteManagerDao;
import com.geovis.receiver.pojo.model.Produce;
import com.geovis.receiver.pojo.model.Satellite_Hj1289;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
public class InsertTools {

    @Autowired
    private ReadConfig readConfig;

    @Autowired
    private CreateSQLTools createSQLTools;

    @Autowired
    private SatelliteManagerDao satelliteManagerDao;
    /**
     * 卫星数据入库
     *
     * @param pro
     * @return
     */
    public boolean insertSat_hj1289(Produce pro, String lv){
        StringBuilder errorLogBuilder = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        Satellite_Hj1289 sat = (Satellite_Hj1289)pro.getProductList().get(0);

        String attribute = "";
        String format = "";
        String value = "";
        String tableName = readConfig.readProp(lv,"tableName");
        if(lv.equalsIgnoreCase("L0")){
            attribute = sat.toStringAttribute_L0();
            format = sat.toStringFormat_L0();
            value = sat.toStringValue_L0();
        }else if(lv.equalsIgnoreCase("L1")){
            attribute = sat.toStringAttribute_L1();
            format = sat.toStringFormat_L1();
            value = sat.toStringValue_L1();
        }else if(lv.equalsIgnoreCase("PGS")){
            attribute = sat.toStringAttribute_PGS();
            format = sat.toStringFormat_PGS();
            value = sat.toStringValue_PGS();
        }else if(lv.equalsIgnoreCase("MON")){
            attribute = sat.toStringAttribute_MON();
            format = sat.toStringFormat_MON();
            value = sat.toStringValue_MON();
        }else if(lv.equalsIgnoreCase("IMG")){
            attribute = sat.toStringAttribute_IMG();
            format = sat.toStringFormat_IMG();
            value = sat.toStringValue_IMG();
        }else if(lv.equalsIgnoreCase("FUSE")){
            attribute = sat.toStringAttribute_FUSE();
            format = sat.toStringFormat_FUSE();
            value = sat.toStringValue_FUSE();
        }else{
            return true;
        }

        String sql = createSQLTools.sqlJoint(tableName,attribute, format,value,sb);
        String delSql = "delete from "+tableName+" where dataname='"+sat.getDataname()+"'";
        boolean flag = false;
        synchronized (InsertTools.class) {
            satelliteManagerDao.deleteSatellite(delSql);
            flag = satelliteManagerDao.insertSatellite(sql);
        }
        if(flag){
            return true;
        }else {
            pro.addError(errorLogBuilder.toString());
            return false;
        }
    }
}
