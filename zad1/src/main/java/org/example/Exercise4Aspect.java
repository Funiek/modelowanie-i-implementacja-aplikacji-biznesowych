package org.example;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
@Aspect
public class Exercise4Aspect {
    @Pointcut("@annotation(org.example.annotation.Immutable)")
    public void immutableError() {}
    @Before("immutableError()")
    public void immutableSetCheck(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;
            throw new IllegalStateException("Set used in immutable property by method: " + methodSignature.getMethod().getName());
        }
    }
}
