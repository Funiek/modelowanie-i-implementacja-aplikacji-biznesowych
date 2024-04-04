package org.example;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

@Aspect
public class Exercise2Aspect {

    @Pointcut("execution(public * *(..))")
    public void allPublicPointcut(){}

    @Pointcut("within(@org.example.annotation.PublicsLogger *)")
    public void publicLoggerPointcut(){}
    @Pointcut("publicLoggerPointcut() && allPublicPointcut()")
    public void allPublicWithPublicsLogger(){}

    @Around("allPublicWithPublicsLogger()")
    public Object loggingToFile(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("asa");
        Logger logger = LogManager.getLogger(joinPoint.getTarget().getClass());
        try {
            logger.trace("Method: " + joinPoint.getSignature().getName() + " called");
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("Method: " + joinPoint.getSignature().getName() + " threw " + throwable);
            throw throwable;
        }
    }
}
