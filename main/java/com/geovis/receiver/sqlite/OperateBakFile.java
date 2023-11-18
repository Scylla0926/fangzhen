package com.geovis.receiver.sqlite;

import com.geovis.receiver.tools.FindFileVisitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
/***
 * 类描述:
 * sqlite数据操作
 * @author yxl
 * @Date: 10:01 2020/9/19
 */

@Slf4j
@Component
public class OperateBakFile {

	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 初始化备份文件
	 * 
	 * @return
	 */
	private boolean initSqliteFile(String dbFile) {	
		// sqlite文件绝对路径 
		File bakFile = new File(dbFile);
		
		if (bakFile.exists() && bakFile.length()>0) {
			return true;
		}
		
		Connection c = null;
		Statement stmt = null;
		try {
			File bakParentFile = bakFile.getParentFile();
			if (!bakParentFile.exists()) {
				bakParentFile.mkdirs();
			} else {
				if(bakParentFile.delete()){
					bakParentFile.mkdirs();
				}
			}
			
			c = sqliteTool.getConnection(dbFile);
			stmt = c.createStatement();
			
			String sql = "create table fileinfo "
					+ "(filename       varchar2(200),"
					+ " filesize     varchar2(50),"
					+ " filelastmodifieddate     varchar2(20),"
					+ " insertdate     varchar2(20))";
			
			stmt.executeUpdate(sql);
			String initData = "insert into fileinfo(filename,filesize,filelastmodifieddate,insertdate) " +
					"values('initfilename','initfilesize','initfilelastmodifieddate','"
					+ format.format(new Date()) + "')";
			stmt.executeUpdate(initData);
			c.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (c != null) {
				sqliteTool.closeConnection(c);
			}
		}
		return true;
	}

	public boolean operateSql(String dbFile,String sql,String opType){
		synchronized ("op_sqlite") {
			if(opType.equals("query")){
				return selectSql(dbFile,sql);
			}else if(opType.equals("commit")){
				return updateSql(dbFile,sql);
			}else if(opType.equals("init")){
				return initSqliteFile(dbFile);
			}else{
				return false;
			}
		}
	}
	
