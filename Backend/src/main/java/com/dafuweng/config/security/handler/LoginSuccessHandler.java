package com.dafuweng.config.security.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dafuweng.config.redis.RedisService;
import com.dafuweng.entity.User;
import com.dafuweng.util.JwtUtils;
import com.dafuweng.util.LoginResult;
import com.dafuweng.util.ResultCode;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 登录成功处理器类
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private JwtUtils jwtUtils; // jwtUtils对象

    @Resource
    private RedisService redisService; // redis服务对象

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8"); // 设置客户端响应格式编码

        User user = (User) authentication.getPrincipal(); // 获取用户登录信息

        String token = jwtUtils.generateToken(user);// 根据用户生成token
        long expirationTime = Jwts.parser().setSigningKey(jwtUtils.getSecret())// 设置签名时间
                .parseClaimsJws(token.replace("jwt_", ""))
                .getBody().getExpiration().getTime();//设置过期时间
        LoginResult loginResult = new LoginResult(user.getId(), ResultCode.SUCCESS, token, expirationTime); // 创建登录结果对象

        String result = JSON.toJSONString(loginResult, SerializerFeature.DisableCircularReferenceDetect); // post返回LoginResult对象，消除循环引用

        ServletOutputStream outputStream = response.getOutputStream(); // 获取输出流
        outputStream.write(result.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();

        String tokenKey = "token_"+token;
        redisService.set(tokenKey,token,jwtUtils.getExpiration() / 1000); //把生成的token存到redis
    }
}
