package com.sangeng;

import com.sangeng.domain.Phone;
import com.sangeng.domain.User;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Demo {
    public static void main(String[] args) {

        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        User user = (User) app.getBean("user");
        System.out.println(user);



    }
}
