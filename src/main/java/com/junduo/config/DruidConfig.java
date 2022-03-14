package com.junduo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName DruidConfig
 * Description : 配置Druid
 * Create by JBuck
 * Date 2022/1/23 18:24
 * @author JBuck
 */

/**
 * 相当于学SSM时候的一个个配置文件.xml
 */
@Configuration
public class DruidConfig {

    /**
     * 将写于application.yaml的配置内容，注入到Druid中，因此需要注册数据源成为组件，并且导入参数
     * @return
     */
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druidSource(){
        return new DruidDataSource();
    }

    /**
     * 后台监视，死代码
     * 相当于 web.xml
     * 因为 SpringBoot内置了servlet容器，所以没有web.xml，替代方法 ServletRegistrationBean
     * @return
     */
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");

        // 后台需要有人登录，账号密码设置
        HashMap<String, String> initParameters = new HashMap<>();
        //-----------增加配置-------------

        //登录key 是固定的 loginUsername loginPassword
        initParameters.put("loginUsername","admin");
        initParameters.put("loginPassword","991229");

        // 允许谁可以访问
        initParameters.put("allow","localhost");

        // 禁止谁能访问 initParameters.put("junduo","192.168.11.123");

        //设置初始化参数
        bean.setInitParameters(initParameters);
        return bean;
    }


    /**
     * 配置 Druid 监控 之  web 监控的 filter
     *  WebStatFilter：用于配置Web和Druid数据源之间的管理关联监控统计
     * @return
     */
    @Bean
    public FilterRegistrationBean webStatFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());

        //exclusions：设置哪些请求进行过滤排除掉，从而不进行统计
        Map<String, String> initParams = new HashMap<>();
        // 配置的路径不进行统计
        initParams.put("exclusions", "*.js,*.css,/druid/*,/jdbc/*");
        bean.setInitParameters(initParams);

        //"/*" 表示过滤所有请求
        bean.setUrlPatterns(Arrays.asList("/*"));
        return bean;
    }
}
