package org.example.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging method calls in web controllers and services.
 */
@Aspect
@Component
public class LoggingAspect {

    /**
     * Pointcut for all methods within classes annotated with @Controller or @RestController.
     */
    @Pointcut("execution(* org.example.controller.*.*(..))")
    public void webPointcut() {}

    /**
     * Advice to log method calls in web controllers.
     *
     * @param joinPoint the join point for the intercepted method
     */
    @Before("webPointcut()")
    public void logMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        System.out.println("WEB CONTROLLER LOGGER ===> Method: " + className + "." + methodName + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    /**
     * Pointcut for all methods within classes annotated with @Service.
     */
    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void servicePointcut() {}

    /**
     * Advice to log method calls in services.
     *
     * @param joinPoint the join point for the intercepted method
     */
    @Before("servicePointcut()")
    public void logMethod2Call(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        System.out.println("SERVICE CALL LOGGER ===> Method: " + className + "." + methodName + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}