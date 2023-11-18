package com.geovis.receiver.tools;

import com.geovis.receiver.dao.ProductTableDao;
import com.geovis.receiver.pojo.bean.DataSetTableBean;
import com.geovis.receiver.pojo.model.ConfigElement;
import com.geovis.receiver.pojo.vo.DataSetVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author qhy
 * @version 1.0.0
 * @Package com.geovis.systemmanager.tool
 * @ClassName RedisUtil
 * @date 2022/7/14 14:28
 * @description TODO
 */
@Component
@Slf4j
public class RedisUtil {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 1.普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key,String value,long time){
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 2.普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 3.判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 4.普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 5.删除缓存
     * @param keys 字符串集合
     */
    @SuppressWarnings("unchecked")
    public void del(List<String> keys) {
        if (keys != null && keys.size () > 0) {
            redisTemplate.delete (keys);
        }
    }
    /**
     * 计算文件总个数
     *
     * @description:
     * @author: liusong
     * @date: 2022/9/27 10:12
     * @param: [configElement]
     * @return: int
     **/
    public int totalNum(ConfigElement configElement) {
        int totalNum = 0;
        String totalNums = configElement.getTotalNum();
        String[] total = totalNums.split(",");
        for (String numbers : total) {
            int number = Integer.parseInt(numbers.split("=")[1]);
            totalNum += number;
        }
        return totalNum;
    }



}
