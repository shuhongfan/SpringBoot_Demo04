package com.sangeng.service.impl;

import com.sangeng.dao.AccoutDao;
import com.sangeng.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccoutDao accoutDao;

    @Transactional(propagation = Propagation.REQUIRES_NEW,isolation = Isolation.READ_COMMITTED)
    public void transfer(Integer outId, Integer inId, Double money) {
        //增加
        accoutDao.updateMoney(inId,money);
        //减少
        accoutDao.updateMoney(outId,-money);
    }

    @Transactional(readOnly = true)
    public void log() {
        System.out.println("打印日志");
        int i = 1/0;
    }
}
