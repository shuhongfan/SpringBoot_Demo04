package com.sangeng.dao;

import com.sangeng.domain.User;

public interface UserDao {

    String getUserNameById(Integer id);

    User getUserById(Integer id);

}
