package com.sangeng;

import com.sangeng.dao.UserDao;
import com.sangeng.domain.Phone;
import com.sangeng.service.UserService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Demo {
    public static void main(String[] args) {
        //创建容器
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        //获取对象
//        UserDao userDao = (UserDao) app.getBean("userDao");
        Phone phone = (Phone) app.getBean("phone");
        UserService userService = (UserService) app.getBean("userService");
        System.out.println(phone);
        System.out.println(userService);
//        System.out.println(userDao);
    }
}
