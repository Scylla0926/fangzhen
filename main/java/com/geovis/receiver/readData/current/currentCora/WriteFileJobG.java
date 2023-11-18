package com.geovis.receiver.readData.current.currentCora;

import com.geovis.receiver.pojo.model.NcBeanPg;
import com.geovis.receiver.tools.*;
import ucar.ma2.DataType;
import ucar.nc2.Attribute;
import ucar.nc2.Variable;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 处理 3/4维产品
 *
 * @category
 */
public class WriteFileJobG implements Runnable {
    //	private double[][] datas;
    private String outFilePath;
    private CopyOnWriteArrayList<String> outPathes;
    private GribToPngUtils png;
    private NcBeanPg pg = new NcBeanPg();
    private NcBeanPg pg_o = new NcBeanPg();
    //	private double[] values;
//	private double[] lonWestLatSouth;
//	private double[] interval;
    private CountDownLatch latch;
    private String prefixFileName;
    private float[] ps;
    private Map<String, Object> datasMap;
    private String elName;
    private String element;
    private String layerName;
    private String timeName = null;
    private int timeSize = -1;
    private int i;
    private String outPath;
    private AtomicInteger total;
    private double[][] datasRain = null;
    private static DecimalFormat df = new DecimalFormat("0.00");


    public WriteFileJobG(Map<String, Object> datasMap, float[] ps, String prefixFileName, String elName, String element, String layerName, String outPath, int i, CopyOnWriteArrayList<String> outPathes, GribToPngUtils png, NcBeanPg pg, CountDownLatch latch) {
        this.datasMap = datasMap;
        this.ps = ps;
        this.prefixFileName = prefixFileName;
        this.elName = elName;
        this.element = element;
        this.outFilePath = outPath;
        this.layerName = layerName;
        this.i = i;
        this.outPathes = outPathes;
        this.png = png;
        this.pg_o = pg;
        copy(pg, this.pg);
        this.latch = latch;
        this.total = total;
    }

    private void copy(NcBeanPg from, NcBeanPg to) {
        TransObjUtil.transObj(from, to);
    }

    @Override
    public void run() {
        Thread.currentThread().setName(prefixFileName);
        double[][] datas = null;

        //datas = NcReaderUtils.readByElemNameLayerSlice(datasMap, elName, layerName);

        datas = NcReaderUtils.readByNameLayer(datasMap, elName, layerName);

        String elNames = String.valueOf(datasMap.get(elName));


        String elementUnit = NcReaderUtils.getElementUnit(datasMap, elName);

        String dataType = DataTypeUtils.getDataType(pg.filePath);


        String prefix = "";

        // 判断该要素 结构是否是 不小于三维数组
        Variable v = (Variable) datasMap.get(elName);
        String[] lonlats = null;
        if (v.getRank() > 2) {
            lonlats = NcReaderUtils.getLonLatName(datasMap);
        } else {
            lonlats = NcReaderUtils.getLonLatNameTwo(datasMap);
        }

        // 读取步长方法 因 外协 水文海洋锋需要 加入prefixFileName 参数 判断
        double[] interval = NcReaderUtils.getGridInterval(datasMap, lonlats[0], lonlats[1], prefixFileName);
        // 数组反转方法
        if (interval[1] > 0) {
            double[][] datas1 = new double[datas.length][];
            for (int i = 0, count = datas.length; i < count; i++) {
                datas1[i] = datas[count - 1 - i];
            }
            datas = datas1;
        }
        double[] values = ArrayReverseUtils.trans( datas);

        int index = CalculateIndexUtils.getIndex(pg.lons, pg.lonstep);
        // 根据经纬度 做判断1 是否对数组 进行处理
        //  源 pg.lone == 360 全球的  现 因为 大浪产品打补丁 修改为 359.8
//        if (pg.lons == 0 && (pg.lone >= 359.8 || pg.lone == Math.abs(360 - interval[0]))) {
//            values = ArrayReverseUtils.transReverse(datas, index);
//            pg.lons = (pg.lons - 180);
//            pg.lone = (pg.lone - 180);
//            pg_o.lons = pg.lons;
//            pg_o.lone = pg.lone;
//        } else if (pg.lons >= 179) {
//            values = ArrayReverseUtils.transToOneArray(datas);
//            pg.lons = (pg.lons - 360);
//            pg.lone = (pg.lone - 360);
//            pg_o.lons = pg.lons;
//            pg_o.lone = pg.lone;
//        } else if (pg.lons < 180 && pg.lone > 180 && !elName.equals("var")) {
//            pg.lone = (pg.lone - 360);
//            pg_o.lone = pg.lone;
//            values = ArrayReverseUtils.trans_reverse(datas, index);
//        }

        List<Attribute> attributes = v.getAttributes();

        double scale_factor = (double) 1;
        double add_offset = (double) 0;
        for (Attribute att : attributes) {
            if (att.getFullName().equals("scale_factor")) {
                if (att.getDataType() == DataType.SHORT) {
                    scale_factor = (short) att.getNumericValue();
                } else if (att.getDataType() == DataType.FLOAT) {
                    scale_factor = (float) att.getNumericValue();
                } else if (att.getDataType() == DataType.DOUBLE) {
                    scale_factor = (double) att.getNumericValue();
                }
            }
            if (att.getFullName().equals("add_offset")) {
                if (att.getDataType() == DataType.SHORT) {
                    add_offset = (short) att.getNumericValue();
                } else if (att.getDataType() == DataType.FLOAT) {
                    add_offset = (float) att.getNumericValue();
                } else if (att.getDataType() == DataType.DOUBLE) {
                    add_offset = (double) att.getNumericValue();
                }
            }
        }
        for (int j = 0; j < values.length; j++) {
            values[j] = (values[j] * scale_factor) + add_offset;

            if (elName.equals("air") || elName.equals("skt")) {
                values[j] = values[j] - 273.15;
            } else if (elName.equals("mslp")) {
                values[j] = values[j] / 100;
            }
        }


//        double[] lonWestLatSouth = NcReaderUtils.getLonWestLatSouth(datasMap, prefix + NcReaderUtils.lon, prefix + NcReaderUtils.lat);

//        FileUtil.writeBytesToFile(outFilePath + ".bin", datas);

        //获取 写入hdr 文件中的字符串，用来在后续写入hdr文件中使用
//        String enviTxt = EnviTxtUtil.getEnviTxt(datas[0].length, datas.length, 4, lonWestLatSouth[0], lonWestLatSouth[1], interval[0], interval[1], elementUnit);
//        FileUtil.writeStrToFile(enviTxt, outFilePath + ".hdr");
        // 输入png

        png.png(outFilePath + ".png", values, pg.lonnum, pg.latnum, pg.lons, pg.lats, pg.lone, pg.late, pg.lonstep, pg.latstep, elementUnit);


        outPathes.add(outFilePath + ".png");


        latch.countDown();
    }
}