	/**
	 * 查询
	 * 
	 * @param dbFile
	 * @param sql
	 * @return
	 */
	private boolean selectSql(String dbFile, String sql) {
//		log.info("查询调试损坏db:{},sql为:{}",dbFile,sql);
		boolean flag = false;
		int count = 0;
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;

		File db = new File(dbFile);
		if (!db.exists()) {
			operateSql(dbFile, "", "init");
		}

		try {
			c = sqliteTool.getConnection(dbFile);
			stmt = c.createStatement();
			int i = 0;
			while (i < 5) {
				try {
					rs = stmt.executeQuery(sql);
					if(i != 0) {
						log.info("db文件:{},重试{}次,sql为{},成功!", dbFile, i, sql);
					}
					break;
				} catch (Exception e) {
					log.error("db文件:{},错误原因为:{},重试{}次,sql为{}!", dbFile,e.getMessage(), i, sql);
					if (c != null && !c.isClosed()) {
						sqliteTool.closeConnection(c);
					}
					c = sqliteTool.getConnection(dbFile);
					stmt = c.createStatement();
				} finally {
					i++;
				}
			}
			if (rs != null) {
				while (rs.next()) {
					count = rs.getInt("n");
				}
				if (count > 0) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		} catch (Exception e) {
			log.error("查询调试损坏db:{},sql为:{},{}", dbFile, sql, e);
			flag = true;
			return false;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					if (!stmt.isClosed()) {
						stmt.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (c != null) {
				new sqliteTool().closeConnection(c);
			}
			if (flag) {
				new File(dbFile).delete();
			}
		}
	}

	/**
	 * 更新、插入、删除
	 * 
	 * @param sql
	 * @return
	 */
	private boolean updateSql(String dbFile, String sql) {
//		log.info("修改调试损坏db:{},sql为:{}",dbFile,sql);

		File db = new File(dbFile);
		if(!db.exists()){
			operateSql(dbFile,"","init");
		}
		
		Connection c = null;
		Statement stmt = null;
		try {
			c = sqliteTool.getConnection(dbFile);
			stmt = c.createStatement();
			stmt.executeUpdate(sql);
			if(c.isReadOnly()){
				return false;
			}
			if(!db.canRead() && 
					!db.canWrite()){
				return false;
			}
			c.commit();
		} catch (Exception e) {
			log.info("修改调试损坏db:{},sql为:{},{}",dbFile,sql,e);
			try {
				c.rollback();
			} catch (SQLException eb) {
				eb.printStackTrace();
			}
			return false;
		} finally{
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (c != null) {
				sqliteTool.closeConnection(c);
			}
		}
		return true;
	}
	/**
	 * ------------------------------------------------------------------------------------
	 */
	public boolean checkSql(String dbFile,String sql,String opType){
		synchronized ("op_sqlite") {
			if(opType.equals("query")){
				return selectSql(dbFile,sql);
			}else if(opType.equals("commit")){
				return updateSql(dbFile,sql);
			}else if(opType.equals("init")){
				return initSqliteScore(dbFile);
			}else{
				return false;
			}
		}
	}
	/**
	 * 初始化备份文件
	 * 
	 * @return
	 */
	private boolean initSqliteScore(String dbFile) {	
		// sqlite文件绝对路径 
		File bakFile = new File(dbFile);
		
		if (bakFile.exists() && bakFile.length()>0) {
			return true;
		}
		
		Connection c = null;
		Statement stmt = null;
		try {
			File bakParentFile = bakFile.getParentFile();
			if (!bakParentFile.exists()) {
				bakParentFile.mkdirs();
			} else {
				if(bakParentFile.delete()){
					bakParentFile.mkdirs();
				}
			}
			
			c = sqliteTool.getConnection(dbFile);
			stmt = c.createStatement();
			
			String sql = 
			"create table ldb_dat_nwp_score " +
			"(" +
			" ID         VARCHAR2(10) not null," +
			" OPUSER     VARCHAR2(10)," +
			" OPTIME     VARCHAR2(200)," +
			" INSERTDATE DATE," +
			" TNAME      VARCHAR2(200)," +
			" ODATE      VARCHAR2(20)," +
			" CLA_S      VARCHAR2(10)," +
			" CBH_S      VARCHAR2(10)," +
			" VIS_S      VARCHAR2(10)," +
			" WD_S       VARCHAR2(10)," +
			" WS_S       VARCHAR2(10)," +
			" AT_S       VARCHAR2(10)," +
			" RAINWW_S   VARCHAR2(10)," +
			" PMWW_S     VARCHAR2(10)," +
			" WINDWW_S   VARCHAR2(10)," +
			" FC_S       VARCHAR2(10)," +
			" STATION    VARCHAR2(10) )";
			stmt.executeUpdate(sql);
			String initData = "insert into ldb_dat_nwp_score(ID,OPUSER,OPTIME,INSERTDATE,TNAME,ODATE,CLA_S,CBH_S,VIS_S,WD_S,WS_S,AT_S,RAINWW_S,PMWW_S,WINDWW_S,FC_S,STATION) " +
					"values('0','OPUSER','OPTIME','"+ format.format(new Date()) + "','TNAME','"+ "','odate','" + "','CLA_S','CBH_S','VIS_S'," +
							"'WD_S','WS_S','AT_S','RAINWW_S','PMWW_S','WINDWW_S','FC_S','STATION')";
			stmt.executeUpdate(initData);
			c.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (c != null) {
				sqliteTool.closeConnection(c);
			}
		}
		return true;
	}

	public boolean initSqlite(String dbFile,String sql) {
		// sqlite文件绝对路径
		File bakFile = new File(dbFile);

		if (bakFile.exists() && bakFile.length()>0) {
			return true;
		}

		Connection c = null;
		Statement stmt = null;
		try {
			File bakParentFile = bakFile.getParentFile();
			if (!bakParentFile.exists()) {
				bakParentFile.mkdirs();
			} else {
				if(bakParentFile.delete()){
					bakParentFile.mkdirs();
				}
			}

			c = sqliteTool.getConnection(dbFile);
			stmt = c.createStatement();

			stmt.executeUpdate(sql);
			c.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (c != null) {
				sqliteTool.closeConnection(c);
			}
		}
		return true;
	}

	/**
	 * 更新、插入、删除
	 *
	 * @param sql
	 * @return
	 */
	public boolean insertSql(String dbFile, String sql) {

		File db = new File(dbFile);

		Connection c = null;
		Statement stmt = null;
		try {
			c = sqliteTool.getConnection(dbFile);
			stmt = c.createStatement();
			stmt.executeUpdate(sql);
			if(c.isReadOnly()){
				return false;
			}
			if(!db.canRead() &&
					!db.canWrite()){
				return false;
			}
			c.commit();
		} catch (Exception e) {
			System.err.println("异常DB:\n" + dbFile);
			e.printStackTrace();
			try {
				c.rollback();
			} catch (SQLException eb) {
				eb.printStackTrace();
			}
			return false;
		} finally{
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (c != null) {
				sqliteTool.closeConnection(c);
			}
		}
		return true;
	}


//	public static void main(String[] args) {
//		long startTime = System.currentTimeMillis();
//		File dirFile = new File("D://");
//		getFile(dirFile);
//		long endTime = System.currentTimeMillis();
//		System.out.println("总耗时:"+(endTime - startTime ) / 1000);
//	}
//	public static void getFile(File dirFile){
//
//
//		FindFileVisitor visitor = new FindFileVisitor();
//		try {
//			Files.walkFileTree(dirFile.toPath(),visitor);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

//
//		File[] files = dirFile.listFiles();
//		if(files == null){
//			return;
//		}
//		for (File file : files){
//			if(file.isDirectory()){
//				getFile(file);
//			}else{
//				System.out.println(file.getAbsolutePath());
//			}
//		}
//	}
}