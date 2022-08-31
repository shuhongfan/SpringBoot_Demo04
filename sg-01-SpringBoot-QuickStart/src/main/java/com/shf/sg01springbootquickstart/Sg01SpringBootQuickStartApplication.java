package com.shf.sg01springbootquickstart;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.shf.sg01springbootquickstart.mapper")
@ComponentScan("com.shf.sg01springbootquickstart")
@EnableConfigurationProperties
public class Sg01SpringBootQuickStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(Sg01SpringBootQuickStartApplication.class, args);
    }

}
