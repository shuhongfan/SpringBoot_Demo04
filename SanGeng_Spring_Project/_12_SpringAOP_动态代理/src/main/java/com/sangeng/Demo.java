package com.sangeng;

import com.sangeng.controller.AIController;
import com.sangeng.controller.AIControllerImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class Demo {
    public static void main(String[] args) {
        AIControllerImpl aiController = new AIControllerImpl();
//        String answer = aiController.getAnswer("三连了吗？");
//        System.out.println(answer);

        //使用动态代理增强getAnswer方法

        //1.JDK动态代理
        //获取类加载器
        ClassLoader cl = Demo.class.getClassLoader();
        //被代理类所实现接口的字节码对象数组
        Class<?>[] interfaces = AIControllerImpl.class.getInterfaces();
        AIController proxy = (AIController) Proxy.newProxyInstance(cl, interfaces, new InvocationHandler() {
            //使用代理对象的方法时 会调用到invoke
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //proxy   是代理对象
                //method 是当前被调用的方法封装的Method对象
                //args   是调用方法时传入的参数
                //调用被代理对象的对应方法
                //判断 当前调用的是否是getAnswer方法
                if(method.getName().equals("getAnswer")){
                    System.out.println("增强");
                }
                Object ret = method.invoke(aiController, args);
                return ret;
            }
        });
        String answer = proxy.getAnswer("三连了吗？");

        System.out.println(proxy.fortuneTelling("张三"));
//        System.out.println(answer);
    }
}
