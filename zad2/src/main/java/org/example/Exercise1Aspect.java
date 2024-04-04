package org.example;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Parameter;

@Aspect
public class Exercise1Aspect {
    @Pointcut("@annotation(org.example.annotation.NotNullArgs)")
    private void notNullArgsPointcut() {}

    @Around("notNullArgsPointcut()")
    public Object checkNotNullArgs(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = methodSignature.getMethod().getParameters();

        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                Parameter param = parameters[i];
                String paramSimpleName = param.getType().getSimpleName();
                String paramName = param.getName();
                throw new IllegalArgumentException("Argument can't be: " + paramSimpleName + " " + paramName);
            }
        }

        return joinPoint.proceed();
    }

}
