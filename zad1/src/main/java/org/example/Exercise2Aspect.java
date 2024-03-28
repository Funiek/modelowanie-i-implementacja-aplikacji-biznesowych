package org.example;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class Exercise2Aspect {
//        @Pointcut("execution(public * org.example..*.*(..))") +
//            " && !execution(public * org.example..*.get*(..))" +
//            " && !execution(public * org.example..*.set*(..))")
    @Pointcut("execution(public * org.example..*.*(..))" +
            " && !execution(public * org.example..*.get*(..))" +
            " && !execution(public * org.example..*.set*(..))")
    public void allPublicMethodsWithoutGetAndSet(){}

    @Around("allPublicMethodsWithoutGetAndSet()")
    public Object logBeforeAllMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - startTime;
        System.out.println("method: " + joinPoint.getSignature().getName() + " executed in " + executionTime + "ms");

        return proceed;
    }
}
