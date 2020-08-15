package com.taotao.redis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
@MapperScan("com.taotao.redis.mysql.mapper")
public class RedisTestMain {

    public static void main(String[] args) {
        SpringApplication.run(RedisTestMain.class, args);
    }
}
