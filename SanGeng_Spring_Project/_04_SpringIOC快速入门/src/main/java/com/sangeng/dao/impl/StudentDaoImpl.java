package com.sangeng.dao.impl;

import com.sangeng.dao.StudentDao;
import com.sangeng.domain.Student;

public class StudentDaoImpl implements StudentDao {
    public Student getStudentById(int id) {
        return new Student("国服最强西施",30,30);
    }
}
