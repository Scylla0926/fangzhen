package com.geovis.receiver.tools;

import com.geovis.receiver.listener.ConnectionCloseListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import redis.clients.jedis.Jedis;

/**
 * @Author liusong
 * @Date 2023/1/5 20:08
 * @Description
 **/
@Configuration
public class RedisSubConfig {
    @Value("${spring.redis.host}")
    private String ip;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedisSubListener redisSubInit() {
        RedisSubListener redisSubListener = new RedisSubListener();
        new Thread(()->{
            Jedis jedis = new Jedis(ip, port);
            if (password != null && !password.equals("")) {
                jedis.auth(password);
            }
            //jedis.subscribe(redisSubListener, "THERMOCLINE");

        }).start();
        return redisSubListener;
    }
}
