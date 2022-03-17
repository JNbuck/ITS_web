package com.junduo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

/**
 * ClassName WebMvcConfigurer
 * Description : 原SpringMVC配置，先迁移到Springboot以配置类的形式存在
 * Create by JBuck
 * Date 2022/1/23 16:47
 * @author JBuck
 */
@Configuration
public class WebMvcConfigurer implements org.springframework.web.servlet.config.annotation.WebMvcConfigurer {

    /**
     * 配置基础的访问映射，用于一般内容的展示
     * @param registry 视图控制中心
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
        registry.addViewController("/main.html").setViewName("dashboard");
    }

    /**
     * 配置编写好的语言控制类为组件，使其在springboot中生效
     * @return
     */
    @Bean
    public LocaleResolver localeResolver(){
        return new MyLocalResolver();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LoginHandlerInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns("/index.html","/","/user/login","/css/*","/img/*","/js/*");
    }
}
