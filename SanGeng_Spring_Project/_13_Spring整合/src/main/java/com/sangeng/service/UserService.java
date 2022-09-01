package com.sangeng.service;

import com.sangeng.domain.User;

import java.util.List;

public interface UserService {

    public List<User> findAll();

    public void findById(Integer id);
}
