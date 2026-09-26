package com.straw;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.straw.mapper")
public class StrawManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(StrawManagementApplication.class, args);
    }
}
