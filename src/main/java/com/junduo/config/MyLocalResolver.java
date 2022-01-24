package com.junduo.config;

import org.springframework.web.servlet.LocaleResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * ClassName MyLocalResolver
 * Description ： 继承LocaleResolver，用于监测访问是否包含l参数，用于切换网站的语言信息
 * Create by JBuck
 * Date 2022/1/23 17:16
 * @author JBuck
 */
public class MyLocalResolver implements LocaleResolver {

    // 解析请求

    @Override
    public Locale resolveLocale(HttpServletRequest request) {

        // 获取请求中的语言参数
        String language = request.getParameter("l");
        //如果没有就使用默认的
        Locale locale = Locale.getDefault();
        // 如果请求的链接携带了地区化的参数
        if(! StringUtils.isEmpty(language)){
            //zh_CN
            String[] split = language.split("_");
            // 国家，地区
            locale = new Locale(split[0],split[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}
