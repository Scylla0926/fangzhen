package com.geovis.receiver.pojo.model;

import lombok.Data;
import org.springframework.stereotype.Component;
/***
 * 类描述:
 * 用于记录并向DMS传递处理信息的类
 * @author yxl
 * @Date: 10:01 2020/9/19
 */

@Data
@Component
public class ProInfo {

	private String filename;
	private String stime;
	private String etime;
	private String status;

}