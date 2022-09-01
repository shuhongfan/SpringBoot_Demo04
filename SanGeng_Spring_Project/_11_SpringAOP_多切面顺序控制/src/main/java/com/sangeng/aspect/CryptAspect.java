package com.sangeng.aspect;

import com.sangeng.util.CryptUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(1)
public class CryptAspect {

    //确定切点
    @Pointcut("@annotation(com.sangeng.aspect.Crypt)")
    public void pt(){

    }

    //定义通知
    @Around("pt()")
    public Object crypt(ProceedingJoinPoint pjp) {
        //获取去目标方法调用时的参数
        Object[] args = pjp.getArgs();
        //对参数进行解密  解密后传入目标方法执行
        String arg = (String) args[0];
        String s = CryptUtil.AESdecode(arg);//解密
        args[0] = s;
        Object proceed = null;
        String ret = null;
        try {
            proceed = pjp.proceed(args);//目标方法调用
            //目标方法执行后需要获取到返回值
            ret = (String) proceed;
            //对返回值加密后进行真正的返回
            ret = CryptUtil.AESencode(ret);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return ret;
    }

}
