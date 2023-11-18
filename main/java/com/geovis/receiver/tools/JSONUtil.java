package com.geovis.receiver.tools;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geovis.receiver.pojo.bean.BaseBean;

/**
 *  jsonObject 操作工具类
 */
public class JSONUtil {
	public static JSONObject json(BaseBean pg, List<String> outPath)
	{
		JSONObject json = new JSONObject();
		json.put("logs", new JSONArray());
		JSONArray pathes = new JSONArray();
		for(String path : outPath)
		{
			pathes.add(path);
		}
		json.put("path", pathes);
		JSONObject info = JSONObject.parseObject(JSONObject.toJSONString(pg));
		json.put("info", info);
		
		return json;
	}
	
	public static void addLogs(JSONObject json, List<String> logsList)
	{
		JSONArray logs = (JSONArray) json.get("logs");
		for(String log : logsList)
		{
			logs.add(log);
		}
	}
}
