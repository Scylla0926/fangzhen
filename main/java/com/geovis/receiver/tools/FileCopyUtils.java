package com.geovis.receiver.tools;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * 类描述:
 * @author yxl
 * @Date: 10:01 2020/9/19
 *
 * 浏览器:chrome/
 *
 * 数据库:mysql
 *
 * jdk版本:1.8
 *
 * 开发使用的软件:IDEA
 *
 * 代码统计行:Statistics
 *
 * 数据库操作软件:navicat 15
 *
 * 指标体系控制器：
 *  其中包含了
 *      添加指标体系
 *      删除指标体系
 *      查询指标体系
 *      根据id查询指标体系
 *
 */
@Component
public class FileCopyUtils {

	/**
	 *方法描述:
     * 文件拷贝
	 * @param: [srcFile, destFile, resMsg]
	 * @author: yangxl
	 * @Date: 2020/11/4 22:03
	 * @Return boolean
	 *
	 */
	public static boolean fileCopy(File srcFile, File destFile, String resMsg){
		if(srcFile.exists()){
			File destParentFile = destFile.getParentFile();
			if(!destParentFile.exists()){
				destParentFile.mkdirs();
			}
			try {
				if(srcFile.renameTo(srcFile)){
					FileUtils.copyFile(srcFile,destFile);
				}else{
					return false;
				}
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}

		}else{
			return false;
		}
	}


}
