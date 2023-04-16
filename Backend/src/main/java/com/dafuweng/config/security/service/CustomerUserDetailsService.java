package com.dafuweng.config.security.service;

import com.dafuweng.entity.User;
import com.dafuweng.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CustomerUserDetailsService implements UserDetailsService {
    @Resource
    private UserService userService;

    /**
     * 根据用户名返回用户对象
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userService.findUserByUserName(username); // 根据用户名查询用户信息
        if (user == null){
            throw new UsernameNotFoundException("用户名或密码错误！");
        }
        return user;
    }
}
