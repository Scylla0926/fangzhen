package com.geovis.receiver.readData.caGLB;

import com.geovis.receiver.pojo.model.NcBeanPg;
import com.geovis.receiver.tools.*;
import ucar.ma2.DataType;
import ucar.nc2.Attribute;
import ucar.nc2.Variable;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
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

        String prefix = "";

        double[] interval = NcReaderUtils.getGridInterval(datasMap, prefix + NcReaderUtils.lon, prefix + NcReaderUtils.lat, prefixFileName);

//        if (interval[1] < 0) {
//            double[][] datas11 = new double[datas.length][];
//            for (int i = 0, count = datas.length; i < count; i++) {
//                datas11[i] = datas[count - 1 - i];
//            }
//            datas = datas11;
//
//            double[][] datas22 = new double[datas2.length][];
//            for (int i = 0, count = datas2.length; i < count; i++) {
//                datas22[i] = datas2[count - 1 - i];
//            }
//            datas2 = datas22;
//        }
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

        String unit = null;
        if (pg.pressLayerUnit.toLowerCase().contains("pa") || pg.pressLayerUnit.toLowerCase().endsWith("m")) {
            unit = pg.pressLayerUnit;
        } else {
            unit = "";
        }
        Variable u = (Variable) datasMap.get(elName);
        Variable v = (Variable) datasMap.get(elName1);
        List<Attribute> attributesU = u.getAttributes();
        List<Attribute> attributesV = v.getAttributes();

        double scale_factorU = (double) 1;
        double add_offsetU = (double) 0;
        double fileValueU = (double) -9999;
        double scale_factorV = (double) 1;
        double add_offsetV = (double) 0;
        double fileValueV = (double) -9999;

        for (Attribute att : attributesU) {
            if (att.getFullName().equals("scale_factor")) {
                if (att.getDataType() == DataType.SHORT) {
                    scale_factorU = (short) att.getNumericValue();
                } else if (att.getDataType() == DataType.FLOAT) {
                    scale_factorU = (float) att.getNumericValue();
                } else if (att.getDataType() == DataType.DOUBLE) {
                    scale_factorU = (double) att.getNumericValue();
                }
            }
            if (att.getFullName().equals("add_offset")) {
                if (att.getDataType() == DataType.SHORT) {
                    add_offsetU = (short) att.getNumericValue();
                } else if (att.getDataType() == DataType.FLOAT) {
                    add_offsetU = (float) att.getNumericValue();
                } else if (att.getDataType() == DataType.DOUBLE) {
                    add_offsetU = (double) att.getNumericValue();
                }
            }
            if (att.getFullName().equals("_FillValue")) {
                if (att.getDataType() == DataType.SHORT) {
                    fileValueU = (short) att.getNumericValue();
                } else if (att.getDataType() == DataType.FLOAT) {
                    fileValueU = (float) att.getNumericValue();
                } else if (att.getDataType() == DataType.DOUBLE) {
                    fileValueU = (double) att.getNumericValue();
                }
                fileValueU = (fileValueU * scale_factorU) + add_offsetU;
            }
        }

        for (Attribute att : attributesV) {
            if (att.getFullName().equals("scale_factor")) {
                if (att.getDataType() == DataType.SHORT) {
                    scale_factorV = (short) att.getNumericValue();
                } else if (att.getDataType() == DataType.FLOAT) {
                    scale_factorV = (float) att.getNumericValue();
                } else if (att.getDataType() == DataType.DOUBLE) {
                    scale_factorV = (double) att.getNumericValue();
                }
            }
            if (att.getFullName().equals("add_offset")) {
                if (att.getDataType() == DataType.SHORT) {
                    add_offsetV = (short) att.getNumericValue();
                } else if (att.getDataType() == DataType.FLOAT) {
                    add_offsetV = (float) att.getNumericValue();
                } else if (att.getDataType() == DataType.DOUBLE) {
                    add_offsetV = (double) att.getNumericValue();
                }
            }
            if (att.getFullName().equals("_FillValue")) {
                if (att.getDataType() == DataType.SHORT) {
                    fileValueV = (short) att.getNumericValue();
                } else if (att.getDataType() == DataType.FLOAT) {
                    fileValueV = (float) att.getNumericValue();
                } else if (att.getDataType() == DataType.DOUBLE) {
                    fileValueV = (double) att.getNumericValue();
                }
                fileValueV = (fileValueV * scale_factorV) + add_offsetV;
            }
        }

        for (int j = 0; j < values.length; j++) {
            values[j] = (values[j] * scale_factorU) + add_offsetU;
            values2[j] = (values2[j] * scale_factorV) + add_offsetV;
        }

        png.png(outFilePath + ".png", values, values2, pg.lonnum, pg.latnum, pg.lons, pg.lats, pg.lone, pg.late, pg.lonstep, pg.latstep, elementUnit, fileValueU, fileValueV);

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
            }
//            else if (pg.lons < 180 && pg.lone > 179.88) {
//                values = ArrayReverseUtils.trans_reverse(datas, index);
//                pg.lons = (pg.lons - 180);
//                pg.lone = (pg.lone - 180);
//            }
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
