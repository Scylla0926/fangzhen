package com.geovis.receiver.readData.cz;

import com.geovis.receiver.pojo.model.NcBeanPg;
import com.geovis.receiver.tools.*;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 处理UV风
 *
 * @category
 */
public class WriteFileJobUV implements Runnable {
    private String outFilePath;
    private CopyOnWriteArrayList<String> outPathes;
    private GribToPngUtils png;
    private NcBeanPg pg = new NcBeanPg();
    private CountDownLatch latch;
    private String prefixFileName;
    private float[] ps;
    private Map<String, Object> datasMap;
    private String elName;
    private String elName1;
    private String element;
    private String layerName;
    private String timeName = null;
    private int timeSize = -1;
    private int i;
    private String outPath;
    private AtomicInteger total;
    private static Map<String, Map<String, Object>> datasMapUV = new ConcurrentHashMap<String, Map<String, Object>>();
    private static DecimalFormat df = new DecimalFormat("0.00");

    //Map<String, Object> datasMap, float[] ps, String prefixFileName, String elName, String element, String layerName, String outPath, int i, String outFilePath, CopyOnWriteArrayList<String> outPathes, GribToPngUtils png, NcBeanPg pg, CountDownLatch latch
    public WriteFileJobUV(Map<String, Object> datasMap, float[] ps, String prefixFileName, String elName, String elName1, String element, String layerName, String outPath, int i, String outFilePath, CopyOnWriteArrayList<String> outPathes, GribToPngUtils png, NcBeanPg pg, CountDownLatch latch, String timeName, int timeSize) {
        this.datasMap = datasMap;
        this.ps = ps;
        this.prefixFileName = prefixFileName;
        this.elName = elName;
        this.elName1 = elName1;
        this.element = element;
        this.outPath = outPath;
        this.layerName = layerName;
        this.i = i;
        this.outFilePath = outFilePath;
        this.outPathes = outPathes;
        this.png = png;
        copy(pg, this.pg);
        this.latch = latch;
        this.timeName = timeName;
        this.timeSize = timeSize;
    }

    public WriteFileJobUV(Map<String, Object> datasMap, float[] ps, String prefixFileName, String elName, String elName1, String element, String layerName, String outPath, int i, String outFilePath, CopyOnWriteArrayList<String> outPathes, GribToPngUtils png, NcBeanPg pg, CountDownLatch latch) {
        this.datasMap = datasMap;
        this.ps = ps;
        this.prefixFileName = prefixFileName;
        this.elName = elName;
        this.elName1 = elName1;
        this.element = element;
        this.outPath = outPath;
        this.layerName = layerName;
        this.i = i;
        this.outFilePath = outFilePath;
        this.outPathes = outPathes;
        this.png = png;
        copy(pg, this.pg);
        this.latch = latch;
    }

    private void copy(NcBeanPg from, NcBeanPg to) {
        TransObjUtil.transObj(from, to);
    }

