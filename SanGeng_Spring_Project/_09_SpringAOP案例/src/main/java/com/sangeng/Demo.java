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
//        String answer = aiController.getAnswer("看视频应该三连吗？");

        String name = CryptUtil.AESencode("张三");
        String result = aiController.fortuneTelling(name);
//        System.out.println(answer);


        System.out.println(CryptUtil.AESdecode(result));
        System.out.println(result);



    }
}
