package com.sangeng.dao.impl;

import com.sangeng.dao.UserDao;
import com.sangeng.domain.User;

public class UserDaoImpl1 implements UserDao {
    @Override
    public String getUserNameById(Integer id) {
        return "ζε1";
    }

    @Override
    public User getUserById(Integer id) {
        return new User(1,"εδΊ¬1",27);
    }
}
