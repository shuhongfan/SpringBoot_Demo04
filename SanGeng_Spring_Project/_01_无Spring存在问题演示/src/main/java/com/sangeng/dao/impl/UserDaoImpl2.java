package com.sangeng.dao.impl;

import com.sangeng.dao.UserDao;
import com.sangeng.domain.User;

public class UserDaoImpl2 implements UserDao {
    @Override
    public String getUserNameById(Integer id) {
        return "李四2";
    }

    @Override
    public User getUserById(Integer id) {
        return new User(1,"北京2",25);
    }
}
