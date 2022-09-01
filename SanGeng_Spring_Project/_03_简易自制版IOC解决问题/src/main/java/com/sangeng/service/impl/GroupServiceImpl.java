package com.sangeng.service.impl;

import com.sangeng.ClassPathXmlApplicationContext;
import com.sangeng.dao.UserDao;
import com.sangeng.dao.impl.UserDaoImpl1;
import com.sangeng.service.GroupService;

public class GroupServiceImpl implements GroupService {

    UserDao userDao  = (UserDao) new ClassPathXmlApplicationContext("beans.xml").getBean("userDao");

    @Override
    public void showUser() {
       ;
        System.out.println(userDao.getUserById(1));
    }

    @Override
    public void showUser2() {
        System.out.println(userDao.getUserById(1));
    }

    @Override
    public void showUser3() {
        System.out.println(userDao.getUserNameById(1));
    }
}
