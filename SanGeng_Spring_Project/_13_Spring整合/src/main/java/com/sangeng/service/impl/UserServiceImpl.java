package com.sangeng.service.impl;

import com.sangeng.dao.UserDao;
import com.sangeng.domain.User;
import com.sangeng.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;


    public List<User> findAll() {

        return userDao.findAll();
    }

    public void findById(Integer id) {
        System.out.println("findById");
    }
}