    @Override
    public void run() {
        Thread.currentThread().setName(prefixFileName);
        double[][] datas = null;
        double[][] datas2 = null;
        String uUnit = null;
        String vUnit = null;

        if (timeName != null && timeSize != -1) {

            if (prefixFileName.toLowerCase().startsWith("uwnd")) {
                String filePath = pg.filePath.replace("uwnd", "vwnd");
                File file = new File(filePath);
                if (file.exists()) {
                    if (datasMapUV.get(filePath) == null) {
                        datasMapUV.put(filePath, NcReaderUtils.getDatasMap(filePath));
                    }
                    datas = NcReaderUtils.readByNameLayer(datasMap, elName, layerName);
                    datas2 = NcReaderUtils.readByNameLayer(datasMapUV.get(filePath), elName1, layerName);
                    uUnit = NcReaderUtils.getElementUnit(datasMap, elName);
                    vUnit = NcReaderUtils.getElementUnit(datasMapUV.get(filePath), elName1);

                } else {
                    return;
                }
            } else if (prefixFileName.toLowerCase().startsWith("etut")) {
                String filePath = pg.filePath.replace("ETUT", "ETVT");
                File file = new File(filePath);
                if (file.exists()) {

                    if (datasMapUV.get(filePath) == null) {
                        datasMapUV.put(filePath, NcReaderUtils.getDatasMap(filePath));
                    }
                    datas = NcReaderUtils.readByNameLayer(datasMap, elName, layerName);
                    datas2 = NcReaderUtils.readByNameLayer(datasMapUV.get(filePath), elName1, layerName);
                    uUnit = NcReaderUtils.getElementUnit(datasMap, elName);
                    vUnit = NcReaderUtils.getElementUnit(datasMapUV.get(filePath), elName1);

                } else {
                    latch.countDown();
                    return;
                }
            } else if (prefixFileName.toLowerCase().startsWith("etvv") || prefixFileName.toLowerCase().startsWith("etvt")) {
                latch.countDown();
                return;
            } else {
                datas = NcReaderUtils.readByNameLayer(datasMap, elName, layerName,timeSize);
                datas2 = NcReaderUtils.readByNameLayer(datasMap, elName1, layerName,timeSize);

                uUnit = NcReaderUtils.getElementUnit(datasMap, elName);
                vUnit = NcReaderUtils.getElementUnit(datasMap, elName1);
            }

        } else {
            if (prefixFileName.toLowerCase().startsWith("uwnd")) {
                String filePath = pg.filePath.replace("uwnd", "vwnd");
                File file = new File(filePath);
                if (file.exists()) {
                    if (datasMapUV.get(filePath) == null) {
                        datasMapUV.put(filePath, NcReaderUtils.getDatasMap(filePath));
                    }
                    datas = NcReaderUtils.readByNameLayer(datasMap, elName, layerName);
                    datas2 = NcReaderUtils.readByNameLayer(datasMapUV.get(filePath), elName1, layerName);
                    uUnit = NcReaderUtils.getElementUnit(datasMap, elName);
                    vUnit = NcReaderUtils.getElementUnit(datasMapUV.get(filePath), elName1);

                } else {
                    return;
                }
            } else if (prefixFileName.toLowerCase().startsWith("etut")) {
                String filePath = pg.filePath.replace("ETUT", "ETVT");
                File file = new File(filePath);
                if (file.exists()) {

                    if (datasMapUV.get(filePath) == null) {
                        datasMapUV.put(filePath, NcReaderUtils.getDatasMap(filePath));
                    }
                    datas = NcReaderUtils.readByNameLayer(datasMap, elName, layerName);
                    datas2 = NcReaderUtils.readByNameLayer(datasMapUV.get(filePath), elName1, layerName);
                    uUnit = NcReaderUtils.getElementUnit(datasMap, elName);
                    vUnit = NcReaderUtils.getElementUnit(datasMapUV.get(filePath), elName1);

                } else {
                    latch.countDown();
                    return;
                }
            } else if (prefixFileName.toLowerCase().startsWith("etvv") || prefixFileName.toLowerCase().startsWith("etvt")) {
                latch.countDown();
                return;
            } else {
                datas = NcReaderUtils.readByNameLayer(datasMap, elName, layerName);
                datas2 = NcReaderUtils.readByNameLayer(datasMap, elName1, layerName);

                uUnit = NcReaderUtils.getElementUnit(datasMap, elName);
                vUnit = NcReaderUtils.getElementUnit(datasMap, elName1);
            }

        }


        String prefix = "";

        double[] interval = NcReaderUtils.getGridInterval(datasMap, prefix + NcReaderUtils.lon, prefix + NcReaderUtils.lat, prefixFileName);

        if (interval[1] < 0) {
            double[][] datas11 = new double[datas.length][];
            for (int i = 0, count = datas.length; i < count; i++) {
                datas11[i] = datas[count - 1 - i];
            }
            datas = datas11;

            double[][] datas22 = new double[datas2.length][];
            for (int i = 0, count = datas2.length; i < count; i++) {
                datas22[i] = datas2[count - 1 - i];
            }
            datas2 = datas22;
        }
//		System.out.println("读取数据耗时: " + (System.currentTimeMillis() - time));

        String elementUnit = "u=" + uUnit + ";" + "v=" + vUnit;
        String elNames = String.valueOf(datasMap.get(elName));
        // lon 比lat 更外层的时候 将 数组的行和列反转
        if (elNames.indexOf("lon") < elNames.indexOf("lat")) {
            datas = ArrayReverseUtils.toArrayReversal(datas);
            datas2 = ArrayReverseUtils.toArrayReversal(datas2);
        }
        double[] values = trans(datas);
        double[] values2 = trans(datas2);


        if (prefixFileName.contains(".wave.nc")) {
            layerName = (int) ((ps[i] - ps[0]) * 24) + "";
        }

        //double[] lonWestLatSouth = NcReaderUtils.getLonWestLatSouth(datasMap, prefix + NcReaderUtils.lon, prefix + NcReaderUtils.lat);

//        element = element.replaceAll("u_|v_", "uv_");
//        element = element.replaceAll("u-|v-", "uv-");

        String unit = null;
        if (pg.pressLayerUnit.toLowerCase().contains("pa") || pg.pressLayerUnit.toLowerCase().endsWith("m")) {

            unit = pg.pressLayerUnit;
        } else {
            unit = "";
        }

        //outFilePath = outPath + prefixFileName.split("\\.")[0].substring(0, prefixFileName.split("\\.")[0].length()) + "_" + layerName + "_" + ((element.equals("u") || element.equals("u10")) ? "uv" : element);
//		System.out.println("数据准备耗时: " + (System.currentTimeMillis() - time));

        //FileUtil.writeBytesToFile(outFilePath + ".bin", datas, datas2);
        //outPathes.add(outFilePath + ".bin");
        //String enviTxt = EnviTxtUtil.getEnviTxt(datas[0].length, datas.length, 4, lonWestLatSouth[0], lonWestLatSouth[1], interval[0], interval[1], elementUnit);
        // FileUtil.writeStrToFile(enviTxt, outFilePath + ".hdr");
        //outPathes.add(outFilePath + ".hdr");


//        if (timeName != null && timeSize != -1){
//            outFilePath = outPath + prefixFileName.split("\\.")[0].substring(0, prefixFileName.split("\\.")[0].length()) + "_" + timeName + "_" + layerName + "_" + element;
//        }else {
//            outFilePath = outPath + prefixFileName.split("\\.")[0].substring(0, prefixFileName.split("\\.")[0].length()) + "_" + layerName + "_" + element;
//        }

        png.png(outFilePath + ".png", values, values2, pg.lonnum, pg.latnum, pg.lons, pg.lats, pg.lone, pg.late, pg.lonstep, pg.latstep, elementUnit);

        outPathes.add(outFilePath + ".png");
        latch.countDown();
    }

