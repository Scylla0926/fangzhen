package com.geovis.receiver.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/***
 * @Package: com.geovis.receiver.tools
 * @ClassName: RedisPublish
 * @author     ：yangxl
 * @date       ：Created in 2022/7/31 8:30
 * @description：redis消息发送者
 * @modified By：
 * @version:
 */
@Component
public class RedisPublish {
    @Value("${spring.redis.host}")
    private String ip;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    /***
     *方法描述:
     * @param: [channel, message]
     * @author: yangxl
     * @Date: 2022/7/31 8:32
     * @Return boolean
     * 单例模式类 向指定通道发送消息 成功发送返回true 失败返回false
     */
    public boolean sendMessage(String channel ,String message){
        Jedis jedis = new Jedis(ip,port);
        if (password!=null&&!password.equals("")){
            jedis.auth(password);
        }
        try{
            Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

            jedis.publish(channel.getBytes(),jackson2JsonRedisSerializer.serialize(message));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            jedis.close();
        }
        return true;
    }


}
