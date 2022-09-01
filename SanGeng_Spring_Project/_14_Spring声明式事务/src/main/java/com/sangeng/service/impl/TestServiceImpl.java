package com.sangeng.service.impl;

import com.sangeng.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestServiceImpl {
    @Autowired
    AccountService accountService;

    @Transactional
    public void test(){
        accountService.transfer(1,2,10D);
        accountService.log();
    }
}