package com.geovis.receiver.readData.depth.depthWoa18;

import com.geovis.receiver.pojo.model.NcBeanPg;
import com.geovis.receiver.pojo.vo.MldWoa18Vo;
import com.geovis.receiver.tools.*;
import org.springframework.stereotype.Component;
import ucar.ma2.Array;
import ucar.nc2.Variable;

import java.io.File;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 跃层 woa18 诊断产品
 */
@Component
public class DecodeMldWoa18NcFile {

    private static int cpus = Runtime.getRuntime().availableProcessors();
    private ExecutorService threadService = Executors.newFixedThreadPool(cpus * 2);
    private List<String> logs = new ArrayList<String>();

    public static void main(String[] args) {
        DecodeMldWoa18NcFile decodeMldWoa18NcFile = new DecodeMldWoa18NcFile();
        decodeMldWoa18NcFile.decode("D:\\Work_hjcjj\\PDS\\STAT\\WOA_MLD\\WOA18_00_MLD.nc","D:\\Work_hjcjj\\PDS\\STAT\\WOA_MLD","");
    }

    /**
     * 入口
     *
     * @param filePath
     * @param outPath
     * @return
     */
    public List<MldWoa18Vo> decode(String filePath, String outPath, String regex) {
        List<MldWoa18Vo> split = split(filePath, outPath, regex);
        return split;
    }

