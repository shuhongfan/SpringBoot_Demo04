package com.sangeng.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    private String name;
    private int id;
    private int age;

    public void init(){
        System.out.println("对学生对象进行初始化操作");
    }

    public void close(){
        System.out.println("对象销毁之前调用，用于释放资源");
    }
}
