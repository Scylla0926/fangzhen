package com.geovis.receiver.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author zls
 * @version 1.0.0
 * @Package com.geovis.receiver.tools
 * @ClassName NwpTimerUtils
 * @date 2023/2/9 14:31
 * @description TODO
 * 工具类 根据不同数值预报产品 取出相对应的 预报失效 和发布时间
 */

public class NwpTimerUtils {

    // 取出欧洲格点数据的预报失效
    public static String EcmfTimer(String fileData, String timer) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMDDHH");
        // 发布时间
        try {
            Date forecastTime = dateFormat.parse(fileData.substring(4, 10));
            Date timerTime = dateFormat.parse(timer);
            long l = timerTime.getTime() - forecastTime.getTime();
            timer = String.valueOf(l / 3600000);
            if (timer.length() == 2) {
                return "0" + timer;
            } else if (timer.length() == 1) {
                return "00" + timer;
            } else {
                return timer;
            }
        } catch (ParseException e) {
            System.out.println("EC 时间转换格式报错");
            e.printStackTrace();
        }
        return null;
    }

    public static String StringToInt(String str) {
        String reg = "[^0-9]";
        Pattern pattern = Pattern.compile(reg);
        return pattern.matcher(str).replaceAll("").trim();

    }
}
