package com.geovis.receiver.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * 格式化文件名称类
 */
@Component
public class CleanNameTools {

	@Autowired
	private ReadConfig rc;

	/**
	 * 将文件命名统一为短名
	 * @param fn
	 * @param regx
	 * @param eleName
	 * @return
	 */
	public String cleanSatName(String fn,String regx,String eleName){

		Pattern regxFile = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
		Matcher match = regxFile.matcher(fn);
		if (match.find()) {
			String out = rc.readProp("OUT_"+eleName,"satTable_hj1289");
			if(out!=null && !out.isEmpty()){
				if(regx.contains("<SATE>")){
					out = out.replaceAll("SATE", match.group("SATE"));
					out = out.replaceAll("H08","Himawari8");
					out = out.replaceAll("AHI8","Himawari8");
				}

				if(regx.contains("<SENSOR>")){
					out = out.replaceAll("SENSOR", match.group("SENSOR"));
				}

				if(regx.contains("<GEO>")){
					out = out.replaceAll("GEO", "GEO");
				}

				if(regx.contains("<THOU>")){
					out = out.replaceAll("GEO", "R1000M");
				}

				if(regx.contains("<TID>")){
					String tid = match.group("TID");
					out = out.replaceAll("TID",tid);
				}

				String ymd = match.group("YEAR")+match.group("MONTH")+match.group("DAY");
				out = out.replaceAll("yyyyMMdd",ymd);

				String ss = "00";
				if(regx.contains("<SECOND>")){
					ss = match.group("SECOND");
				}

				String hms = match.group("HOUR")+match.group("MINUTE")+ss;
				out = out.replaceAll("HHmmss",hms);

				if(regx.contains("<PID>")){
					String pid = match.group("PID");
					if(pid!=null){
						out = out.replaceAll("PID",pid);
					}
				}
				fn = out;
			}
		}
		return fn;
	}

}
