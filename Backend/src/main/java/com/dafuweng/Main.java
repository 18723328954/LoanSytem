package com.dafuweng;

import com.dafuweng.util.IPUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.SocketException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


/**
 * @program: UserMapper
 *
 * @description: Main class of springboot application.
 *
 * @author: Zhou.Y.M
 *
 * @create: 2023-02-27 20:19
 **/
@MapperScan("com.dafuweng.dao")
@SpringBootApplication
public class Main {
    public static void main(String[] args) throws SocketException {
        String ip = IPUtil.getLocalIp4Address().get().toString().replaceAll("/", "");
        System.out.println(IPUtil.getCityInfo("112.79.58.181"));
        SpringApplication.run(Main.class,args);
    }
}