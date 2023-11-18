package com.geovis.receiver.pojo.bean;

import lombok.Data;

@Data
/**
 *  FY4A 强降水雷暴 json 实体类
 */
public class SatTdscZzBean {
    private String tableName;
    private String cldNum_now;
    private String cldTime_now;
    private String cldNum_old;
    private String cldLon_now;
    private String cldLat_now;
    private String cldStatus_now;
    private String cldLon_old;
    private String cldLat_old;
    private String cg_dci;
    private String t_max;
    private String t_min;
    private String t_mean;
    private String g_max;
    private String insertDate;
}
