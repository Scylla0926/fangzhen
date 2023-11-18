package com.geovis.receiver.tools;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;

public class ReadDXZSKXtxt {

    public static void main(String[] args)  {

        String source="C:\\Users\\Administrator\\Desktop\\a.out";
        File file1=new File(source);
        JSONArray jsonArray = readStringForP (file1);
        System.out.println(JSONObject.toJSONString(jsonArray));
    }

    private static JSONArray readStringForP(File file1) {
        BufferedReader br = null;
        String info = "";
        String last = "";
        String km = "";
        JSONArray jsonArray = new JSONArray();
        try {
            br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file1.getAbsolutePath()), "utf-8"));
            while((info = (br.readLine())) != null) {
                if(info.contains("range") == true && info.substring(0, 5).equals("range") == true) {
                    km = info.split("\\s+")[4];
                }

                if(info.split("\\s+").length==4) {
                    last = info;
                    if(last.length() == 30) {
                        String[] dat = last.split("\\s+");
                        dat[2] = dat[1];
                        dat[1] = km;
                        jsonArray.add(dat);
                    }
                }
            }

        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
