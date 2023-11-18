package com.geovis.receiver.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class GetRealStation {

    @Autowired
    private ReadConfig rc;

    /***
     *方法描述:
     * @param: [id, year, month]
     * @author: yxl
     * @Date: 12:54 2020/9/12
     * @Return java.lang.String
     * 查询对应的实况台站信息
     */
    public String getStation(String id,String year,String month){
        String station = "";
        String filePath = rc.readProp("snDir", "config")+"/"+"sn"+year+month+".txt";
        InputStream fileinputstream = null;
        InputStreamReader ir = null;
        BufferedReader fr = null;
        try{
            fileinputstream = new FileInputStream(new File(filePath));
            ir = new InputStreamReader(fileinputstream);
            fr = new BufferedReader(ir);
            String str = "";
            while((str = fr.readLine()) != null) {
                String[] strs = str.split("\\s+");

                if (strs[0].contains(id)) {
                    station = strs[1];
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                fr.close();
                ir.close();
                fileinputstream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return station;
    }
}
