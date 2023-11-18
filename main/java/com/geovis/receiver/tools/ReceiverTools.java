package com.geovis.receiver.tools;

import com.alibaba.fastjson.JSONObject;
import com.geovis.receiver.factory.ReceiverInit;
import com.geovis.receiver.pojo.model.ConfigElement;
import com.geovis.receiver.pojo.model.Produce;
import com.geovis.receiver.pojo.model.Satellite_Hj1289;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;

/***
 * ReceiverTools 工具类
 */
@Data
@Component
public class ReceiverTools {

    @Autowired
    private readJson rj;

    @Autowired
    private ReadConfig rc;

    @Autowired
    private HttpClient httpClient;

    @Value("${workflow_ip}")
    private String workflowIp;

    @Value("${workflow_port}")
    private String workflowPort;

    @Value("${workflow_app}")
    private String workflowApp;

    /**
     * 读取json文件中的经纬度信息
     *
     * @param f
     * @param sat
     * @return
     */
    public String setLonLat(File f, Satellite_Hj1289 sat) {
        try {
            if (sat == null) {
                return null;
            }
            String jsonPath = f.getParent() + File.separator + f.getName().replace("NUL", "GLL").replace("SWC", "SWCA").split("\\.")[0] + ".json";
            File jf = new File(jsonPath);
            if (!jf.exists()) {
                return null;
            }
            String lonlat = rj.readJsonForLonLat(jf);
            return lonlat;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 发送通过http请求方式调度指令
     *
     * @param filePath
     * @param codes
     * @return
     */
    public boolean sendTask(String filePath, String[] codes) {
        // 由于在家测试 家里面没有调度环境 所以先把这个方法注释掉。
//        return true;
        try {
            for (String code : codes) {
                if (code == null || code.isEmpty() || Integer.parseInt(code) < 0) {
                    return true;
                }
                String ip = workflowIp;
                String port = workflowPort;
                String app = workflowApp;
                JSONObject result = new JSONObject();
                result.put("wf_tpt_id", Integer.parseInt(code));
                result.put("wf_type", "Normal");
                JSONObject envs = new JSONObject();
                envs.put("input_file", filePath);
                System.out.println("filePath ===========================>" + filePath);
                result.put("wf_envs", envs);
                System.out.println("SendTask------------------------------start!");
                System.out.println(JSONObject.toJSONString(result));
                System.out.println("SendTask------------------------------end!");
                String msg = httpClient.sendPost_json(ip, port, app, "", result);
                System.out.println("httpMsg:" + msg);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean sendTask(String filePath, String code) {
        // 由于在家测试 家里面没有调度环境 所以先把这个方法注释掉。
//        return true;
        try {
            if (code == null || code.isEmpty() || Integer.parseInt(code) < 0) {
                return true;
            }
            String ip = workflowIp;
            String port = workflowPort;
            String app = workflowApp;
            JSONObject result = new JSONObject();
            result.put("wf_tpt_id", Integer.parseInt(code));
            result.put("wf_type", "Normal");
            JSONObject envs = new JSONObject();
            envs.put("input_file", filePath);
            System.out.println("filePath ===========================>" + filePath);
            result.put("wf_envs", envs);
            System.out.println("SendTask------------------------------start!");
            System.out.println(JSONObject.toJSONString(result));
            System.out.println("SendTask------------------------------end!");
            String msg = httpClient.sendPost_json(ip, port, app, "", result);
            System.out.println("httpMsg:" + msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 根据产品列表以及卫星名称获取 产品标识
     *
     * @param productStrs
     * @param satellite
     * @return
     */
    public String getProductIdByCondition(String[] productStrs, String satellite) {
        String result = "";
        List<JSONObject> prdList = ReceiverInit.prd;
        for (String product : productStrs) {
            for (JSONObject jsonObject : prdList) {
                String productType = jsonObject.getString("prd_id");
                if (productType.equalsIgnoreCase(product)) {
                    String satList = jsonObject.getString("sat_list");
                    String tableId = jsonObject.getString("table_id");
                    if (satList == null || satList.split(",").length == 1) {
                        result += tableId + ",";
                    } else {
                        String[] satLists = satList.split(",");
                        String[] tableIds = tableId.split(",");
                        for (int index = 0; index < satLists.length; index++) {
                            if (satLists[index].equalsIgnoreCase(satellite)) {
                                result += tableIds[index] + ",";
                            }
                        }
                    }
                }
            }
        }
        return result.substring(0, result.length() - 1);
    }


    /**
     * 实时分发产品 非常难看的一段代码。。。
     *
     * @param pro
     */
    public void sendProduct(Produce pro, ConfigElement configElement) {
        CompressTools compressTools = new CompressTools();
        String lv = configElement.getEleName().split("_")[2];
        Map<String, List<JSONObject>> taskMap = ReceiverInit.taskMap;
        //默认不分发该数据
        boolean flag = false;
        String sendFloder = null;
        if (taskMap != null && taskMap.size() > 0) {
            //首先判断是否需要分发本次入库的数据
            List<JSONObject> dataList = taskMap.get("realTime");
            Satellite_Hj1289 sat = (Satellite_Hj1289) pro.getProductList().get(0);
            if (lv.equalsIgnoreCase("PGS") || lv.equalsIgnoreCase("MON") ||
                    lv.equalsIgnoreCase("FUSE") || lv.equalsIgnoreCase("IMG")) {
                //如果是这几种数据那么可能是需要进行分发的数据
                String satellite = sat.getSatelliteid();
                String sensor = sat.getSensorid();
                String product = sat.getProductid();
                String minute = sat.getDatastarttime().substring(10, 12);
                String level = lv;
                for (JSONObject json : dataList) {
                    //如果本次入库的数据的等级或者说产品类型与对应的某一个task 产品类型一致，就还需要进行判断是否为融合产品，因为融合产品没有对应的卫星载荷
                    if (json.getString("level").contains(level)) {
                        boolean timeFlag = false;
                        String[] frequencys = json.getString("frequency").split(",");
                        for (String frequency : frequencys) {
                            Integer time = Integer.parseInt(frequency);
                            Integer minuteInt = Integer.parseInt(minute);
                            if (minuteInt >= time - 5 && minuteInt <= time + 5) {
                                timeFlag = true;
                                break;
                            }
                        }
                        //如果数据时间都不对，无论是什么数据都不传输
                        if (timeFlag) {
                            if (level.equalsIgnoreCase("FUSE")) {
                                //如果确实是融合产品 那么就需要在进行判断是否是 对应的产品如果是则直接分发，如果不是则不进行分发。
                                //json.getString("product") 是存入数据库的数据， 其中的值为 KJGTX 、 DTDTX 等 需要转换为 对应的 产品种类标识才可以进行对比。
                                String productStr = json.getString("product");
                                String[] productStrs = productStr.split(",");
                                //根据productStrs 和卫星， 获取到产品标识列表 然后在进行判断
                                //根据列表和卫星获取到真实的产品标识
                                String productId = getProductIdByCondition(productStrs, satellite);
                                if (productId.contains(product)) {
                                    sendFloder = json.getString("floder");
                                    flag = true;
                                    break;
                                }
                            } else {
                                //那么需要判断 卫星载荷产品是否都相等
                                boolean sateFlag = satellite.equalsIgnoreCase(json.getString("satellite"));
                                boolean sensorFlag = sensor.equalsIgnoreCase(json.getString("sensor"));
                                String productStr = json.getString("product");
                                String[] productStrs = productStr.split(",");
                                //根据productStrs 和卫星， 获取到产品标识列表 然后在进行判断
                                //根据列表和卫星获取到真实的产品标识
                                System.out.println("sendProductProduct--------------------------" + JSONObject.toJSONString(productStrs));
                                System.out.println("sendProductSatellite--------------------------" + satellite);
                                if (sateFlag && sensorFlag) {
                                    String productId = getProductIdByCondition(productStrs, satellite);
                                    boolean productFlag = productId.contains(product);
                                    if (productFlag) {
                                        sendFloder = json.getString("floder");
                                        flag = true;
                                        break;

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (flag) {
            //如果是需要进行分发，那么就拷贝到归档目录下
            System.out.println("压缩文件中......");
            String suffix = pro.getFile().getAbsolutePath().substring(pro.getFile().getAbsolutePath().indexOf("."), pro.getFile().getAbsolutePath().length());
            String zipFileStr = pro.getFile().getAbsolutePath().replaceAll(suffix, ".zip");
            String fileName = pro.getFile().getAbsolutePath().split("PDS")[1].replaceAll(suffix, ".zip");
            File zipFile = new File(zipFileStr);
            compressTools.doZip(pro.getFile().getAbsolutePath(), zipFileStr);
            System.out.println("压缩完成......");
            System.out.println("准备分发......");
            String sendFile = sendFloder + File.separator + fileName;
            FileCopyUtils fileCopyUtils = new FileCopyUtils();
            boolean sendFlag = fileCopyUtils.fileCopy(zipFile, new File(sendFile), null);
            if (sendFlag) {
                zipFile.delete();
                System.out.println("产品分发成功!产品名称为:" + sendFile);
            } else {
                System.out.println("产品分发失败!");
            }
        }
    }
}
