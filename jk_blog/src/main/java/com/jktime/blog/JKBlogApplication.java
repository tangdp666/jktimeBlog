package com.jktime.blog;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.jktime.framework.mapper")
@ComponentScan(basePackages = {"com.jktime.blog", "com.jktime.framework"})
@EnableScheduling
public class JKBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(JKBlogApplication.class,args);
    }
}