    public double[] trans(double[][] datas) {
        int index = CalculateIndexUtils.getIndex(pg.lons, pg.lonstep);
        double[] values = ArrayReverseUtils.trans(datas);
        if (!prefixFileName.contains(".wave.nc")) {

            if (pg.lons == 0 && pg.lons == 360) {
                values = ArrayReverseUtils.trans_reverse(datas, index);
            } else if (pg.lons >= 180) {
                values = ArrayReverseUtils.trans(datas);
                pg.lons = (pg.lons - 360);
                pg.lone = (pg.lone - 360);
            } else if (pg.lons < 180 && pg.lone > 179.88) {
                values = ArrayReverseUtils.trans_reverse(datas, index);
                pg.lons = (pg.lons - 180);
                pg.lone = (pg.lone - 180);
            }
        }
        return values;
    }

    public double[] trans2(double[][] datas) {
        double[] values = ArrayReverseUtils.transToOneArray(datas);
        String[] lonlats = NcReaderUtils.getLonLatName(datasMap);
        double[] interval = NcReaderUtils.getGridInterval(datasMap, lonlats[0], lonlats[1], prefixFileName);

        int index = CalculateIndexUtils.getIndex(pg.lons, pg.lonstep);
        if (pg.lons == 0 && (pg.lone == 360 || pg.lone == Math.abs(360 - interval[0]))) {
            values = ArrayReverseUtils.transReverse(datas, index);
            pg.lons = (pg.lons - 180);
            pg.lone = (pg.lone - 180);
        } else if (pg.lons >= 180) {
            values = ArrayReverseUtils.transToOneArray(datas);
            pg.lons = (pg.lons - 360);
            pg.lone = (pg.lone - 360);
        } else if (pg.lons < 180 && pg.lone > 180) {
            pg.lone = (pg.lone - 360);
            values = ArrayReverseUtils.trans_reverse(datas, index);

            if (prefixFileName.contains(".wave.nc")) {
                layerName = (int) ((ps[i] - ps[0]) * 24) + "";
            }

        }
        return values;
    }
}
