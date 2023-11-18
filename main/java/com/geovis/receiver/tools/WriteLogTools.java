package com.geovis.receiver.tools;

import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class WriteLogTools {

	public void writeScoreDetail(File scoreDetailFile,String type,String detailStr){
		StringBuffer strB = new StringBuffer();
		String str = "";
		FileReader fr = null;
		BufferedReader br = null;
		FileWriter fw = null;
		try {
			fr = new FileReader(scoreDetailFile);
			br = new BufferedReader(fr);
			while((str = br.readLine()) != null){
				if(str.contains(type)){
					strB.append("\r\n"+str);
					strB.append(detailStr);
				}else{
					strB.append("\r\n"+str+"\r\n");
				}
			}
			fw = new FileWriter(scoreDetailFile);
			String value = strB.toString();
			value = value.replaceAll("(\r?\n(\\s*\r?\n)+)","\r\n");
			fw.write(value);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(fw != null){
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fr != null){
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
