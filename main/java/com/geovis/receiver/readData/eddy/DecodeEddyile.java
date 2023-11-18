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
public class DecodeEddyile {
    public static void main(String[] args) {
        decode("C:/Users/Public/Nwt/cache/recv/杨晓龙/Result_ssh/eddy_detect_20200102.nc");
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
        short[] amplitude = (short[]) NcReaderUtils.getArray(datasMap, "amplitude").copyToNDJavaArray();
        // 涡的编号
        int[] track = (int[]) NcReaderUtils.getArray(datasMap, "track").copyToNDJavaArray();
        // 中心纬度
        float[] lat = (float[]) NcReaderUtils.getArray(datasMap, "latitude").copyToNDJavaArray();
        // 中心经度
        float[] lon = (float[]) NcReaderUtils.getArray(datasMap, "longitude").copyToNDJavaArray();
        //半径
        short[] radius = (short[]) NcReaderUtils.getArray(datasMap, "effective_radius").copyToNDJavaArray();
        // 冷 暖 涡 标识
        long[] type = (long[]) NcReaderUtils.getArray(datasMap, "eddy_type").copyToNDJavaArray();

        double[] propagateDirection = (double[]) NcReaderUtils.getArray(datasMap, "propagate_direction").copyToNDJavaArray();
        //
        double[] propagateSpeed = (double[]) NcReaderUtils.getArray(datasMap, "propagate_speed").copyToNDJavaArray();

        short[] speedAverage = (short[]) NcReaderUtils.getArray(datasMap, "speed_average").copyToNDJavaArray();

        float[] effectiveArea = (float[]) NcReaderUtils.getArray(datasMap, "effective_area").copyToNDJavaArray();

        for (int i = 0; i < amplitude.length; i++) {

            HashMap<String, Object> feature = new HashMap<>();
            feature.put("type", "Feature");
            HashMap<String, Object> geometry = new HashMap<>();
            geometry.put("type", "LineString");
            List<Object> coordinates = new ArrayList<>();

            geometry.put("coordinates", coordinates);
            feature.put("geometry", geometry);

            Map<String, Object> properties = new HashMap<>();

            // 振幅
            properties.put("amplitude", amplitude[i]);
            //id
            properties.put("track", track[i]);
            properties.put("propagateDirection", (double) Math.round(propagateDirection[i] * 100) / 100);
            properties.put("propagateSpeed", (double) Math.round(propagateSpeed[i] * 100) / 100);
            properties.put("speedAverage", (short) Math.round(speedAverage[i] * 100) / 100);
            properties.put("effectiveArea", (float) Math.round(effectiveArea[i] * 100) / 100);
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
