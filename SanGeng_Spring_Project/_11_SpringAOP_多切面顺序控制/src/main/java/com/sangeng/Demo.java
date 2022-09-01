package com.sangeng;

import com.sangeng.controller.AIController;
import com.sangeng.util.CryptUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Demo {
    public static void main(String[] args) {
        //创建容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        //获取对象
        AIController aiController = applicationContext.getBean(AIController.class);
        //调用方法
        String question = CryptUtil.AESencode("三连了吗？");
        String answer = aiController.getAnswer(question);
        System.out.println(answer);

    }
}
