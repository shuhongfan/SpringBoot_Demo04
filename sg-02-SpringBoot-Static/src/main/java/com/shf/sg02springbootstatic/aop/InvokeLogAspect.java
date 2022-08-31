package com.shf.sg02springbootstatic.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class InvokeLogAspect {
    //    切点
    @Pointcut("@annotation(com.shf.sg02springbootstatic.aop.InvokeLog)")
    public void pt() {

    }

    @Around("pt()")
    public void printInvokeLog(ProceedingJoinPoint joinPoint) {
//        目标方法调用之前
        Object proceed = null;
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        System.out.println(methodName+" 即将被调用");
        try {
            Object proceed = joinPoint.proceed();
//            目标方法调用后
            System.out.println(methodName+" 被调用完了");
        } catch (Throwable e) {
//            throw new RuntimeException(e);
//            目标方法出现异常
            System.out.println("出现异常");
        }
    }
}
