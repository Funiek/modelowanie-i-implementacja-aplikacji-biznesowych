package org.example.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging security-related method calls.
 */
@Aspect
@Component
public class LoggingAspect {

    /**
     * Pointcut definition for methods in the org.example.security package and its subpackages.
     */
    @Pointcut("execution(* org.example.security..*(..))")
    public void filterPointcut() {}

    /**
     * Advice to log method calls intercepted by the filterPointcut.
     *
     * @param joinPoint the join point representing the intercepted method
     */
    @Before("filterPointcut()")
    public void logMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        System.out.println("SECURITY LOGGER ===> Method: " + className + "." + methodName + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}