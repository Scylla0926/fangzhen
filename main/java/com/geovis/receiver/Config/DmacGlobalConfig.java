package com.geovis.receiver.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/***
 * 类描述:解决跨域问题
 * @author yxl
 * @Date: 10:01 2020/9/19
 */
@Configuration
public class DmacGlobalConfig implements WebMvcConfigurer {


    /***
     *方法描述: 跨域访问允许通过的请求方式。
     * @param: [registry]
     * @author: yangxl
     * @Date: 2022/7/27 20:28
     * @Return void
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowCredentials(true)
                .maxAge(3600).allowedHeaders("*");
    }
}
