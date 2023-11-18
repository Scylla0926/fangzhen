package com.geovis.receiver.pojo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/***
 * @Package: com.geovis.receiver.pojo
 * @ClassName: ConfigElement
 * @author     ：yangxl
 * @date       ：Created in 2022/7/27 22:28
 * @description：ConfigXml配置文件类
 * @modified By：
 * @version:
 */
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class ConfigElement {

    private String eleName;

    private String eleType;

    private String cycleType;

    private String cycleNum;

    private String resource;

    private String endWith;

    private String recClass;

    private String isCache;

    private String cname;

    private String ip;

    private String port;

    private String backupFile;

    private String dataResourceDir;

    private String dataCacheDir;

    private String dataStorageDir;

    private List<String> regexList;

    private String totalNum;
}
