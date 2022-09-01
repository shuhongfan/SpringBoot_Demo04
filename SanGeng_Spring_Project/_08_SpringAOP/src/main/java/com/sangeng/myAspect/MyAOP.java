package com.sangeng.myAspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MyAOP {
    @Pointcut("execution(* com.sangeng.service..*.*(..))")
    public void pt() {

    }

    @Before("pt()")
    public void methodBefore(JoinPoint joinPoint) {
        System.out.println("Before");
    }

    @AfterReturning(value = "pt()",returning = "res")
    public void methodAfterReturning(JoinPoint joinPoint,Object res) {
        System.out.println("AfterReturning"+res);
    }

    @After("pt()")
    public void methodAfter(JoinPoint joinPoint) {
        System.out.println("After");
    }

    @AfterThrowing(value = "pt()",throwing = "e")
    public void methodAfterThrowing(JoinPoint joinPoint,Throwable e) {
        System.out.println("AfterThrowing"+e);
    }

//    @Around("pt()")
    public Object methodAround(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        Signature signature = pjp.getSignature();
        Object target = pjp.getTarget();
        Object ret = null;

        Object ret;
        try {
            ret = pjp.proceed();
            System.out.println(ret);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
//        return ret;
        return new Integer(1111);
    }
}
