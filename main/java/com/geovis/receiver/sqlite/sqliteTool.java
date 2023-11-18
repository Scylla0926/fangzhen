package com.geovis.receiver.sqlite;


import com.geovis.receiver.Config.SpringBeanLoader;
import com.geovis.receiver.listener.ConnectionCloseListener;

import java.sql.Connection;
import java.sql.DriverManager;
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

/**
 * sqlite工具
 * 
 * @author sunm
 */
public class sqliteTool{
	
//	private static sqliteTool sqlite = null;
//	
//	private sqliteTool(){}
//	
//	public static sqliteTool getInstance(){
//		if(sqlite != null){
//			sqlite = new sqliteTool();
//		}
//		return sqlite;
//	}

	public static synchronized Connection getConnection(String filename){
		Connection c =null;
		try{
			Class.forName("org.sqlite.JDBC");
			c =DriverManager.getConnection("jdbc:sqlite:"+filename);
			c.setAutoCommit(false);
			//if(SpringBeanLoader.applicationContext.containsBean("connectionCloseListener")) {
				// 增加到缓存中
				ConnectionCloseListener closeListener = SpringBeanLoader.applicationContext.getBean(ConnectionCloseListener.class);
				closeListener.sqlLiteConnectionList.add(c);
			//}
		}catch(Exception e ){
			e.printStackTrace();
			System.err.println("连接:"+filename+"出错了！");
//			writeLogTools.backupInerted(e.getMessage()+"\n建立连接时出错");
		}
		return c;
	}
	
	public static boolean closeConnection(Connection con){
		try {
			con.close();

			// 增加到缓存中
			ConnectionCloseListener closeListener = SpringBeanLoader.applicationContext.getBean(ConnectionCloseListener.class);
			if(closeListener.sqlLiteConnectionList.contains(con)){
				closeListener.sqlLiteConnectionList.remove(con);
			}
			if(!con.isClosed() || con.isReadOnly()){
//				writeLogTools.backupInerted("\n"+con.getCatalog()+"\n未成功释放");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
//			writeLogTools.backupInerted("\n"+e.getMessage()+"\n未成功释放");
			return false;
		}
		return true;
	}


}