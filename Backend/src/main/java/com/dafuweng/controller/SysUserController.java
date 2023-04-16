package com.dafuweng.controller;

import com.dafuweng.config.redis.RedisService;
import com.dafuweng.entity.User;
import com.dafuweng.util.JwtUtils;
import com.dafuweng.util.Result;
import com.dafuweng.vo.TokenVo;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/sysUser")
public class SysUserController {
    @Resource
    private RedisService redisService;
    @Resource
    private JwtUtils jwtUtils;

    /**
     * 刷新token
     *
     * @param request
     * @return
     */
    @PostMapping("/refreshToken")
    public Result refreshToken(HttpServletRequest request) {
        //从header中获取前端提交的token
        String token = request.getHeader("token");
        //如果header中没有token，则从参数中获取
        if (ObjectUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }
        //从Spring Security上下文获取用户信息
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        //获取身份信息
        UserDetails details = (UserDetails) authentication.getPrincipal();
        //重新生成token
        String reToken = "";
        //验证原来的token是否合法
        if (jwtUtils.validateToken(token, details)) {
            //生成新的token
            reToken = jwtUtils.refreshToken(token);
        }
        //获取本次token的到期时间，交给前端做判断
        long expireTime = Jwts.parser().setSigningKey(jwtUtils.getSecret())
                .parseClaimsJws(reToken.replace("jwt_", ""))
                .getBody().getExpiration().getTime();
        //清除原来的token信息
        String oldTokenKey = "token_" + token;
        redisService.del(oldTokenKey);
        //存储新的token
        String newTokenKey = "token_" + reToken;
        redisService.set(newTokenKey, reToken, jwtUtils.getExpiration() / 1000);
        //创建TokenVo对象
        TokenVo tokenVo = new TokenVo(expireTime, reToken);
        //返回数据
        return Result.ok(tokenVo).message("token刷新成功");
    }

    /**
     * 用户退出登录
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request, HttpServletResponse response){
        // 获取token信息
        String token = request.getHeader("token");
        // 若Header中没有token信息，则从参数中获取
        if (ObjectUtils.isEmpty(token)){
            // 从参数中获取token
            token = request.getParameter("token");
        }
        // 从Spring Security上下文对象中获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 判断用户信息是否为空，不为空，则清空用户信息
        if (authentication !=null){
            // 清空用户信息
            new SecurityContextLogoutHandler().logout(request,response,authentication);
            // 清除redis缓存中的token
            redisService.del("token_"+token);
            return Result.ok().message("用户退出成功！");
        }

        return Result.ok().message("用户退出失败！");
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/getInfo")
    public Result getInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // 从Spring Security上下文中获取对象

        if (authentication == null){
            return Result.error().message("用户信息查询失败");
        }

        User user = (User) authentication.getPrincipal(); // 获取User对象
        return Result.ok(user).message("查询成功");
    }

    /**
     * 获取用户头像
     */
    @GetMapping("/getAvatar")
    public Result getAvatar(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // 从Spring Security上下文中获取对象

        if (authentication == null){
            return Result.error().message("用户信息查询失败");
        }

        User user = (User) authentication.getPrincipal(); // 获取User对象
        return Result.ok(user.getAvatar()).message("头像返回成功！");
    }
}