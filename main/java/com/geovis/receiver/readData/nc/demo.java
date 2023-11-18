package com.geovis.receiver.readData.nc;

import com.geovis.receiver.pojo.model.NcBeanPg;
import com.geovis.receiver.tools.GribToPngUtils;
import com.geovis.receiver.tools.NcReaderUtils;
import com.geovis.receiver.tools.TimeUtil;
import ucar.ma2.Array;
import ucar.nc2.Attribute;
import ucar.nc2.Variable;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 水深数据 解析 demo
 */
public class demo {

    public static void main(String[] args) {

        String ncPath = args[0];
//        String ncPath = "D:/neiwangtong/B3_内网中转机2/ETOPO_2022_v1_30s_N90W180_surface.nc";
        String outPath = args[1];
//        String outPath = "C:\\Users\\Administrator\\Desktop\\nc";

        //获取数据结构体
        Map<String, Object> datasMap = NcReaderUtils.getDatasMap(ncPath);

        Variable v = (Variable) datasMap.get("z");
        if (v == null) {
            System.out.println("2");
        }
        List<Attribute> attributes = v.getAttributes();
        double missValue = (double) -9999;
        Array array = null;
        try {
            array = v.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Object objArray = array.copyToNDJavaArray();
        Class<? extends Object> arrayClass = objArray.getClass();

        float[][] datas = null;
        datas = ((float[][]) objArray);

        double[] values = new double[datas.length * datas[0].length];
        for (int i = 0; i < datas.length; i++) {
            for (int j = 0, num = datas[i].length; j < num; j++) {
                values[i * num + j] = datas[i][j];
            }
        }
//        double[][] datas = NcReaderUtils.readByNameLayer(datasMap, "z", "0");

//        double[] values = ArrayReverseUtils.transToOneArray(result);
        System.out.println(1);

        // 解析所需信息类
        NcBeanPg pg = new NcBeanPg();
        pg.filePath = ncPath;

        // 组织解析所用的信息
        getPg(pg, new File(ncPath).getName(), ".nc", "0", "z", datasMap, "m");

        GribToPngUtils png = new GribToPngUtils();

        png.png(outPath + File.separator + "z.png", values, pg.lonnum, pg.latnum, pg.lons, pg.lats, pg.lone, pg.late, pg.lonstep, pg.latstep, "m");

    }

    /**
     * 组织返回值的 参数列表
     *
     * @param pg
     * @param fileName
     * @param suffix
     * @param layerName
     * @param elName
     * @param datasMap
     * @param layerUnit
     */
    private static void getPg(NcBeanPg pg, String fileName, String suffix, String layerName, String elName, Map<String, Object> datasMap, String layerUnit) {
        //是否为三维温盐流
//        if(fileName.contains("3dts") && "nc".equalsIgnoreCase(suffix)){
        get3Dts(pg, fileName, layerName, elName, datasMap, layerUnit);
//        }
//        else if(){
//
//        }
//        else{
//            // TODO: 2022/8/24 预留代码块
//        }
    }

    private static void get3Dts(NcBeanPg pg, String fileName, String layerName, String elName, Map<String, Object> datasMap, String layerUnit) {
        pg.vti = (int) Float.parseFloat(layerName);
        createPg(pg, fileName, layerName, elName, datasMap, layerUnit);
        if (pg.type == null) {
            pg.type = elName;
        }
    }

    private static void createPg(NcBeanPg pg, String fileName, String layerName, String elName, Map<String, Object> datasMap, String layerUnit) {
        pg.pressLayerUnit = layerUnit;
        pg.fileName = new File(pg.filePath).getName();
        File file = new File(pg.filePath);
        pg.fileSize = file.length();
        //String timeStr = datasMap.get("time").toString();
        String time = "time";
        if (elName.startsWith("xxx")) {
            time = elName.substring(0, 4) + time;
        }
        if (pg.fileDate == null) {
            pg.fileDate = TimeUtil.date2String(new Date(file.lastModified()), "yyyyMMddHHmmss");
            pg.fileDate = pg.fileDate.substring(0, 8) + (pg.fcTime < 10 ? "0" + pg.fcTime : pg.fcTime) + "0000";
        }
        String prefix = "";
        String[] lonlats = NcReaderUtils.getLonLatName(datasMap);
        float[] lons = getArrayDatas(datasMap, prefix + lonlats[0]);
        float[] lats = getArrayDatas(datasMap, prefix + lonlats[1]);
        pg.late = Double.parseDouble(String.format("%.4f", lats[lats.length - 1]));
        pg.latnum = lats.length;
        pg.lats = Double.parseDouble(String.format("%.4f", lats[0]));
//        pg.latstep = Math.abs(lats[0] - lats[1]);
        pg.latstep = lats[1] - lats[0];

        double tem = 0;
        if (pg.lats > pg.late) {
            tem = pg.lats;
            pg.lats = pg.late;
            pg.late = tem;
        }

        pg.lone = Double.parseDouble(String.format("%.4f", lons[lons.length - 1]));
        pg.lonnum = lons.length;
        pg.lons = Double.parseDouble(String.format("%.4f", lons[0]));
//        pg.lonstep = Math.abs(lons[0] - lons[1]);
        pg.lonstep = lons[1] - lons[0];


    }

    private static float[] getArrayDatas(Map<String, Object> datasMap, String elem) {
        Object array = NcReaderUtils.getArray(datasMap, elem).copyToNDJavaArray();
        float[] result = null;
        if (array.getClass() == double[].class) {
            double[] datas = (double[]) array;
            result = new float[datas.length];
            for (int i = 0, count = datas.length; i < count; i++) {
                result[i] = (float) datas[i];
            }
        } else {
            Object obj = null;
            Array array2 = NcReaderUtils.getArray(datasMap, elem);
            int rank = array2.getRank();
            if (elem.equals("lat")) {
                elem = "xc";
            }
            if (elem.equals("lon")) {
                elem = "yc";
            }
            if (rank == 2) {
                array2 = NcReaderUtils.getArray(datasMap, elem);
            }
            obj = array2.copyTo1DJavaArray();
            if (obj.getClass() == double[].class) {
                double[] datas = (double[]) obj;
                result = new float[datas.length];
                for (int i = 0, count = datas.length; i < count; i++) {
                    result[i] = (float) datas[i];
                }
            } else {
                result = (float[]) obj;
            }
        }

        return result;
    }
}
