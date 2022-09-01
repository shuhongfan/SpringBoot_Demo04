package com.sangeng;

import com.sangeng.service.UserService;
import com.sangeng.service.impl.UserServiceImpl;

public class Demo {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.showUser();
    }
}
