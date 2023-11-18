package com.geovis.receiver.pojo.model;


import lombok.Data;

@Data
public class Satellite_Hj1289 extends Product {

    private String satelliteid;
    private String sensorid;
    private String orbitid;
    private String datastarttime;
    private String dateendtime;
    private String filepath;
    private String dataname;
    private String thumppng;
    private String lefttoplon;
    private String lefttoplat;
    private String rightbuttonlon;
    private String rightbuttonlat;
    private String centerlon;
    private String centerlat;
    private String heightangle;
    private String zenithangle;
    private String lt_latitude;
    private String lb_longitude;
    private String lb_latitude;
    private String lt_longitude;
    private String thumb_pic;
    private String shootstarttime;
    private String shootendtime;
    private String createtime;
    private String productid;

    private String shoottime;
    private String projection;
    private String unitid;
    private String resolution;
    private String filesize;

    private String srcpath;


    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }


    public String getShoottime() {
        return shoottime;
    }

    public void setShoottime(String shoottime) {
        this.shoottime = shoottime;
    }

    public String getProjection() {
        return projection;
    }

    public void setProjection(String projection) {
        this.projection = projection;
    }

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getShootstarttime() {
        return shootstarttime;
    }

    public void setShootstarttime(String shootstarttime) {
        this.shootstarttime = shootstarttime;
    }

    public String getShootendtime() {
        return shootendtime;
    }

    public void setShootendtime(String shootendtime) {
        this.shootendtime = shootendtime;
    }

    public String getLt_latitude() {
        return lt_latitude;
    }

    public void setLt_latitude(String ltLatitude) {
        lt_latitude = ltLatitude;
    }

    public String getLb_longitude() {
        return lb_longitude;
    }

    public void setLb_longitude(String lbLongitude) {
        lb_longitude = lbLongitude;
    }

    public String getLb_latitude() {
        return lb_latitude;
    }

    public void setLb_latitude(String lbLatitude) {
        lb_latitude = lbLatitude;
    }

    public String getLt_longitude() {
        return lt_longitude;
    }

    public void setLt_longitude(String ltLongitude) {
        lt_longitude = ltLongitude;
    }

    public String getThumb_pic() {
        return thumb_pic;
    }

    public void setThumb_pic(String thumbPic) {
        thumb_pic = thumbPic;
    }

    public String getSatelliteid() {
        return satelliteid;
    }

    public void setSatelliteid(String satelliteid) {
        this.satelliteid = satelliteid;
    }

    public String getSensorid() {
        return sensorid;
    }

    public void setSensorid(String sensorid) {
        this.sensorid = sensorid;
    }

    public String getOrbitid() {
        return orbitid;
    }

    public void setOrbitid(String orbitid) {
        this.orbitid = orbitid;
    }

    public String getDatastarttime() {
        return datastarttime;
    }

    public void setDatastarttime(String datastarttime) {
        this.datastarttime = datastarttime;
    }

    public String getDateendtime() {
        return dateendtime;
    }

    public void setDateendtime(String dateendtime) {
        this.dateendtime = dateendtime;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getDataname() {
        return dataname;
    }

    public void setDataname(String dataname) {
        this.dataname = dataname;
    }

    public String getThumppng() {
        return thumppng;
    }

    public void setThumppng(String thumppng) {
        this.thumppng = thumppng;
    }

    public String getLefttoplon() {
        return lefttoplon;
    }

    public void setLefttoplon(String lefttoplon) {
        this.lefttoplon = lefttoplon;
    }

    public String getLefttoplat() {
        return lefttoplat;
    }

    public void setLefttoplat(String lefttoplat) {
        this.lefttoplat = lefttoplat;
    }

    public String getRightbuttonlon() {
        return rightbuttonlon;
    }

    public void setRightbuttonlon(String rightbuttonlon) {
        this.rightbuttonlon = rightbuttonlon;
    }

    public String getRightbuttonlat() {
        return rightbuttonlat;
    }

    public void setRightbuttonlat(String rightbuttonlat) {
        this.rightbuttonlat = rightbuttonlat;
    }

    public String getCenterlon() {
        return centerlon;
    }

    public void setCenterlon(String centerlon) {
        this.centerlon = centerlon;
    }

    public String getCenterlat() {
        return centerlat;
    }

    public void setCenterlat(String centerlat) {
        this.centerlat = centerlat;
    }

    public String getHeightangle() {
        return heightangle;
    }

    public void setHeightangle(String heightangle) {
        this.heightangle = heightangle;
    }

    public String getZenithangle() {
        return zenithangle;
    }

    public void setZenithangle(String zenithangle) {
        this.zenithangle = zenithangle;
    }

    @Override
    public String toStringAttribute() {
        return null;
    }

    @Override
    public String toStringFormat() {
        return null;
    }

    @Override
    public String toStringValue() {
        return null;
    }

    public String toStringAttribute_L1() {
        return "satelliteid,sensorid,orbitid,datastarttime,dataendtime,filepath,"+
                "dataname,thumppng,lefttoplon,lefttoplat,rightbuttonlon,rightbuttonlat,"+
                "centerlon,centerlat,heightangle,zenithangle,filesize";
    }

    public String toStringFormat_L1() {
        return "varchar,varchar,varchar,timestamp,timestamp,varchar,"+
                "varchar,varchar,number,number,number,number,"+
                "number,number,number,number,number";
    }

    public String toStringValue_L1() {
        return satelliteid+","+sensorid+","+orbitid+","+datastarttime+","+
                dateendtime+","+filepath+","+dataname+","+thumppng+","+lefttoplon+","+
                lefttoplat+","+rightbuttonlon+","+rightbuttonlat+","+centerlon+","+
                centerlat+","+heightangle+","+zenithangle+","+filesize;
    }

    public String toStringAttribute_L0() {
        return "satelliteid,sensorid,orbitid,datastarttime,dataendtime,filepath,"+
                "dataname,thumb_pic,lt_longitude,lt_latitude,lb_longitude,lb_latitude,"+
                "centerlon,centerlat,heightangle,zenithangle,filesize";
    }

    public String toStringFormat_L0() {
        return "varchar,varchar,varchar,timestamp,timestamp,varchar,"+
                "varchar,varchar,number,number,number,number,"+
                "number,number,number,number,number";
    }

    public String toStringValue_L0() {
        return satelliteid+","+sensorid+","+orbitid+","+datastarttime+","+
                dateendtime+","+filepath+","+dataname+","+thumb_pic+","+lt_longitude+","+
                lt_latitude+","+lb_longitude+","+lb_latitude+","+centerlon+","+
                centerlat+","+heightangle+","+zenithangle+","+filesize;
    }

    public String toStringAttribute_PGS() {
        return "satelliteid,sensorid,orbitid,shootstarttime,shootendtime,filepath,"+
                "dataname,thumppng,lefttoplon,lefttoplat,rightbuttonlon,rightbuttonlat,"+
                "centerlon,centerlat,heightangle,zenithangle,createtime," +
                "productid,filesize";
    }

    public String toStringFormat_PGS() {
        return "varchar,varchar,varchar,timestamp,timestamp,varchar,"+
                "varchar,varchar,number,number,number,number,"+
                "number,number,number,number,timestamp,varchar,number";
    }

    public String toStringValue_PGS() {
        return satelliteid+","+sensorid+","+orbitid+","+shootstarttime+","+
                shootendtime+","+filepath+","+dataname+","+thumppng+","+lefttoplon+","+
                lefttoplat+","+rightbuttonlon+","+rightbuttonlat+","+centerlon+","+
                centerlat+","+heightangle+","+zenithangle+","+createtime+","+
                productid+","+filesize;
    }

    public String toStringAttribute_MON() {
        return "satelliteid,sensorid,orbitid,shootstarttime,shootendtime,filepath,"+
                "dataname,thumppng,lefttoplon,lefttoplat,rightbuttonlon,rightbuttonlat,"+
                "centerlon,centerlat,heightangle,zenithangle,createtime," +
                "productid,filesize,srcpath";
    }

    public String toStringFormat_MON() {
        return "varchar,varchar,varchar,timestamp,timestamp,varchar,"+
                "varchar,varchar,number,number,number,number,"+
                "number,number,number,number,timestamp,varchar,number,varchar";
    }

    public String toStringValue_MON() {
        return satelliteid+","+sensorid+","+orbitid+","+shootstarttime+","+
                shootendtime+","+filepath+","+dataname+","+thumppng+","+lefttoplon+","+
                lefttoplat+","+rightbuttonlon+","+rightbuttonlat+","+centerlon+","+
                centerlat+","+heightangle+","+zenithangle+","+createtime+","+
                productid+","+filesize+","+srcpath;
    }

    public String toStringAttribute_IMG() {
        return "satelliteid,sensorid,orbitid,shootstarttime,shootendtime,filepath,"+
                "dataname,thumppng,lefttoplon,lefttoplat,rightbuttonlon,rightbuttonlat,"+
                "centerlon,centerlat,heightangle,zenithangle,createtime," +
                "productid,filesize";
    }

    public String toStringFormat_IMG() {
        return "varchar,varchar,varchar,timestamp,timestamp,varchar,"+
                "varchar,varchar,number,number,number,number,"+
                "number,number,number,number,timestamp,varchar,number";
    }

    public String toStringValue_IMG() {
        return satelliteid+","+sensorid+","+orbitid+","+shootstarttime+","+
                shootendtime+","+filepath+","+dataname+","+thumppng+","+lefttoplon+","+
                lefttoplat+","+rightbuttonlon+","+rightbuttonlat+","+centerlon+","+
                centerlat+","+heightangle+","+zenithangle+","+createtime+","+
                productid+","+filesize;
    }

    public String toStringAttribute_FUSE() {
        return "filepath,dataname,shoottime,lefttoplon,lefttoplat," +
                "rightbuttonlon,rightbuttonlat,centerlon,centerlat," +
                "createtime,projection,unitid,resolution,thumppng,productid,filesize";
    }

    public String toStringFormat_FUSE() {
        return "varchar,varchar,timestamp,number,number," +
                "number,number,number,number,timestamp,varchar," +
                "varchar,varchar,varchar,varchar,number";
    }

    public String toStringValue_FUSE() {
        return filepath+","+dataname+","+shoottime+","+lefttoplon+","+lefttoplat+","+
                rightbuttonlon+","+rightbuttonlat+","+centerlon+","+centerlat+","+
                createtime+","+projection+","+unitid+","+resolution+","+thumppng+","+productid+","+filesize;
    }

}