    /**
     * 解析数据
     *
     * @param filePath
     * @param outPath
     * @return
     */
    private List<MldWoa18Vo> split(String filePath, String outPath, String regex) {

        // 返回入库信息信息
        List<MldWoa18Vo> result = new ArrayList<MldWoa18Vo>();
        // 文件信息类

        //  判断文件存不存在 不存在 直接返回
        File file = new File(filePath);
        if (!file.exists()) {
            logs.add(filePath + "文件路径不存在！");
            return result;
        }
        // 输出目录为参数中的输出目录加上文件名
        if (outPath == null || outPath.length() == 0) {
            System.out.println("请配置文件输出路径!");
            return result;
        }
        // 处理文件路径中的 /
        if (!outPath.endsWith(File.separator) && !outPath.endsWith("/")) {
            outPath += File.separator;
        }

        String prefixFileName = new File(filePath).getName();
        //获取后缀
        String suffix = FileUtil.getSuffix(prefixFileName);


        //获取数据结构体
        Map<String, Object> datasMap = NcReaderUtils.getDatasMap(filePath);

        GribToPngUtils png = GribToPngUtils.getInstance();

        StringBuilder elemBuilder = new StringBuilder();
        StringBuilder psBuilder = new StringBuilder();
        StringBuilder layerBuilder = new StringBuilder();
        StringBuilder heightBuilder = new StringBuilder();
        StringBuilder vtiBuilder = new StringBuilder();

        CopyOnWriteArrayList<String> outPathes = new CopyOnWriteArrayList<String>();
        // 解析所需信息类
        NcBeanPg pg = new NcBeanPg();
        pg.filePath = filePath;
        // 根据 正则 从文件名上取出 文件诺没有文件名上 没有 文件时间则取 文件最后修改时间
        String fileDate = new FileUtil().getFileDate(prefixFileName, regex);
        pg.fileDate = fileDate;

        Set<String> keySet = datasMap.keySet();
        String prefix = "";

        String[] name = null;
        // 获取 每一个要素的结构体
        for (String elName : keySet) {

//            //获取每一个key对应的value 如果类型不是Variable 那么则跳出本次循环
            if (!(datasMap.get(elName) instanceof Variable)) {
                continue;
            }

//            //如果是Variable 那么就要判断是否为该要素中具有那些子要素 判断子要素数量是否大于2 如果大于2 再继续进行解析
            Variable variable = (Variable) datasMap.get(elName);
            int rank = variable.getRank();
            if (rank <= 1) {
                continue;
            }

            //获取 该属性的第一个要素的名称以及单位 一般第一个要素都是层数，所以这个可以理解为 获取该要素的层数子要素的名称以及单位 例如 ["depth","m","",""]
            name = NcReaderUtils.getDimensionNameAndUnit(datasMap, elName);
                String dimensionName = prefix + name[0];

            String outFilePath = "";
            String layerName = "";
            String element = "";

            float[] ps = null;
            if (name[0] == null || name[0].length() == 0) {
                ps = new float[]{1};
                name[1] = "pa";
            } else {
                //获取出所有的层数以及每一层对应的值
                ps = NcReaderUtils.toFloats(NcReaderUtils.getArray(datasMap, dimensionName).copyToNDJavaArray());
            }

            // 判断 该节点要素是否是 uv风相关
            // v风时 结束本次循环
            element = elName;
            {
                // 声明一个长度为 ps.length 的数据
                CountDownLatch latch = new CountDownLatch(ps.length);
                for (int i = 0, count = ps.length; i < count; i++) {
                    Long id = System.currentTimeMillis();
                    //某一层的值 例如 第0层 值为 5 那么layerName = 5
                    layerName = NumberFormatUtil.science(ps[i]);
                    if (name[1].toLowerCase().endsWith("pa")) {
                        psBuilder.append(layerName);
                        psBuilder.append(",");
                    } else if (name[1].toLowerCase().endsWith("m") || name[1].toLowerCase().endsWith("(m)")) {
                        heightBuilder.append(layerName);
                        heightBuilder.append(",");
                    } else if (name[1].toLowerCase().startsWith("hours")) {
                        vtiBuilder.append(layerName);
                        vtiBuilder.append(",");
                    } else {
                        layerBuilder.append(layerName);
                        layerBuilder.append(",");
                    }


                    // 组织解析所用的信息
                    getPg(pg, prefixFileName, suffix, layerName, element, datasMap, name[1]);

                    // 判断时间是否为 高度
                    if (!dimensionName.contains("time")) {
                        outFilePath = outPath + File.separator + element + File.separator + layerName + File.separator;
                    } else {
                        outFilePath = outPath + File.separator + element + File.separator + "SURF" + File.separator;
                    }
                    outFilePath = outFilePath + prefixFileName.split("\\.")[0].substring(0, prefixFileName.split("\\.")[0].length()) + "_" + layerName + "_" + element;

                    //  处理 UV 风
                    if (!elName.equalsIgnoreCase("u10")) {
                        threadService.execute(new WriteFileJobG(datasMap, ps, prefixFileName, elName, element, layerName, outFilePath, i, outPathes, png, pg, latch));
                    }
                    // 准备入库信息
                    MldWoa18Vo woa18Vo = new MldWoa18Vo();
                    woa18Vo.setId(id);
                    woa18Vo.setPngFileAbsolutePath(outFilePath + ".png");
                    woa18Vo.setSrcFileAbsolutePath(filePath);
                    woa18Vo.setSrcFileType(suffix);
                    woa18Vo.setEleName(elName);
                    woa18Vo.setInsertTime(new Date());
                    woa18Vo.setNumberX(pg.latnum);
                    woa18Vo.setNumberY(pg.lonnum);

                    FileUtil fileUtil = new FileUtil();
                    woa18Vo.setLevel(Double.valueOf(layerName));
                    woa18Vo.setRegionType(fileUtil.getFileRegion(file.getName(), regex));
                    woa18Vo.setMethod(fileUtil.getFileMethod(file.getName(), regex));
                    woa18Vo.setStatType(fileUtil.getStatType(file.getName(), regex));
                    if (woa18Vo.getStatType().equals("month")) {
                        woa18Vo.setMonth(fileUtil.getFileDate(file.getName(), regex));
                    }
                    result.add(woa18Vo);
                }
                try {
                    //  判断所有线程是否都已经 完成工作
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        new File(file.getAbsolutePath() + ".gbx9").delete();
        new File(file.getAbsolutePath() + ".ncx3").delete();
        return result;
    }


    private float[] getArrayDatas(Map<String, Object> datasMap, String elem) {
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
    private void getPg(NcBeanPg pg, String fileName, String suffix, String layerName, String elName, Map<String, Object> datasMap, String layerUnit) {
        get3Dts(pg, fileName, layerName, elName, datasMap, layerUnit);
    }

    private void get3Dts(NcBeanPg pg, String fileName, String layerName, String elName, Map<String, Object> datasMap, String layerUnit) {
        pg.vti = (int) Float.parseFloat(layerName);
        createPg(pg, fileName, layerName, elName, datasMap, layerUnit);
        if (pg.type == null) {
            pg.type = elName;
        }
    }

    private void createPg(NcBeanPg pg, String fileName, String layerName, String elName, Map<String, Object> datasMap, String layerUnit) {
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
        String[] lonlats = new String[]{"lon", "lat"};
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
}
