package com.junduo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ClassName HelloController
 * Description
 * Create by JBuck
 * Date 2022/1/21 23:38
 */
// 热部署

@Controller
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello";
    }

    @GetMapping("/world")
    @ResponseBody
    public String world(){
        return "world";
    }
}

