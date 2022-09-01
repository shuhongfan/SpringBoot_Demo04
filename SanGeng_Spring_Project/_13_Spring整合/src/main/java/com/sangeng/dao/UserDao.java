package com.sangeng.dao;

import com.sangeng.domain.User;

import java.util.List;

public interface UserDao {
    List<User> findAll();
}
