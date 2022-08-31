package com.shf.sg02springbootstatic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Sg02SpringBootStaticApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Sg02SpringBootStaticApplication.class, args);
    }

}
