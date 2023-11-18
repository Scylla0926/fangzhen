package com.geovis.receiver.readData.subBuoy;

import com.alibaba.fastjson.JSONObject;
import com.geovis.receiver.pojo.bean.NwpPngCurrencyBean;
import com.geovis.receiver.pojo.model.NcBeanPg;
import com.geovis.receiver.pojo.vo.SubSurfaceBuoyVo;
import com.geovis.receiver.readData.nc.WriteFileJobG;
import com.geovis.receiver.readData.nc.WriteFileJobUV;
import com.geovis.receiver.tools.*;
import org.springframework.stereotype.Component;
import ucar.ma2.Array;
import ucar.nc2.Variable;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @category
 * @Date 2022/4/25 15:56
 */
@Component
public class DecodeBuoyNCFile {

    private static int cpus = Runtime.getRuntime().availableProcessors();

    private List<String> logs = new ArrayList<String>();

    public static void main(String[] args) throws IOException, ParseException {
        String filePath = "D:\\DATA\\HJ_CJJ\\潜标\\obs_demo1.nc";
        String outPath = "D:\\DATA\\HJ_CJJ\\潜标";
        List<SubSurfaceBuoyVo> data = new DecodeBuoyNCFile().decode(filePath, outPath, "\\\\d{4}\\\\d{2}\\\\d{2}.(\\\\w+)$");
        System.out.println(JSONObject.toJSONString(data));
    }


    /**
     * 入口
     *
     * @param filePath
     * @param outPath
     * @return
     */
    public List<SubSurfaceBuoyVo> decode(String filePath, String outPath, String regex) throws IOException, ParseException {
        List<SubSurfaceBuoyVo> split = split(filePath, outPath, regex);
        return split;
    }

    /**
     * 解析数据
     *
     * @param filePath
     * @param outPath
     * @return
     */
    private List<SubSurfaceBuoyVo> split(String filePath, String outPath, String regex) throws IOException, ParseException {

        // 返回入库信息信息
        List<SubSurfaceBuoyVo> beansList=new ArrayList<>();
        //  判断文件存不存在 不存在 直接返回
        File file = new File(filePath);
        if (!file.exists()) {
            logs.add(filePath + "文件路径不存在！");
            return beansList;
        }

        String prefixFileName = new File(filePath).getName();
        //获取后缀
        String suffix = FileUtil.getSuffix(prefixFileName);


        //获取数据结构体
        Map<String, Object> datasMap = NcReaderUtils.getDatasMap(filePath);

        // 解析所需信息类
        // 根据 正则 从文件名上取出 文件诺没有文件名上 没有 文件时间则取 文件最后修改时间
        String fileDate = new FileUtil().getFileDate(prefixFileName, regex);
        String prefix = "";

        ///经纬度
        double[] lonLat=NcReaderUtils.getLonLatTwo(datasMap);
        ///层数
        float[] zArr=NcReaderUtils.toFloats(NcReaderUtils.getArray(datasMap, "z").copyToNDJavaArray());
        //////时间一维数组
        double[] timeList = (double[]) NcReaderUtils.getArray(datasMap, "time").copyToNDJavaArray();
        /////温度
        double[][] tArr=NcReaderUtils.readByNameLayer(datasMap,"t",null);
        ///声速
        double[][] csArr=NcReaderUtils.readByNameLayer(datasMap,"cs",null);
        ///盐度
        double[][] sArr=NcReaderUtils.readByNameLayer(datasMap,"s",null);
        ///密度
        double[][] rhoArr=NcReaderUtils.readByNameLayer(datasMap,"rho",null);
        ///u
        double[][] uArr=NcReaderUtils.readByNameLayer(datasMap,"u",null);
        ///v
        double[][] vArr=NcReaderUtils.readByNameLayer(datasMap,"v",null);
        ///Depth
        double[][] depthArr=NcReaderUtils.readByNameLayer(datasMap,"Depth",null);

        if(timeList.length>0&&zArr.length>0){
            for(int level=0;level<zArr.length;level++){
                for(int i=0;i<timeList.length;i++){
                    SubSurfaceBuoyVo vo=new SubSurfaceBuoyVo();
                    vo.setId(UUID.randomUUID().toString());
                    vo.setCs( csArr[level][i]);
                    vo.setT( tArr[level][i]);
                    vo.setS( sArr[level][i]);
                    vo.setRho( rhoArr[level][i]);
                    vo.setCs( csArr[level][i]);
                    vo.setU( uArr[level][i]);
                    vo.setV( vArr[level][i]);
                    vo.setDepth( depthArr[level][i]);
                    ///日期
                    vo.setInsertTime(new Date());
                    vo.setDataTime(TimeUtil.getYmdhms(timeList[i]));
                    //经纬度
                    vo.setLon(lonLat[0]);
                    vo.setLat(lonLat[1]);
                    beansList.add(vo);
                }
            }
        }

        return beansList;
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

}
