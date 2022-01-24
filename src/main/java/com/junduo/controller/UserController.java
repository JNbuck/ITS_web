package com.junduo.controller;

import com.junduo.mapper.UserMapper;
import com.junduo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName UserController
 * Description
 * Create by JBuck
 * Date 2022/1/24 19:54
 * @author JBuck
 */
@RestController
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/all")
    public List<User> queryUserList(){
        List<User> userList = userMapper.queryUserList();
        return userList;
    }

}
