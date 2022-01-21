package com.junduo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// @SpringBootApplication: 标注这个类是一个springboot的应用，启动类下的所有资源被导入
@SpringBootApplication
public class ItsWebApplication {
    // 将springboot应用启动
    // springApplication类
    // run方法
    public static void main(String[] args) {
        // 该方法返回一个ConfigurableApplicationContext对象
        // 参数一：应用入口的类       参数类：命令行参数
        SpringApplication.run(ItsWebApplication.class, args);
    }

}
