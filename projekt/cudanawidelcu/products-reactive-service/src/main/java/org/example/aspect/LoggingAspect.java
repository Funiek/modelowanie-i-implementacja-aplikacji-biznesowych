package org.example.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Aspect for logging method calls in classes annotated with @Service.
 */
@Aspect
@Component
public class LoggingAspect {

    protected Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    /**
     * Pointcut definition to match all Spring @Service annotated classes.
     */
    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void servicePointcut() {
    }

    /**
     * Advice to log method calls for classes matched by the servicePointcut.
     *
     * @param joinPoint the JoinPoint representing the intercepted method
     */
    @Before("servicePointcut()")
    public void logMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        logger.info("PRODUCTS LOGGER ===> Method: " + className + "." + methodName + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}