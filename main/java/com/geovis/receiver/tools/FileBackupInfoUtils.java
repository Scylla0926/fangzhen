package com.geovis.receiver.tools;

import com.geovis.receiver.receiver.ReceiverBase;
import com.geovis.receiver.sqlite.OperateBakFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * 类描述:
 * @author yxl
 * @Date: 10:01 2020/9/19
 */
@Component
public class FileBackupInfoUtils {

	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private OperateBakFile operateBakFile;
	/**
	 * 判断文件是否已经处理完成
	 * @param dbName sqlite文件名称
	 * @param filename 文件名称
	 * @param filesize 文件大小
	 * @param fileLastModified 文件的最后修改时间
	 * @return
	 */
	public boolean isFileCompleted(String backupDir, String dbName, String filename, String filesize, String fileLastModified) {
		// sqlite所在位置绝对路径
		String dbFile = backupDir + dbName + ".db";
		//初始化sqlite
		if (operateBakFile.operateSql(dbFile,"","init")) {
			String selectSql = "select count(*) n from  fileinfo where filename='"
					+ filename + "' and filesize='" + filesize
					+ "' and filelastmodifieddate='" + fileLastModified + "';";

			if (operateBakFile.operateSql(dbFile, selectSql,"query")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 将已经处理完成的文件进行记录
	 *
	 * @param backupDir sqlite文件存放的目录
	 * @param dbName sqlite 文件名称
	 * @param filename 文件名称
	 * @param filesize 文件大小
	 * @param fileLastModified 文件的最后修改时间
	 * @return
	 */
	public boolean fileCompletedBakup(String backupDir, String dbName, String filename,
									  String filesize, String fileLastModified) {
		boolean flag = false;
		String dbFile = backupDir + dbName + ".db";

		String insertSql = "insert into fileinfo(filename,filesize,filelastmodifieddate,insertdate) values('"
				+ filename
				+ "','"
				+ filesize
				+ "','"
				+ fileLastModified
				+ "','" + format.format(new Date()) + "')";

		//初始化sqlite
		if (operateBakFile.operateSql(dbFile,"","init")) {
			if(operateBakFile.operateSql(dbFile, insertSql,"commit")){
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 判断文件是否已经完成任务的入口
	 *
	 * @param backupDir sqlite文件存放的目录
	 * @param f 文件
	 * @param regexRules 文件名称匹配的正则表达式列表（用于获取时间信息）
	 * @param eleName 引接任务的唯一标识
	 * @return
	 */
	public boolean fileCompletedPattern(String backupDir, File f, List<String> regexRules, String eleName) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		//文件名称
		String filename = f.getName().trim();
		//文件的大小
		String filesize = String.valueOf(f.length());
		//文件的修改时间
		long fileLastModifiedTime = f.lastModified();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(fileLastModifiedTime);
		// 获取文件修改时间的字符串
		String fileLastModified = sdf.format(calendar.getTime());

		for(String reg : regexRules){
			Pattern filePattern = Pattern.compile(reg.trim(), Pattern.CASE_INSENSITIVE);
			Matcher matcher = filePattern.matcher(filename);
			if(matcher.find()){
				//文件日期
				String fileDateString = getFileTimeInfo(f, reg, matcher, "day");
				//记录文件是否已完成任务的sqlite文件名称
				String dbName = eleName+"_"+fileDateString;

				if (isFileCompleted(backupDir + "/" + eleName.trim() + "/",dbName,filename,filesize,fileLastModified)){
					return true;
				} else{
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 根据给定的文件、文件名称匹配的正则表达式列表、引接任务的唯一标识将已经完成任务的文件名称进行记录的入口
	 *
	 * @param f 文件
	 * @param regexRules 文件名称匹配的正则表达式列表（用于获取时间信息）
	 * @param eleName 引接任务的唯一标识
	 * @return
	 */
	public boolean fileCompletedWriter(String backupDir, File f, List<String> regexRules, String eleName) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		//文件名称
		String filename = f.getName().trim();
		//文件的大小
		String filesize = String.valueOf(f.length());
		//文件的修改时间
		long fileLastModifiedTime = f.lastModified();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(fileLastModifiedTime);
		String fileLastModified = sdf.format(calendar.getTime());

		for(String reg : regexRules){
			Pattern filePattern = Pattern.compile(reg.trim(), Pattern.CASE_INSENSITIVE);
			Matcher matcher = filePattern.matcher(filename);

			if(matcher.find()){
				//文件日期
				String fileDateString = getFileTimeInfo(f, reg, matcher, "day");
				//记录文件是否已完成任务的sqlite文件名称
				String dbName = eleName + "_" + fileDateString;

				if (fileCompletedBakup(backupDir + "/" + eleName + "/",dbName,filename,filesize,fileLastModified)){
					return true;
				}else{
					return false;
				}
			}
		}

		return false;
	}

	/**
	 * 获取文件最后修改时间信息
	 *
	 * @param format 日期格式（yyyy/MM/dd/HH/mm/ss）
	 * @param f 文件
	 * @return
	 */
	public String getFileLastModifiedTime(String format, File f) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		long fileLastModifiedTime = f.lastModified();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(fileLastModifiedTime);
		String fileLastModifiedDate = sdf.format(calendar.getTime());
		return fileLastModifiedDate;
	}

	/**
	 * 根据给定的文件和正则表达式列表，判断文件名称是否能够匹配给定的正则表达式中任意一种
	 *
	 * @param f 文件
	 * @param regexRules 正则表达式列表
	 * @return
	 */
	public boolean fileNamePattern(File f, List<String> regexRules) {
		String filename = f.getName().trim();
		for(String reg : regexRules){
			Pattern filePattern = Pattern.compile(reg.trim(), Pattern.CASE_INSENSITIVE);
			Matcher matcher = filePattern.matcher(filename);
			if(matcher.matches()){
				return true;
			}
		}
		return false;
	}
	/**
	 *
	 * FY4A-_AGRI--_N_REGI_1047E_L1C_GRA-_C011_GLL_20190918000000_20190918001459_4000M_V0001.JPG
	 */
	/**
	 * 判断文件日期是否在规定的有效期时间范围内
	 *
	 * @param  f 文件
	 * @param regexRules 文件名称匹配的正则表达式列表（用于获取时间信息）
	 * @param cycleType 时间类型(day/hour)
	 * @param cycleNum 时间数
	 * @return
	 */
	public boolean fileDatePattern(File f, List<String> regexRules, String cycleType, String cycleNum) {
		if (cycleType == null || cycleType.trim().isEmpty() || cycleNum == null || cycleNum.trim().isEmpty()) {
			return true;
		} else {
			// 获取文件相关时间信息
			String fileDateString = getFileLastModifiedTime("yyyyMMddHHmmss", f);

			if (isFileInDate(fileDateString, cycleType, cycleNum)) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 根据给定的文件时间、时间类型、时间范围数，和当前时间进行对比，判断文件是否在允许的范围内
	 *
	 * @param fileDateString 文件时间
	 * @param cycleType 时间类型（day/hour）
	 * @param cycleNum 时间数
	 * @return
	 */
	public boolean isFileInDate(String fileDateString, String cycleType, String cycleNum) {
		String format = "yyyyMMddHHmmss";
		long cycleNumInt = Integer.parseInt(cycleNum);
		long misTime = (cycleNumInt * 24 * 60 * 60 * 1000);

		if (("hour").equalsIgnoreCase(cycleType)) {
			misTime = cycleNumInt * 60 * 60 * 1000;
		}

		try {
			Date nowDate = new SimpleDateFormat(format).parse(new SimpleDateFormat(format).format(new Date()));
			Date fileDate = new SimpleDateFormat(format).parse(fileDateString);
			long nl =  nowDate.getTime() ;
			long bl =  fileDate.getTime();
			long rs = nl - bl ;
			if ((rs) < misTime) {
				return true;
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 判断文件是否在执行
	 * @param receiver
	 *
	 * @param f 文件
	 * @return
	 */
	public boolean isFileExecuting(File f, ReceiverBase receiver) {
		try {
			if(receiver!=null && receiver.getProReal()!=null && f!=null){
				return new File(receiver.getProReal().getSrcFilePath()).equals(f);
			}
		} catch (Exception e) {
			System.out.println("isFileExecuting  --!!!!--  "+f.getName());
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取文件相关时间信息
	 *
	 * @param f 文件
	 * @param reg 正则表达式
	 * @param matcher 匹配对象
	 * @param cycleType 时间类型
	 * @return
	 */
	public String getFileTimeInfo(File f, String reg, Matcher matcher, String cycleType) {
		String year = "";
		String month = "";
		String day = "";
		String hour = "";
		String yearTemp = "";

		if (reg.contains("?<YEAR>")) {
			yearTemp = matcher.group("YEAR");
			if(yearTemp != null && !yearTemp.trim().isEmpty()){
				if(yearTemp.length() == 2){
					year = "20"+yearTemp;
				}else{
					year = yearTemp;
				}
			}else{
				getFileLastModifiedTime("yyyy", f);
			}
//			year = matcher.group("YEAR") != null && !matcher.group("YEAR").trim().isEmpty() ? matcher.group("YEAR") : getFileLastModifiedTime("yyyy", f);
		} else {
			year = getFileLastModifiedTime("yyyy", f);
		}
		if (reg.contains("?<MONTH>")) {
			month = matcher.group("MONTH") != null && !matcher.group("MONTH").trim().isEmpty() ? matcher.group("MONTH") : getFileLastModifiedTime("MM", f);
		} else {
			month = getFileLastModifiedTime("MM", f);
		}

		if (reg.contains("?<DAY>")) {
			day = matcher.group("DAY") != null && !matcher.group("DAY").trim().isEmpty() ? matcher.group("DAY") : getFileLastModifiedTime("dd", f);
		} else {
			day = getFileLastModifiedTime("dd", f);
		}

		if (("hour").equalsIgnoreCase(cycleType.trim())) {
			if (reg.contains("?<HOUR>")) {
				hour = matcher.group("HOUR") != null && !matcher.group("HOUR").trim().isEmpty() ? matcher.group("HOUR") : getFileLastModifiedTime("HH", f);
			} else {
				hour = getFileLastModifiedTime("HH", f);
			}
		}

		String fileDateString = year + month + day + hour;

		fileDateString = getRealDateString(fileDateString);

		return fileDateString;
	}

	public String getFileTimeInfoFor799(File f, String reg, Matcher matcher, String cycleType) {
		String year = "";
		String month = "";

		if (reg.contains("?<YEAR>")) {
			year = matcher.group("YEAR") != null && !matcher.group("YEAR").trim().isEmpty() ? matcher.group("YEAR") : getFileLastModifiedTime("yyyy", f);
		} else {
			year = getFileLastModifiedTime("yyyy", f);
		}

		if (reg.contains("?<MONTH>")) {
			month = matcher.group("MONTH") != null && !matcher.group("MONTH").trim().isEmpty() ? matcher.group("MONTH") : getFileLastModifiedTime("MM", f);
		} else {
			month = getFileLastModifiedTime("MM", f);
		}

		String fileDateString = year + "/" + month;

		return fileDateString;
	}

	/**
	 * 检查日期是否合理并返回合理日期字符串
	 * @param fileDateString
	 * @return
	 */
	private String getRealDateString(String fileDateString) {
		Calendar calendarNow = Calendar.getInstance();
		Date dateNow = calendarNow.getTime();

		String pattern = "yyyyMMdd";
		if(fileDateString.length()==10){
			pattern = "yyyyMMddHH";

		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			Date fileDate = sdf.parse(fileDateString);
			if(fileDate.after(dateNow)){
				String resultString = "";
				Calendar calendarFile = Calendar.getInstance();
				if(pattern.length()==10){
					calendarFile.setTime(fileDate);
					calendarFile.add(Calendar.DAY_OF_MONTH, -1);
					resultString = sdf.format(calendarFile.getTime());
					return resultString;

				}else{
					calendarFile.setTime(fileDate);
					calendarFile.add(Calendar.MONTH, -1);
					resultString = sdf.format(calendarFile.getTime());
					return resultString;
				}

			}
			return fileDateString;
		} catch (ParseException e) {
			e.printStackTrace();
			return fileDateString;
		}
	}
}