package com.geovis.receiver.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author liusong
 * @Date 2022/10/27 19:38
 * @Description
 **/
@Slf4j
@Component
public class ConnectionCloseListener implements ApplicationListener<ContextClosedEvent> {
    /**
     * 存储全部的数据库链接
     */
    public static CopyOnWriteArrayList<Connection> sqlLiteConnectionList = new CopyOnWriteArrayList<>();

    /**
     * 监听系统关闭
     *
     * @param contextClosedEvent
     */
    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        log.info("监听到系统关闭,开始关闭未关闭的数据库链接 {} 个!", sqlLiteConnectionList.size());
        for (Connection connection : sqlLiteConnectionList) {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        log.info("关闭未关闭的数据库链接成功!");
    }
}
