package com.junduo.controller;

import com.alibaba.druid.util.StringUtils;
import com.junduo.mapper.UserMapper;
import com.junduo.pojo.User;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Collection;
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

    static Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/all")
    @ResponseBody
    public List<User> queryUserList(){
        List<User> userList = userMapper.queryUserList();
        return userList;
    }

    /**
     * 用户登录
     * @param username  用户名
     * @param password  用户密码
     * @param model     用户的数据模型
     * @param session   用户的session
     * @return  返回前端页面
     */
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

    /**
     * 登录的用户注销
     * @param session   用户的session
     * @return  返回前端页面
     */
    @RequestMapping("/user/out")
    public String signOut(HttpSession session){
        session.invalidate();
        return "redirect:/index.html";
    }

    /**
     * 查询所有管理人员的名单
     * @param model
     * @return
     */
    @RequestMapping("/users")
    public String list(Model model){
        Collection<User> userList = userMapper.queryUserList();
        model.addAttribute("users", userList);
        return "user/list";
    }


    @RequestMapping("/user/queryUser")
    public String searchUser(String searchMsg, Model model){
        Collection<User> users = new ArrayList<User>();
        if(searchMsg.isEmpty()){
            return "/user/blank";
        }
        String[] stringArray = searchMsg.split("\\s+");
        for(String string : stringArray){
            if(StringUtils.isNumber(string)){
                Integer number = Integer.parseInt(string);
                User user = userMapper.queryUserById(number);
                if(user==null){
                    continue;
                }
                users.add(user);
            }else {
                User user = userMapper.queryUserByName(string);
                if(user==null){
                    continue;
                }
                users.add(user);
            }
        }
        if(users.isEmpty()){
            return "/user/blank";
        }
        model.addAttribute("users", users);
        return "user/search";
    }

    /**
     * 转发到添加用户的前端页面
     * @return  返回前端界面
     */
    @GetMapping("/user")
    public String toAddPage(){
        return "user/add";
    }

    @PostMapping("/user")
    public String addUser(User user, Model model){
        try{
            userMapper.addUser(user);
        }catch (Exception e){
            model.addAttribute("msg", "用户名已被使用");
            return "user/add";
        }
        return "redirect:/users";
    }


    /**
     * 转发到用户编辑页面
     * @param id    用户id
     * @param model 用户的数据模型
     * @return  返回前端页面
     */
    @GetMapping("/user/{id}")
    public String toUpdateUser(@PathVariable Integer id, Model model){
        User user = userMapper.queryUserById(id);
        model.addAttribute("user", user);
        return "user/update";
    }

    /**
     * 对用户信息进行修改
     * @param user
     * @return
     */
    @PostMapping("/updateUser")
    public String updateUser(User user){
        userMapper.updateUser(user);
        return "redirect:/users";
    }

    /**
     * 删除用户信息
     * @param id    用户的id
     * @return  重定向到/users链接
     */
    @RequestMapping("delUser/{id}")
    public String deleteUser(@PathVariable Integer id){
        userMapper.deleteUser(id);
        return "redirect:/users";
    }


}
