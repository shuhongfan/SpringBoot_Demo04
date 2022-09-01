package com.sangeng.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class MyAspect {


//    @Before("pt()")

    public void before(JoinPoint joinPoint){
        System.out.println("before");
    }

//    @AfterReturning(value = "pt()",returning = "ret")
    public void afterReturning(JoinPoint joinPoint,Object ret){
        System.out.println("afterReturning:"+ret);
    }
//    @After("pt()")
    public void after(JoinPoint joinPoint){
        System.out.println("after");
    }

//    @AfterThrowing(value = "pt()",throwing = "e")
    public void afterThrowing(JoinPoint joinPoint,Throwable e){
        String message = e.getMessage();
        System.out.println("afterThrowing:"+message);
    }

    public Object around(ProceedingJoinPoint pjp){
        //获取参数
        Object[] args = pjp.getArgs();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Object target = pjp.getTarget();
        Object ret = null;
        try {
            ret = pjp.proceed();//目标方法的执行
            //ret就是被增强方法的返回值
            System.out.println(ret);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.out.println(throwable.getMessage());
        }
//        System.out.println(pjp);
        return ret;
    }
}
