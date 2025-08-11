package com.gongxiang.zhenxuan.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 用户服务启动类
 *
 * @author gongxiang-zhenxuan
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.gongxiang.zhenxuan"})
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}