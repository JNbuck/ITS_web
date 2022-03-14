package com.junduo;

import com.junduo.mapper.DevicesMapper;
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
    private DevicesMapper devicesMapper;
    private Logger logger = LoggerFactory.getLogger(ItsWebApplicationTests.class);

    @Test
    public void test1(){
        devicesMapper.putDMap("1", new WebSocketServer());
    }
}
