package com.sangeng;

import com.sangeng.config.ApplicationConfig;
import com.sangeng.domain.Phone;
import com.sangeng.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;

public class Demo2 {
    public static void main(String[] args) {
        //创建注解容器
        AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        UserService userService = (UserService) app.getBean("userService");

//        DataSource dataSource = (DataSource) app.getBean("dataSource");

        DataSource bean = app.getBean(DataSource.class);
        System.out.println(userService);
    }
}
