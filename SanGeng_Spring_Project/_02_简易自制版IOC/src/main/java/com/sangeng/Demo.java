package com.sangeng;

import com.sangeng.dao.UserDao;

public class Demo {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("beans.xml");
        UserDao userDao = (UserDao) classPathXmlApplicationContext.getBean("userDao");

        System.out.println(userDao.getUserNameById(1));

//        UserDao userDao = new UserDaoImpl1();
    }
}
