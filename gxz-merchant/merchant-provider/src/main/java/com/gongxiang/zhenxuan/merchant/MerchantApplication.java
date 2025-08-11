package com.gongxiang.zhenxuan.merchant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 商户服务启动类
 *
 * @author gongxiang-zhenxuan
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.gongxiang.zhenxuan"})
public class MerchantApplication {

    public static void main(String[] args) {
        SpringApplication.run(MerchantApplication.class, args);
    }
}