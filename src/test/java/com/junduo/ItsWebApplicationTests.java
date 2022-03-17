package com.junduo;

import com.junduo.mapper.DevicesMapper;
import com.junduo.mapper.UserMapper;
import com.junduo.pojo.User;
import com.junduo.server.WebSocketServer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItsWebApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test1(){
        try{
            User user = new User(6, "junduoxv", "123");
            userMapper.addUser(user);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
