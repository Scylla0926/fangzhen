package com.geovis.receiver.factory;

import com.alibaba.fastjson.JSONObject;
import com.geovis.receiver.pojo.model.Downtown;
import com.geovis.receiver.pojo.model.Note;
import com.geovis.receiver.pojo.model.ThermocLine;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * @Package: com.geovis.receiver.factory
 * @ClassName: ReceiverInit
 * @author     ：yangxl
 * @date       ：Created in 2022/7/27 23:23
 * @description：公共属性定义类
 * @modified By：
 * @version:
 */
@Data
@Component
public class ReceiverInit {

    // 本机ip
    public static String ip ;

    //本机端口
    public static String port;

    // cnf 配置表信息
    public static List<Note> ns;

    public static String pds;

    public static String wsp;

    public static String CONFIG_XML_PATH;

    public static Map<String,List<JSONObject>> taskMap = new HashMap<String,List<JSONObject>>();

    public static List<JSONObject> prd;

    public static List<Downtown> downtownList = new ArrayList<Downtown>();

    public static  List<ThermocLine> thermocLineList;

    /**
     * 盐跃层浅海阈值
     */
    public static volatile  String thresholdShallowS;

    /**
     * 温跃层浅海阈值
     */
    public static volatile  String thresholdShallowT;

    /**
     * 温跃层深海阈
     */
    public static volatile String thresholdDeepT;

    /**
     * 密跃层浅海阈值
     */
    public static volatile  String thresholdShallowR;

    /**
     * 盐跃层深海阈值
     */
    public static volatile   String thresholdDeepS;

    /**
     * 密跃层深海阈值
     */
    public static volatile String thresholdDeepR;


}
