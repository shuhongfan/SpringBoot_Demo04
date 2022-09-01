package com.sangeng.service;

import com.sangeng.aspect.InvokeLog;
import com.sangeng.myAspect.MyLog;
import org.springframework.stereotype.Service;

@Service
public class PhoneService {

//    @InvokeLog
    @MyLog
    public void deleteAll(){

        System.out.println("PhoneService中deleteAll的核心代码");
//        System.out.println(1/0);
    }
}
