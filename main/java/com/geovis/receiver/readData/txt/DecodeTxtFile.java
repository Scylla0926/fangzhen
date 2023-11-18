package com.geovis.receiver.readData.txt;


import com.alibaba.fastjson.JSON;
import com.geovis.receiver.pojo.vo.ObsArgoFubBean;
import com.geovis.receiver.tools.FileUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 读取txt 文件
 */
@Component
public class DecodeTxtFile {

    public static void main(String[] args) {
        String str = "C:\\Users\\Administrator\\Desktop\\sss\\漂流浮标\\GDP";
        File[] file = new File(str).listFiles();
        for (File file1 : file) {
            try {
                List<String> list = Files.readAllLines(new File(file1.getAbsolutePath()).toPath());
                ArrayList<Map> maps = new ArrayList<>();
                for (String len : list) {
                    HashMap<Object, Object> map = new HashMap<>();
                    String[] split = len.split("\\s+");
                    map.put("lon", split[1]);
                    map.put("lat", split[2]);
                    map.put("u", split[3]);
                    map.put("v", split[4]);
                    maps.add(map);
                }
                String jsonString = JSON.toJSONString(maps);
                FileUtil.writeStrToFile(jsonString, file1.getAbsolutePath().replace(".txt", ".json"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Argo浮标
     *
     * @param dataPath
     * @return
     */
    public List<ObsArgoFubBean> readArgoFubDatFile(String dataPath) {
        List<String> spacTxtLines = null;
        ArrayList<ObsArgoFubBean> rest = new ArrayList<>();
        try {
            HashMap<String, String> headers = new HashMap<>();

            ArrayList<String> files = new ArrayList<>();
            // 标识 1 为 header 2 为 location 3 为file
            int i = 0;
            int j = 0;
            spacTxtLines = Files.readAllLines(new File(dataPath).toPath());
            for (String spacTxtLine : spacTxtLines) {
                if (spacTxtLine.contains("*HEADER")) {
                    i = 1;
                } else if (spacTxtLine.contains("*LOCATION")) {
                    i = 2;
                } else if (spacTxtLine.contains("*FILE")) {
                    i = 3;
                }
                switch (i) {
                    case 1:
                        if (spacTxtLine.contains(":")) {
                            String[] len = spacTxtLine.replace(" ", "").split(":");
                            if (len.length == 2) {
                                headers.put(len[0], len[1]);
                            } else {
                                headers.put(len[0], null);
                            }

                        }
                        break;
                    case 2:
                        if (spacTxtLine.contains(":")) {
                            String[] len = spacTxtLine.replace(" ", "").split(":");
                            headers.put(len[0], len[1]);
                        }
                        break;
                    case 3:
                        j++;
                        if (j <= 11) continue;
                        files.add(spacTxtLine);
                        break;
                    default:
                        continue;

                }
            }
            for (String file : files) {
                ObsArgoFubBean obsArgoFubBean = new ObsArgoFubBean();
                String[] split = file.split("\\s+");
                if (split.length > 10) continue;
                if (split[1].equals("COLUMN")) break;
                obsArgoFubBean.setPlatformNumber(headers.get("PLATFORMNUMBER"));
                obsArgoFubBean.setCycleNumber(headers.get("CYCLENUMBER"));
                obsArgoFubBean.setDateCreation(headers.get("DATECREATION"));
                obsArgoFubBean.setDateUpdate(headers.get("DATEUPDATE"));
                obsArgoFubBean.setProjectName(headers.get("PROJECTNAME"));
                obsArgoFubBean.setPiName(headers.get("PINAME"));
                obsArgoFubBean.setInstrumentType(headers.get("INSTRUMENTTYPE"));
                obsArgoFubBean.setFloatSerialNo(headers.get("FLOATSERIALNO"));
                obsArgoFubBean.setFirmwareVersion(headers.get("FIRMWAREVERSION"));
                obsArgoFubBean.setWmoInstrumentType(headers.get("WMOINSTRUMENTTYPE"));
                obsArgoFubBean.setTransmissionSystem(headers.get("TRANSMISSIONSYSTEM"));
                obsArgoFubBean.setPositioningSystem(headers.get("POSITIONINGSYSTEM"));
//                obsArgoFubBean.setDate(headers.get("DATE"));
                if (headers.get("DATE") != null) {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(headers.get("DATE"));
                    obsArgoFubBean.setDataTime(date);
//                    obsArgoFubBean.setDataTime(headers.get("DATE").replace("-", ""));
                }
                obsArgoFubBean.setLon(Double.valueOf(headers.get("LONGITUDE")));
                obsArgoFubBean.setLat(Double.valueOf(headers.get("LATITUDE")));
                obsArgoFubBean.setLp(Double.valueOf(split[1]));
                obsArgoFubBean.setCorrectedPressure(Double.valueOf(split[2]));
                obsArgoFubBean.setQualityPressure(Double.valueOf(split[3]));
                obsArgoFubBean.setAt(Double.valueOf(split[4]));
                obsArgoFubBean.setCorrectedTemperature(Double.valueOf(split[5]));
                obsArgoFubBean.setQualityTemperature(Double.valueOf(split[6]));
                obsArgoFubBean.setSalt(Double.valueOf(split[7]));
                obsArgoFubBean.setCorrectedSalinity(Double.valueOf(split[8]));
                obsArgoFubBean.setQualitySalinity(Double.valueOf(split[9]));
                obsArgoFubBean.setStationChn(obsArgoFubBean.getPlatformNumber());
                rest.add(obsArgoFubBean);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return rest;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return rest;
    }

    /**
     * 浅层漂流浮标
     *
     * @param dataPath
     * @return
     */
//    public List<ObsSurfDriftFubBean> readSurfDriftFubTxtFile(String dataPath) {
//        List<String> spacTxtLines = null;
//        List<ObsSurfDriftFubBean> rest = new ArrayList<>();
//
//        try {
//            spacTxtLines = Files.readAllLines(new File(dataPath).toPath());
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            int i = 0;
//            for (String spacTxtLine : spacTxtLines) {
//                String[] station = spacTxtLine.replace(", ", "_").split(",");
//                if (station.length < 32) continue;
//                if (i < 2) {
//                    i++;
//                    continue;
//                }
//                ObsSurfDriftFubBean fubBean = new ObsSurfDriftFubBean();
//                fubBean.setStationChn(station[0]);
//                fubBean.setPlatformType(station[1]);
//                fubBean.setCountry(station[2]);
////                if (station[3].contains("REPUBLIC OF")) {
////                    System.out.println();
////                }
//                Date parse = sdf.parse(station[3].replace("T", " ").replace("Z", ""));
//                fubBean.setDataTime(parse);
//                fubBean.setLat(Double.valueOf(station[4]));
//                fubBean.setLon(Double.valueOf(station[5]));
//                fubBean.setObservationDepth(Double.valueOf(station[6]));
//                fubBean.setSst(station[7].equals("NaN") ? null : Double.valueOf(station[7]));
//                fubBean.setAt(station[8].equals("NaN") ? null : Double.valueOf(station[8]));
//                fubBean.setRain(station[9].equals("NaN") ? null : Double.valueOf(station[9]));
//                fubBean.setSlat(station[10].equals("NaN") ? null : Double.valueOf(station[10]));
//                fubBean.setZtmp(station[11].equals("NaN") ? null : Double.valueOf(station[11]));
//                fubBean.setZsla(station[12].equals("NaN") ? null : Double.valueOf(station[12]));
//                fubBean.setSlp(station[13].equals("NaN") ? null : Double.valueOf(station[13]));
//                fubBean.setWs(station[14].equals("NaN") ? null : Double.valueOf(station[14]));
//                fubBean.setWd(station[15].equals("NaN") ? null : Double.valueOf(station[15]));
//                fubBean.setSwh(station[16].equals("NaN") ? null : Double.valueOf(station[16]));
//                fubBean.setWaterLevel(station[17].equals("NaN") ? null : Double.valueOf(station[17]));
//                fubBean.setCla(station[18].equals("NaN") ? null : Double.valueOf(station[18]));
//                fubBean.setTd(station[19].equals("NaN") ? null : Double.valueOf(station[19]));
//                fubBean.setUo(station[20].equals("NaN") ? null : Double.valueOf(station[20]));
//                fubBean.setVo(station[21].equals("NaN") ? null : Double.valueOf(station[21]));
//                fubBean.setWo(station[22].equals("NaN") ? null : Double.valueOf(station[22]));
//                fubBean.setRainfallRate(station[23].equals("NaN") ? null : Double.valueOf(station[23]));
//                fubBean.setRh(station[24].equals("NaN") ? null : Double.valueOf(station[24]));
//                fubBean.setSeaWaterElecConductivity(station[25].equals("NaN") ? null : Double.valueOf(station[25]));
//                fubBean.setSeaWaterPressure(station[26].equals("NaN") ? null : Double.valueOf(station[26]));
//                fubBean.setRlds(station[27].equals("NaN") ? null : Double.valueOf(station[27]));
//                fubBean.setRsds(station[28].equals("NaN") ? null : Double.valueOf(station[28]));
//                fubBean.setWaterlevelMetRes(station[29].equals("NaN") ? null : Double.valueOf(station[29]));
//                fubBean.setWaterLevelWrtLcd(station[30].equals("NaN") ? null : Double.valueOf(station[30]));
//                fubBean.setWaterColHt((station[31].equals("NaN") ? null : Double.valueOf(station[31])));
//                fubBean.setWindToDirection((station[32].equals("NaN") ? null : Double.valueOf(station[32])));
//                fubBean.setLon360(station[33].equals("NaN") ? null : Double.valueOf(station[33]));
//                rest.add(fubBean);
//            }
//            System.out.println();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return rest;
//    }

    /**
     * 读取海洋站数据
     *
     * @param dataPath
     * @return
     */
    public static List<List<String>> readGTSCBTxtFile(String dataPath) {
        List<String> spacTxtLines = null;
        try {
            spacTxtLines = Files.readAllLines(new File(dataPath).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String spacTxtLine : spacTxtLines) {
            String[] split = (spacTxtLine.replace("\u0000", " ").replace("0\u0002", "0 ")
                    .split("\\s+"));
            return splitBySize(split, 30);
        }
        return null;
    }

//    public List<ObsHYZBean> readHyzTxtFile(String dataPath) {
//        List<ObsHYZBean> rest = new ArrayList<>();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
//        File file = new File(dataPath);
//        if (!file.exists()) {
//            return rest;
//        }
//        try {
//            List<String> spacTxtLines = Files.readAllLines(file.toPath(), StandardCharsets.ISO_8859_1);
//            for (String spacTxtLine : spacTxtLines) {
//                String[] split = spacTxtLine.replace("\u0000", " ").split(spacTxtLine.substring(0, 8));
//                for (String len : split) {
//                    if (len == null || len.length() == 0 || "".equals(len)) continue;
//                    // 每个站的数据
//                    // - 代表该要素缺值
//                    String[] station = len.split("\\s+");
//                    ObsHYZBean hyjFubBean = new ObsHYZBean();
//                    Date parse = sdf.parse(spacTxtLine.substring(0, 8) + station[1]);
//                    hyjFubBean.setDataTime(parse);
//                    hyjFubBean.setStationChn(station[2]);
//                    hyjFubBean.setLat(station[3].equals("-") ? null : Double.valueOf(station[3]) / 100);
//                    hyjFubBean.setLon(station[4].equals("-") ? null : Double.valueOf(station[4]) / 100);
//                    hyjFubBean.setWd(station[5].equals("-") ? null : Double.valueOf(station[5]));
//                    hyjFubBean.setWs(station[6].equals("-") ? null : Double.valueOf(station[6]));
//                    hyjFubBean.setZwd(station[7].equals("-") ? null : Double.valueOf(station[7]));
//                    hyjFubBean.setZws(station[8].equals("-") ? null : Double.valueOf(station[8]));
//                    hyjFubBean.setVis(station[9].equals("-") ? null : Double.valueOf(station[9]));
//                    hyjFubBean.setSlp(station[10].equals("-") ? null : Double.valueOf(station[10]));
//                    hyjFubBean.setAt(station[11].equals("-") ? null : Double.valueOf(station[11]));
//                    hyjFubBean.setRho(station[12].equals("-") ? null : String.valueOf(station[12]));
//                    hyjFubBean.setRain(station[13].equals("-") ? null : Double.valueOf(station[13]));
//                    hyjFubBean.setSst(station[14].equals("-") ? null : Double.valueOf(station[14]));
//                    hyjFubBean.setHd(station[15].equals("-") ? null : Double.valueOf(station[15]));
//                    hyjFubBean.setSwh(station[16].equals("-") ? null : Double.valueOf(station[16]));
//                    hyjFubBean.setWwp(station[17].equals("-") ? null : Double.valueOf(station[17]));
//                    hyjFubBean.setAvgSwh(station[18].equals("-") ? null : Double.valueOf(station[18]));
//                    hyjFubBean.setAvgWwp(station[19].equals("-") ? null : Double.valueOf(station[19]));
//                    hyjFubBean.setSwh10(station[20].equals("-") ? null : Double.valueOf(station[20]));
//                    hyjFubBean.setWwp10(station[21].equals("-") ? null : Double.valueOf(station[21]));
//                    hyjFubBean.setMaxSwh(station[22].equals("-") ? null : Double.valueOf(station[22]));
//                    hyjFubBean.setMaxWwp(station[23].equals("-") ? null : Double.valueOf(station[23]));
//                    hyjFubBean.setFd(station[24].equals("-") ? null : Double.valueOf(station[24]));
//                    hyjFubBean.setFs(station[25].equals("-") ? null : Double.valueOf(station[25]));
//                    hyjFubBean.setSrcFileName(file.getName());
//                    hyjFubBean.setSrcFileSize(Double.valueOf(file.length()));
//                    hyjFubBean.setSrcFileAbsolutePath(file.getAbsolutePath());
//                    rest.add(hyjFubBean);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return rest;
//    }

    /**
     * 读取海洋局浮标文件
     *
     * @param dataPath
     * @return
     */
//    public List<ObsHYJFubBean> readHyjFubTxtFile(String dataPath) {
//        List<ObsHYJFubBean> rest = new ArrayList<>();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
//        File file = new File(dataPath);
//        if (!file.exists()) {
//            return rest;
//        }
//        try {
//            List<String> spacTxtLines = Files.readAllLines(file.toPath());
//            for (String spacTxtLine : spacTxtLines) {
//                String[] split = spacTxtLine.replace("\u0000", " ").split(spacTxtLine.substring(0, 8));
//                for (String len : split) {
//                    if (len == null || len.length() == 0 || "".equals(len)) continue;
//                    // 每个站的数据
//                    // - 代表该要素缺值
//                    String[] station = len.split("\\s+");
//                    ObsHYJFubBean obsHyjFubBean = new ObsHYJFubBean();
//                    Date parse = sdf.parse(spacTxtLine.substring(0, 8) + station[1]);
//                    obsHyjFubBean.setDataTime(parse);
//                    obsHyjFubBean.setStationChn(station[2]);
//                    obsHyjFubBean.setLat(station[3].equals("-") ? null : Double.valueOf(station[3]) / 100);
//                    obsHyjFubBean.setLon(station[4].equals("-") ? null : Double.valueOf(station[4]) / 100);
//                    obsHyjFubBean.setWd(station[5].equals("-") ? null : Double.valueOf(station[5]));
//                    obsHyjFubBean.setWs(station[6].equals("-") ? null : Double.valueOf(station[6]));
//                    obsHyjFubBean.setZwd(station[7].equals("-") ? null : Double.valueOf(station[7]));
//                    obsHyjFubBean.setZws(station[8].equals("-") ? null : Double.valueOf(station[8]));
//                    obsHyjFubBean.setVis(station[9].equals("-") ? null : Double.valueOf(station[9]));
//                    obsHyjFubBean.setSlp(station[10].equals("-") ? null : Double.valueOf(station[10]));
//                    obsHyjFubBean.setAt(station[11].equals("-") ? null : Double.valueOf(station[11]));
//                    obsHyjFubBean.setRho(station[12].equals("-") ? null : String.valueOf(station[12]));
//                    obsHyjFubBean.setRain(station[13].equals("-") ? null : Double.valueOf(station[13]));
//                    obsHyjFubBean.setSst(station[14].equals("-") ? null : Double.valueOf(station[14]));
//                    obsHyjFubBean.setHd(station[15].equals("-") ? null : Double.valueOf(station[15]));
//                    obsHyjFubBean.setSwh(station[16].equals("-") ? null : Double.valueOf(station[16]));
//                    obsHyjFubBean.setWwp(station[17].equals("-") ? null : Double.valueOf(station[17]));
//                    obsHyjFubBean.setAvgSwh(station[18].equals("-") ? null : Double.valueOf(station[18]));
//                    obsHyjFubBean.setAvgWwp(station[19].equals("-") ? null : Double.valueOf(station[19]));
//                    obsHyjFubBean.setSwh10(station[20].equals("-") ? null : Double.valueOf(station[20]));
//                    obsHyjFubBean.setWwp10(station[21].equals("-") ? null : Double.valueOf(station[21]));
//                    obsHyjFubBean.setMaxSwh(station[22].equals("-") ? null : Double.valueOf(station[22]));
//                    obsHyjFubBean.setMaxWwp(station[23].equals("-") ? null : Double.valueOf(station[23]));
//                    obsHyjFubBean.setFd(station[24].equals("-") ? null : Double.valueOf(station[24]));
//                    obsHyjFubBean.setFs(station[25].equals("-") ? null : Double.valueOf(station[25]));
//                    obsHyjFubBean.setSrcFileName(file.getName());
//                    obsHyjFubBean.setSrcFileSize(Double.valueOf(file.length()));
//                    obsHyjFubBean.setSrcFileAbsolutePath(file.getAbsolutePath());
//                    rest.add(obsHyjFubBean);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return rest;
//    }

    /**
     * 读取CTS浮标文件
     *
     * @param
     * @return
     */
//    public static List<ObsGTSFubBean> readCgsFubTxtFile(String dataPath) {
//        List<ObsGTSFubBean> rest = new ArrayList<>();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
//        File file = new File(dataPath);
//        if (!file.exists()) {
//            return rest;
//        }
//        try {
//            List<String> spacTxtLines = Files.readAllLines(file.toPath());
//            for (String spacTxtLine : spacTxtLines) {
//                String[] split = spacTxtLine.replace("\u0000", " ").split(spacTxtLine.substring(0, 8));
//                for (String len : split) {
//                    if (len == null || len.length() == 0 || "".equals(len)) continue;
//                    // 每个站的数据
//                    // - 代表该要素缺值
//                    String[] station = len.split("\\s+");
//                    ObsGTSFubBean hyjFubBean = new ObsGTSFubBean();
//                    Date parse = sdf.parse(spacTxtLine.substring(0, 8) + station[1]);
//                    hyjFubBean.setDataTime(parse);
//                    hyjFubBean.setStationChn(station[2]);
//                    hyjFubBean.setLat(station[3].equals("-") ? null : Double.valueOf(station[3]) / 100);
//                    hyjFubBean.setLon(station[4].equals("-") ? null : Double.valueOf(station[4]) / 100);
//                    hyjFubBean.setWd(station[5].equals("-") ? null : Double.valueOf(station[5]));
//                    hyjFubBean.setWs(station[6].equals("-") ? null : Double.valueOf(station[6]));
//                    hyjFubBean.setZwd(station[7].equals("-") ? null : Double.valueOf(station[7]));
//                    hyjFubBean.setZws(station[8].equals("-") ? null : Double.valueOf(station[8]));
//                    hyjFubBean.setVis(station[9].equals("-") ? null : Double.valueOf(station[9]));
//                    hyjFubBean.setSlp(station[10].equals("-") ? null : Double.valueOf(station[10]));
//                    hyjFubBean.setAt(station[11].equals("-") ? null : Double.valueOf(station[11]));
//                    hyjFubBean.setRho(station[12].equals("-") ? null : String.valueOf(station[12]));
//                    hyjFubBean.setRain(station[13].equals("-") ? null : Double.valueOf(station[13]));
//                    hyjFubBean.setSst(station[14].equals("-") ? null : Double.valueOf(station[14]));
//                    hyjFubBean.setHd(station[15].equals("-") ? null : Double.valueOf(station[15]));
//                    hyjFubBean.setSwh(station[16].equals("-") ? null : Double.valueOf(station[16]));
//                    hyjFubBean.setWwp(station[17].equals("-") ? null : Double.valueOf(station[17]));
//                    hyjFubBean.setAvgSwh(station[18].equals("-") ? null : Double.valueOf(station[18]));
//                    hyjFubBean.setAvgWwp(station[19].equals("-") ? null : Double.valueOf(station[19]));
//                    hyjFubBean.setSwh10(station[20].equals("-") ? null : Double.valueOf(station[20]));
//                    hyjFubBean.setWwp10(station[21].equals("-") ? null : Double.valueOf(station[21]));
//                    hyjFubBean.setMaxSwh(station[22].equals("-") ? null : Double.valueOf(station[22]));
//                    hyjFubBean.setMaxWwp(station[23].equals("-") ? null : Double.valueOf(station[23]));
//                    hyjFubBean.setFd(station[24].equals("-") ? null : Double.valueOf(station[24]));
//                    hyjFubBean.setFs(station[25].equals("-") ? null : Double.valueOf(station[25]));
//                    hyjFubBean.setSrcFileName(file.getName());
//                    hyjFubBean.setSrcFileSize(Double.valueOf(file.length()));
//                    hyjFubBean.setSrcFileAbsolutePath(file.getAbsolutePath());
//                    rest.add(hyjFubBean);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return rest;
//    }
    public static String[] splitByLength(String str, int length) {
        if (str == null || length < 1) {
            return null;
        }
        int strLen = str.length();  // 字符串长度
        int arrayLen = (strLen + length - 1) / length;  // 数组长度
        String[] strArray = new String[arrayLen];
        for (int i = 0; i < arrayLen; i++) {
            int start = i * length;
            int end = Math.min((i + 1) * length, strLen);
            strArray[i] = str.substring(start, end);
        }
        return strArray;
    }

    public static <T> List<List<T>> splitBySize(T[] array, int size) {
        if (array == null || size < 1) {
            return null;
        }
        int length = array.length;
        int block = (length + size - 1) / size;
        List<List<T>> dataList = new ArrayList<>();
        for (int i = 0; i < block; i++) {
            int fromIndex = i * size;
            int toIndex = Math.min((i + 1) * size, length);
            List<T> subList = Arrays.asList(Arrays.copyOfRange(array, fromIndex, toIndex));
            dataList.add(subList);
        }
        return dataList;
    }


}
