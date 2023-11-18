package com.geovis.receiver.pojo.model;


import com.geovis.receiver.pojo.bean.BaseBean;
import lombok.Data;

/**
 * 表结构实体类
 */
@Data
public class NcBeanPg extends BaseBean {
    /**
     * nameCN: 文件名称<br>
     */
    public String fileName;
    
    /**
     * nameCN: 文件大小<br>
     */
    public long fileSize;
    
    /**
     * nameCN: 文件日期<br>
     */
    public String fileDate;
    
    /**
     * nameCN: 预报中心<br>
     */
    public String cccc;
    
    /**
     * nameCN: 预报模式<br>
     */
    public String fcmode;
    
    /**
     * nameCN: 要素代号<br>
     */
    public String type;
    
    /**
     * nameCN: 预报时效<br>
     */
    public int vti;
    
    /**
     * nameCN: 预报时效<br>
     */
    public String vtis;
    
    /**
     * nameCN: 气压层次<br>
     */
    public String press;
    
    /**
     * nameCN: 高度层次<br>
     */
    public String height;
    
    /**
     * nameCN: 层次<br>
     */
    public String layer;
    
    /**
     * nameCN: 预报时次<br>
     */
    public int fcTime;
    
    /**
     * nameCN: 纬度线上的格点数<br>
     */
    public int latnum;
    
    /**
     * nameCN: 经度线上的格点数<br>
     */
    public int lonnum;

    /**
     * nameCN: 纬度格距<br>
     */
    public double latstep;
    
    /**
     * nameCN: 经度格距<br>
     */
    public double lonstep;
    
    /**
     * nameCN: 起始纬度<br>
     */
    public double lats;
    
    /**
     * nameCN: 终止纬度<br>
     */
    public double late;
    
    /**
     * nameCN: 起始经度<br>
     */
    public double lons;
    
    /**
     * nameCN: 终止经度<br>
     */
    public double lone;
    
    /**
     * nameCN: 文件路径<br>
     */
    public String filePath;
    
    
    public boolean end;
    
    /**
     * nameCN: 气压层或层深的单位<br>
     */
    public String pressLayerUnit;
    
}
