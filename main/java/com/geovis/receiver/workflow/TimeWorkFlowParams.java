package com.geovis.receiver.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 定时 任务
 * 定时执行 调用各类算法产品 生成数据
 */
@Component
@Slf4j
public class TimeWorkFlowParams {

    @Autowired
    WorkFlowParamsValue workFlowParamsValue;


    /**
     * 定时 调用遥感融合 产品算法结果
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void timeSatFuseWorkFlow() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DAY_OF_MONTH, -1);
        date = instance.getTime();
        String fileDate = sdf.format(date).substring(0, 8);

        boolean b = workFlowParamsValue.sendPostFuseWorkFlowParams(fileDate);
        if (b) {
            log.info("定时 调用遥感融合 产品算法结果成功");
        } else {
            log.error("定时 调用遥感融合 产品算法结果失败");
        }
    }


    /**
     * 定时 调用 海流统计 产品算法
     * 调用参数  {year season thresh_in flen_crit}
     * 调用时机 {  季度 12 1 2    345     678      9 10 11
     * 参数           1季度      2季度    3季度      4季度    }
     */
    @Scheduled(cron = "0 30 0 1 3,6,9,12 ?")
    public void timeStatCurrentWorkFlow() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DAY_OF_MONTH, -1);
        date = instance.getTime();
        String year = sdf.format(date).substring(0, 4);
        String month = sdf.format(date).substring(4, 6);
        String season = "";
        switch (month) {
            case "2":
                season = "1";
                break;
            case "5":
                season = "2";
                break;
            case "8":
                season = "3";
                break;
            case "11":
                season = "4";
                break;
        }

        boolean b = workFlowParamsValue.sendPostStatCurrentWorkFlowParams(year, season);
        if (b) {
            log.info("定时 海流统计 产品算法结果成功");
        } else {
            log.error("定时 海流统计 产品算法结果失败");
        }
    }


    /**
     * 定时调用 跃层统计  海洋锋统计  中尺度涡统计  产品算法
     * 调用参数 { year mon }
     * 调用时机 每月 1号 执行 上月统计产品
     */
    @Scheduled(cron = "0 30 0 1 * ?")
    public void timeStatWorkFlow() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DAY_OF_MONTH, -1);
        date = instance.getTime();
        String year = sdf.format(date).substring(0, 4);
        String month = sdf.format(date).substring(4, 6);
        boolean b = workFlowParamsValue.sendPostStatWorkFlowParams(year, month);
        if (b) {
            log.info("定时 统计通用算法 产品算法结果成功");
        } else {
            log.error("定时 统计通用算法 产品算法结果失败");
        }
    }

//    /**
//     * 定时调用 海洋锋统计 产品算法
//     * 调用参数 { year mon }
//     * 调用时机 每月 1号 执行 上月统计产品
//     */
//    @Scheduled(cron = "0 30 0 1 * ?")
//    public void timeStatFrontWorkFlow() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//        Date date = new Date();
//        Calendar instance = Calendar.getInstance();
//        instance.setTime(date);
//        instance.add(Calendar.DAY_OF_MONTH, -1);
//        date = instance.getTime();
//        String year = sdf.format(date).substring(0, 4);
//        String month = sdf.format(date).substring(4, 6);
//        boolean b = workFlowParamsValue.sendPostStatWorkFlowParams(year, month);
//        if (b) {
//            log.info("定时 海洋锋统计 产品算法结果成功");
//        } else {
//            log.error("定时 海洋锋统计 产品算法结果失败");
//        }
//    }

}
