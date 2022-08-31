package com.shf.sg01springbootquickstart.controller;

import com.shf.sg01springbootquickstart.domain.Student;
import com.shf.sg01springbootquickstart.domain.Student2;
import com.shf.sg01springbootquickstart.domain.User;
import com.shf.sg01springbootquickstart.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;


@RestController
public class HelloController {

    @Value("${name}")
    private String name;

    @Value("${date}")
    private Date date;

    @Autowired
    private Student student;

    @Autowired
    private Student2 student2;

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/usermapper")
    public List<User> usermapper() {
        return userMapper.findAll();
    }

    @RequestMapping("/student")
    public Student student() {
        return student;
    }

    @RequestMapping("/student2")
    public Student2 student2() {
        return student2;
    }

    @RequestMapping("/test")
    public String test() {
        return "Hello "+name +" "+date;
    }

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World";
    }

}
