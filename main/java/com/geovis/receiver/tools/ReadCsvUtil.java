package com.geovis.receiver.tools;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
/**
 *  读取 csv 工具类
 */
public class ReadCsvUtil {

    /**
     * 返回读到的所有csv数据
     * @param file csv路径
     * @return list中的每个String数组 是csv中每一行
     */
    public List<String[]> readCsvFile(File file) {
        List<String[]> csvList = new ArrayList<>();
        file.setReadable(true);
        file.setWritable(true);
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            br = new BufferedReader(isr);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String line = "";
        try {
            while ((line = br.readLine()) != null) {
                String[] rows = line.split(",");
                for (int i = 0; i < rows.length; i++) {
                    rows[i] = rows[i].replace(" ","");
                }
                csvList.add(rows);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return csvList;
    }
}
