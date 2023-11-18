package com.geovis.receiver.pojo.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
/***
 * 类描述:
 * 生产业务流水实体
 * @author yxl
 * @Date: 10:01 2020/9/19
 */

@Data
@Component
public class Produce {

	// 属性信息
    // 文件实体
	private File file;
    // 生命周期
	private long lifetime;
    // 流水日志
	private StringBuilder info;
    // 异常日志
	private StringBuilder error;

    //文件的缓存相对路径
	private String dataCachePath;
    //专线中文件的绝对路径
	private String srcFilePath;
	private List<String> regx;

	private List<Product> productList;

	private String chooseRegex;
	// 入库信息集合
	private List<Object> objectList;

	public Produce(){

	}
	public Produce(File file) {
		this.info = new StringBuilder();
		this.error = new StringBuilder();
		this.file = file;
		this.lifetime = System.currentTimeMillis();
	}

	/**
	 * 添加业务作业中的流水日志
	 *
	 * @param info
	 *            日志信息
	 */
	public void addInfo(String info) {
		this.info.append(info).append("\n");
	}

	/**
	 * 添加业务作业中的异常日志
	 *
	 * @param error
	 *            日志信息
	 */
	public void addError(String error) {
		this.error.append(error).append("\n");
	}

}