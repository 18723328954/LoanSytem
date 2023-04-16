package com.dafuweng.config.security.handler;


import com.alibaba.fastjson.JSON;
import com.dafuweng.config.security.exception.CustomerAuthenticationException;
import com.dafuweng.util.Result;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 登录失败处理器类
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    /**
     * 用户失败认证
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8"); // 设置客户端响应格式编码
        ServletOutputStream outputStream = response.getOutputStream(); // 获取输出流

        String message = null; // 提示信息
        int code = 500; // 错误编码

        if (exception instanceof AccountExpiredException) {
            message = "账户过期，登陆失败！";
        } else if (exception instanceof BadCredentialsException) {
            message = "用户名或密码错误，登陆失败！";
        } else if (exception instanceof CredentialsExpiredException) {
            message = "密码国企，登陆失败！";
        } else if (exception instanceof DisabledException) {
            message = "账户被禁用，登陆失败！";
        } else if (exception instanceof LockedException) {
            message = "账户被锁，登陆失败";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            message = "账户不存在，登陆失败";
        } else if(exception instanceof CustomerAuthenticationException){
            message = exception.getMessage();
            code = 600;
        }
        else {
            message = "登陆失败";
        }
        String result = JSON.toJSONString(Result.error().code(code).message(message));// 信息转化为JSON格式

        outputStream.write(result.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}

