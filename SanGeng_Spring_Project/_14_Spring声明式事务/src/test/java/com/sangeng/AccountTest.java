package com.sangeng;

import com.sangeng.service.AccountService;
import com.sangeng.service.impl.TestServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class AccountTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TestServiceImpl testService;

    @Test
    public void testTransfer(){
        accountService.transfer(1,2,new Double(10));
    }
    @Test
    public void testPropagation(){
        testService.test();
    }
}
