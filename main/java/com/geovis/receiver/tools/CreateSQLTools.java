package com.geovis.receiver.tools;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Component
public class CreateSQLTools {



    private SimpleDateFormat dateFormatString = new SimpleDateFormat("yyyyMMddHHmmss");
    /**
     * sql语句拼接
     *
     * @param tablename 	表名称
     * @param keyString 	字段名字符串（使用(,)隔开）
     * @param formatString 	格式字符串（使用(,)隔开）
     * @param valueString 	值字符串（使用(,)隔开）
     * @param insertDate	入库时间
     * @return
     *
     * checked
     */
    public String sqlJoint(String tablename, String keyString, String formatString,
                           String valueString, StringBuilder insertDate) {

        String sql = "";
        String sqlDatePatternStr = "yyyymmddhh24miss";
        String insertDatePatternStr = "yyyymmddhh24miss";
        valueString = valueString + " ";

        try {
            String[] formats = formatString.split(",");
            String[] values = valueString.split(",");
//			if(formats.length==values.length){

            StringBuilder valueBuilder = new StringBuilder();

            for(int i=0;i<formats.length;i++){
                if(values[i]!=null && !values[i].trim().isEmpty() && !"NULL".equalsIgnoreCase(values[i].trim())){
                    values[i] = values[i].replaceAll("\\*","\\,");//NOAA与Metop卫星名，替换“，”
                    //判断数据库中该字段的类型，根据不同的类型拼接值
                    if(("VARCHAR").equalsIgnoreCase(formats[i].trim())){
                        valueBuilder.append("'").append(values[i].trim()).append("',");
                    }else if (("DATE").equalsIgnoreCase(formats[i].trim())) {
                        if(values[i].trim().length()==6){
                            sqlDatePatternStr = "yyyymm";
                        }else if (values[i].trim().length()==8) {
                            sqlDatePatternStr = "yyyymmdd";
                        }else if (values[i].trim().length()==10) {
                            sqlDatePatternStr = "yyyymmddhh24";
                        }else if (values[i].trim().length()==12) {
                            sqlDatePatternStr = "yyyymmddhh24mi";
                        }
                        valueBuilder.append("to_date('").append(values[i].trim()).append("','"+sqlDatePatternStr+"'),");
                    }else if (("NUMBER").equalsIgnoreCase(formats[i].trim())) {
                        valueBuilder.append(values[i].trim()).append(",");
                    }else if (("timestamp").equalsIgnoreCase(formats[i].trim())) {
                        if(values[i].trim().length()==6){
                            sqlDatePatternStr = "yyyymm";
                        }else if (values[i].trim().length()==8) {
                            sqlDatePatternStr = "yyyymmdd";
                        }else if (values[i].trim().length()==10) {
                            sqlDatePatternStr = "yyyymmddhh24";
                        }else if (values[i].trim().length()==12) {
                            sqlDatePatternStr = "yyyymmddhh24mi";
                        }
                        valueBuilder.append("to_timestamp('").append(values[i].trim()).append("','"+sqlDatePatternStr+"'),");
                    }
                }else{
                    valueBuilder.append("NULL,");
                }
            }

            String inDate = dateFormatString.format(new Date());
            valueBuilder.append("to_date('").append(inDate).append("','"+insertDatePatternStr+"')");

            if(tablename.contains("nwp") || tablename.contains("NWP")){
                keyString = keyString + ","+"filedate";
            }else{
                keyString = keyString + ","+"insertdate";
            }

            String valueBuilderString = valueBuilder.toString();

            sql = "INSERT INTO "+tablename+"("+keyString+") values("+valueBuilderString+")";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sql;
    }

}
