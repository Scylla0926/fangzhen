package com.geovis.receiver.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 读取json文件
 */
@Component
public class readJson {
	
	public int readJsonForKeyToInt(File jsonFile,String key){
		int res = 0;
		try {
			String jsonString = FileUtils.readFileToString(jsonFile);
			JSONObject jsonObject = JSONObject.parseObject(jsonString);
			res = jsonObject.getInteger(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public float readJsonForKeyToFloat(File jsonFile,String key){
		float res = 0;
		try {
			String jsonString = FileUtils.readFileToString(jsonFile);
			JSONObject jsonObject = JSONObject.parseObject(jsonString);
			res = jsonObject.getFloat(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public String readJsonForKeyToString(File jsonFile,String key){
		String res = "";
		try {
			String jsonString = FileUtils.readFileToString(jsonFile);
			Object json = JSON.parse(jsonString);
			JSONObject jsonObject = null;
			if(json instanceof JSONArray){
				jsonObject =(JSONObject) ((JSONArray) json).get(0);
			}else if(json instanceof JSONObject){
				jsonObject = (JSONObject)json;
			}else{
				// 应该抛出特定异常
				jsonObject = (JSONObject)json;
			}
			res = jsonObject.getString(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public String readJsonForLonLat(File jsonFile){
		String res = "";
		try {
			String jsonString = FileUtils.readFileToString(jsonFile);
			Object json = JSON.parse(jsonString);
			JSONObject jsonObject = null;
			if(json instanceof JSONArray){
				jsonObject =(JSONObject) ((JSONArray) json).get(0);
			}else if(json instanceof JSONObject){
				jsonObject = (JSONObject)json;
			}else{
				// 应该抛出特定异常
				jsonObject = (JSONObject)json;
			}
			res = jsonObject.getString("lonmin")+","+jsonObject.getString("lonmax")+","+
					jsonObject.getString("latmin")+","+jsonObject.getString("latmax");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
}