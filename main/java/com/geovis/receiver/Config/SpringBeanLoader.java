package com.geovis.receiver.Config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author liusong
 * @Date 2022/10/27 19:43
 * @Description
 **/
@Component
public class SpringBeanLoader implements ApplicationContextAware {
    public static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
