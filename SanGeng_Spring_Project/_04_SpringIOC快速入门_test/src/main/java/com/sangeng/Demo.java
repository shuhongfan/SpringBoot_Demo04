package com.sangeng;

import com.sangeng.dao.StudentDao;
import com.sangeng.domain.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Demo {

    public static void main(String[] args) {
        //创建容器
        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        //获取对象
        StudentDao studentDao = (StudentDao) app.getBean("studentDao");
//        StudentDao studentDao2 = (StudentDao) app.getBean("studentDao");
        //测试
//        Student stu = studentDao.getStudentById(30);
//        System.out.println(stu);
        Student stu = (Student) app.getBean("student2");
//        stu.setName("西施");
//
//        Student stu2  = new Student("东南枝",20,19);
    }



}
