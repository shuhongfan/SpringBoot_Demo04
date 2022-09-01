package com.sangeng;

import com.alibaba.druid.pool.DruidDataSource;
import com.sangeng.dao.StudentDao;
import com.sangeng.domain.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Demo {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
//        DruidDataSource dataSource = (DruidDataSource) app.getBean("dataSource3");
        Student student = (Student) app.getBean("student");
        System.out.println(student);
        app.close();

    }

}
