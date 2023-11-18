package com.geovis.receiver.pojo.model;

import lombok.Data;
import org.springframework.stereotype.Component;

/***
 * 类描述:
 * 产品基类
 * @author yxl
 * @Date: 10:01 2020/9/19
 */
@Data
@Component
public abstract class Product {

    //流水号
	protected String id;

    public abstract String toStringAttribute();

    public abstract String toStringFormat();

    public abstract String toStringValue();
}