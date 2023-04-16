package com.dafuweng.config.security;

import com.dafuweng.config.security.filter.CheckTokenFilter;
import com.dafuweng.config.security.handler.AnonymousAuthenticationHandler;
import com.dafuweng.config.security.handler.CustomerAccessDeniedHandler;
import com.dafuweng.config.security.handler.LoginFailureHandler;
import com.dafuweng.config.security.handler.LoginSuccessHandler;
import com.dafuweng.config.security.service.CustomerUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private CustomerUserDetailsService customerUserDetailsService; // 自定义用户细节服务

    @Resource
    private LoginSuccessHandler loginSuccessHandler;

    @Resource
    private LoginFailureHandler loginFailureHandler;

    @Resource
    private AnonymousAuthenticationHandler anonymousAuthenticationHandler;

    @Resource
    private CustomerAccessDeniedHandler customerAccessDeniedHandler;

    @Resource
    private CheckTokenFilter checkTokenFilter; // token过滤器

    /**
     * 注入加密处理
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 登陆前进行过滤
        http.addFilterBefore(checkTokenFilter, UsernamePasswordAuthenticationFilter.class);
        System.out.println(checkTokenFilter.getLoginUrl());
        // 登录处理
        http.formLogin()
                .loginProcessingUrl("/api/user/login") // 登录的url
                .successHandler(loginSuccessHandler) // 成功处理器
                .failureHandler(loginFailureHandler); // 失败处理器
        http.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/user/login")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(anonymousAuthenticationHandler) // 匿名处理器
                .accessDeniedHandler(customerAccessDeniedHandler) // 自定义处理器
                .and()
                .cors(); // 跨域配置
    }

    /**
     * 配置认证处理器
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // System.out.println(customerUserDetailsService.loadUserByUsername("admin"));
        auth.userDetailsService(customerUserDetailsService).passwordEncoder(passwordEncoder());
    }
}
