package com.sangeng.dao.impl;

import com.sangeng.dao.UserDao;
import org.springframework.stereotype.Repository;

@Repository("dao")
public class UserDaoImpl implements UserDao {

    public void show() {
        System.out.println("查询数据库，展示查询到的数据");
    }
}
