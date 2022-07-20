package com.angeya.bs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author: Angeya
 * @Date: 2021/8/5 9:40
 */

@EnableAsync // 允许异步调用
@SpringBootApplication
//@MapperScan("com.angeya.bs.mapper")
@MapperScan()
public class ServerApp {
    public static void main(String[] args) {
        SpringApplication.run(ServerApp.class, args);
    }
}
