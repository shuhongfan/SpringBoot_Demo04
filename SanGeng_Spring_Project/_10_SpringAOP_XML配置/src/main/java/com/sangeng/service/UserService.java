package com.sangeng.service;

import com.sangeng.aspect.InvokeLog;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public void deleteAll(){

        System.out.println("UserService中deleteAll的核心代码");
    }
//    @InvokeLog
    public void updateById(Integer id){
        System.out.println("UserService中updateById的核心代码");
    }

    @InvokeLog
    public int updateById(Integer id,String name,double price){
        System.out.println(1/0);
        System.out.println("UserService中updateById的核心代码");
        return id;
    }
}
