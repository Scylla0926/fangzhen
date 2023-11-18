package com.geovis.receiver.tools;

import com.geovis.receiver.factory.ReceiverInit;
import com.geovis.receiver.pojo.model.Note;
import com.geovis.receiver.pojo.model.ThermocLine;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

/***
 * 类描述:
 * @author yxl
 * @Date: 10:01 2020/9/19
 */
@Component
public class ReadConfig {
	 
	public ReadConfig(){}
	
	/**
	 * 读取配置文件
	 * @param key key
	 * @param fileName 配置文件名称
	 * @return
	 */
	public String readProp(String key, String fileName) {
		InputStream fileinputstream = null;
		Properties dbProps = new Properties();
		try {
			fileinputstream = ReadConfig.class.getClassLoader().getResourceAsStream(fileName.split("\\.")[0] + ".properties");
			dbProps.load(new InputStreamReader(fileinputstream,"UTF-8"));
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if(fileinputstream!=null){
				try {
					fileinputstream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return dbProps.getProperty(key);
	}

	/***
	 *方法描述:
     * 根据ip获取数据库中的信息
	 * @param: [cnfx, ip]
	 * @author: yangxl
	 * @Date: 2022/7/27 23:45
	 * @Return java.lang.String
	 */
    public String readTable(String key, String ip) {
        List<Note> dataList =  ReceiverInit.ns;
        String result = "";
        for (Note note : dataList){
            if(note.getIp().equals(ip) && note.getCnfKey().equalsIgnoreCase(key)){
                result = note.getCnfValue();
                break;
            }
        }
        return result;
    }

	/**
	 * 读取跃层解释应用阈值表
	 * @param key
	 * @return
	 */
	public  String readConfigTable(String key) {
		List<ThermocLine> dataList =  ReceiverInit.thermocLineList;
		String result = "";
		for (ThermocLine thermocLine : dataList){
			if(thermocLine.getKey().equalsIgnoreCase(key)){
				result = thermocLine.getValue();
				break;
			}
		}
		return result;
	}
}