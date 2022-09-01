package com.sangeng;

import com.sangeng.service.PhoneService;
import com.sangeng.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Demo {
    public static void main(String[] args) {
        //创建容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        //获取对象
//        PhoneService phoneService = applicationContext.getBean(PhoneService.class);
        UserService userService = applicationContext.getBean(UserService.class);
        //调用方法
//        phoneService.deleteAll();
//        userService.deleteAll();
//        userService.updateById(10);
//        userService.deleteAll();
        int ret = userService.updateById(2, "三更", 50000000);
        System.out.println(ret);
    }
}
