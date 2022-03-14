package com.junduo.controller;

import com.junduo.mapper.UserMapper;
import com.junduo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * ClassName UserController
 * Description: MVC中的控制类，负责对与登录有关url的处理
 * Create by JBuck
 * Date 2022/1/24 19:54
 * @author JBuck
 */

/**
 * 由于测试需要使用 @RestController将数据展示于页面上
 */
//@RestController   使得下列皆返回json字符串
@Controller
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/all")
    @ResponseBody
    public List<User> queryUserList(){
        List<User> userList = userMapper.queryUserList();
        return userList;
    }

    @RequestMapping("/user/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model,
                        HttpSession session){
        // 具体的业务
        System.out.println(username+"->"+password);
        User user = userMapper.queryUserByName(username);
        if(user!=null && user.getPassword().equals(password)){
            session.setAttribute("loginUser", username);
            return "redirect:/main.html";
        }else{
            //告诉用户，你登陆失败了
            model.addAttribute("msg","用户名或者密码错误");
            return "index";
        }
    }

    @RequestMapping("/user/out")
    public String signOut(HttpSession session){
        session.invalidate();
        return "redirect:/index.html";
    }

}
