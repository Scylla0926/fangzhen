package com.geovis.receiver.Config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述:
 * 解决内置tomcat 与 内置 tomcat 高版本字符集问题
 * 报错内容为:
 *      The valid characters are defined in RFC 7230 and RFC 3986
 * @author: yxl
 * @Date: 15:27  2020/9/17
 */
@Configuration
public class RfcConfig {

    /***
     *方法描述: 设置tomcat允许通过的字符
     * @param: []
     * @author: yangxl
     * @Date: 2022/7/27 20:29
     * @Return org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
     *
     */
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
            @Override
            public void customize(Connector connector) {
                connector.setProperty("relaxedQueryChars", "|{}[]/+");//允许的特殊字符
            }
        });
        return factory;
    }
}