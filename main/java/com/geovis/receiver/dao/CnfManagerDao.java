package com.geovis.receiver.dao;

import com.geovis.receiver.pojo.model.Note;
import com.geovis.receiver.pojo.model.TableConfigElement;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * @Package: com.geovis.receiver.dao
 * @ClassName: CnfManagerDao
 * @author     ：yangxl
 * @date       ：Created in 2022/7/27 23:34
 * @description：cnf 配置表数据库操作类
 * @modified By：
 * @version:
 */
@Mapper
@Repository
public interface CnfManagerDao {

    public List<Note> queryCnf();

    List<TableConfigElement> queryConfig();
    //List<ThermocLine> queryThemocLine();
}
