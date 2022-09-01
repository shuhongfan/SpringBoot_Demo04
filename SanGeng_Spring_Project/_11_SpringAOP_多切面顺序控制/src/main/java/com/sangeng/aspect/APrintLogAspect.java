package com.sangeng.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Order(2)
public class APrintLogAspect {

    //对哪些方法增强
    @Pointcut("execution(* com.sangeng.controller..*.*(..))")
    public void pt(){}

    //怎么增强
    @Before("pt()")
    public void printLog(JoinPoint joinPoint){
        //输出 被增强的方法所在的类名 方法名 调用时传入的参数   joinPoint.getSignature().getName()  joinPoint.getArgs()
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //类名
        String className = signature.getDeclaringTypeName();
        //方法名
        String methodName = signature.getName();
        //调用时传入的参数
        Object[] args = joinPoint.getArgs();
        System.out.println(className+"=="+methodName+"======"+ Arrays.toString(args));
    }
}
