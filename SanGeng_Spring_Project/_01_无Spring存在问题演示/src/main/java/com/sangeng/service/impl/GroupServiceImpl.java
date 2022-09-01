package com.sangeng.service.impl;

import com.sangeng.dao.UserDao;
import com.sangeng.dao.impl.UserDaoImpl1;
import com.sangeng.dao.impl.UserDaoImpl2;
import com.sangeng.service.GroupService;

public class GroupServiceImpl implements GroupService {

    UserDao userDao  = new UserDaoImpl2();

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
