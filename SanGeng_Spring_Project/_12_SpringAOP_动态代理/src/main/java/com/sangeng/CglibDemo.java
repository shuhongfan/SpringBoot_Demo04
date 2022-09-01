package com.sangeng;


import com.sangeng.controller.AIControllerImpl;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibDemo {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        //设置父类的字节码对象
        enhancer.setSuperclass(AIControllerImpl.class);
        enhancer.setCallback(new MethodInterceptor() {
            //使用代理对象执行方法是都会调用到intercept方法
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                //判断当前调用的方法是不是getAnswer方法 如果是进行增强
                if ("getAnswer".equals(method.getName())){
                    System.out.println("被增强了");
                }
                //调用父类中对应的方法
                Object ret = methodProxy.invokeSuper(o, objects);
                return ret;
            }
        });
        //生成代理对象
        AIControllerImpl proxy = (AIControllerImpl) enhancer.create();
        System.out.println(proxy.getAnswer("你好吗？"));
//        System.out.println(proxy.fortuneTelling("你好吗？"));
    }



}
