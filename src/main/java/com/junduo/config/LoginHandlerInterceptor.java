package com.junduo.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ClassName LoginHandlerInterceptor
 * Description
 * Create by JBuck
 * Date 2022/3/16 12:07
 * @author JBuck
 */
public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 登录成功之后，服务器应该会有用户的session
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null){
            request.setAttribute("msg", "没有权限，请登录");
            request.getRequestDispatcher("/index.html").forward(request, response);
            return false;
        }else {
            return true;
        }
    }
}
