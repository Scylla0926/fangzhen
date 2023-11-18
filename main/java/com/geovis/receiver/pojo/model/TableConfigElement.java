package com.geovis.receiver.pojo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author liusong
 * @Date 2023/2/7 10:42
 * @Description
 **/
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class TableConfigElement {
    private String eleName;

    private String eleType;

    private String cycleType;

    private String cycleNum;

    private String resource;

    private String endWith;

    private String eleClass;

    private String isCache;

    private String cname;

    private String ip;

    private String port;

    private String backupFile;

    private String ResourceDir;

    private String CacheDir;

    private String StorageDir;

    private String regex;

    private String totalNum;
}
