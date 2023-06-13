package com.jktime.admin;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.jktime.framework.mapper")
@ComponentScan(basePackages = {"com.jktime.admin", "com.jktime.framework"})
public class JKAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(JKAdminApplication.class,args);
    }
}
