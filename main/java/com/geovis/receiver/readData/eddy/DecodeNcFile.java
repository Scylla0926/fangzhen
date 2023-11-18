package com.geovis.receiver.readData.eddy;

import cn.hutool.json.JSONObject;
import com.geovis.receiver.tools.FileUtil;
import com.geovis.receiver.tools.NcReaderUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析数值预报 中尺度涡产品
 */
public class DecodeNcFile {
    public static void main(String[] args) {
        decode("C:\\Users\\Administrator\\Desktop\\HYCOM_EDDY_SSH_SSH_20200701.nc");
    }

    public static String decode(String filePath) {

        if (!new File(filePath).exists()) {
            return null;
        }

        // nc集合
        Map<String, Object> datasMap = NcReaderUtils.getDatasMap(filePath);

        // geojson 父标签
        List<Map<String, Object>> features = new ArrayList<>();


        // 振幅
        double[] amplitude = (double[]) NcReaderUtils.getArray(datasMap, "amplitude").copyToNDJavaArray();
        // 涡的编号
        double[] id = (double[]) NcReaderUtils.getArray(datasMap, "id").copyToNDJavaArray();
        // 中心纬度
        double[] lat = (double[]) NcReaderUtils.getArray(datasMap, "lat").copyToNDJavaArray();
        //
        double[] leddy = (double[]) NcReaderUtils.getArray(datasMap, "leddy").copyToNDJavaArray();
        // 中心经度
        double[] lon = (double[]) NcReaderUtils.getArray(datasMap, "lon").copyToNDJavaArray();
        //半径
        double[] radius = (double[]) NcReaderUtils.getArray(datasMap, "radius").copyToNDJavaArray();
        // 冷 暖 涡 标识
        double[] type = (double[]) NcReaderUtils.getArray(datasMap, "type").copyToNDJavaArray();
        // u向速度
        double[] u = (double[]) NcReaderUtils.getArray(datasMap, "u").copyToNDJavaArray();
        // v向速度
        double[] v = (double[]) NcReaderUtils.getArray(datasMap, "v").copyToNDJavaArray();
        //
        double[] ueddy = (double[]) NcReaderUtils.getArray(datasMap, "ueddy").copyToNDJavaArray();
        //
        double[] vorticity = (double[]) NcReaderUtils.getArray(datasMap, "vorticity").copyToNDJavaArray();
        // 涡形状 50点位x坐标
        double[][] xp = (double[][]) NcReaderUtils.getArray(datasMap, "xp").copyToNDJavaArray();
        // 涡形状 50点位y坐标
        double[][] yp = (double[][]) NcReaderUtils.getArray(datasMap, "yp").copyToNDJavaArray();

        for (int i = 0; i < amplitude.length; i++) {

            HashMap<String, Object> feature = new HashMap<>();
            feature.put("type", "Feature");
            HashMap<String, Object> geometry = new HashMap<>();
            geometry.put("type", "LineString");
            List<Object> coordinates = new ArrayList<>();

            // 50 点位写入
            for (int j = 0; j < xp[i].length; j++) {

                if (!Double.isNaN(xp[i][j])) {
                    List<Double> point = new ArrayList<>();
                    point.add((double) Math.round(xp[i][j] * 100) / 100);
                    point.add((double) Math.round(yp[i][j] * 100) / 100);
                    coordinates.add(point);
                }

            }
            geometry.put("coordinates", coordinates);
            feature.put("geometry", geometry);

            Map<String, Object> properties = new HashMap<>();

            // 振幅
            properties.put("amplitude", amplitude[i]);
            //id
            properties.put("id", id[i]);
            //
//            properties.put("leddy", leddy[i]);
            // 半径
            properties.put("radius", radius[i] / 1000);
            //冷 暖 涡 标识
            properties.put("type", type[i]);
            //u
//            properties.put("u", u[i]);
            //v
//            properties.put("v", v[i]);
            //
            properties.put("ueddy", (double) Math.round(ueddy[i] * 100) / 100);
            //
//            properties.put("vorticity", vorticity[i]);
            // 纬度
            properties.put("centerLat", (double) Math.round(lat[i] * 100) / 100);
            // 经度
            properties.put("centerLon", (double) Math.round(lon[i] * 100) / 100);

            feature.put("properties", properties);

            features.add(feature);
        }
        Map<String, Object> root = new HashMap<>();
        root.put("type", "FeatureCollection");
        root.put("features", features);

        JSONObject jsonObject = new JSONObject(root);
        String jsonString = jsonObject.toString();
        System.out.println(jsonString);
        String jsonPath = filePath.replace(".nc", ".json");
        FileUtil.writeStrToFile(jsonString, jsonPath);
        return jsonPath;
    }
}
