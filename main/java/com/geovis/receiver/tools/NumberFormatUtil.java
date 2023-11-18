package com.geovis.receiver.tools;

import java.text.NumberFormat;

/**
 *  科学计数法 转换工具
 */
public class NumberFormatUtil {

    /**
     * 取消科学计数法 并设置保留小数位置
     * @param num
     * @return
     */
    public static String science(float num) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        //设置保留多少为小数
        nf.setMaximumFractionDigits(5);
        //取消科学计数法
        nf.setGroupingUsed(false);
        return nf.format(num);
    }
    
    public static String scienceD(double num) {
    	NumberFormat nf = NumberFormat.getNumberInstance();
    	//设置保留多少为小数
    	nf.setMaximumFractionDigits(5);
    	//取消科学计数法
    	nf.setGroupingUsed(false);
    	
    	
    	return nf.format(num);
    }
}
