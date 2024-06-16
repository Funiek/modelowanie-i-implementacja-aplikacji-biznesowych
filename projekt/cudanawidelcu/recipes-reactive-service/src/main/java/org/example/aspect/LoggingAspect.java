package org.example.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Aspect for logging method calls in services.
 */
@Aspect
@Component
public class LoggingAspect {

    protected Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    /**
     * Pointcut definition that matches all Spring service beans.
     */
    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void servicePointcut() {}

    /**
     * Advice that logs method calls.
     *
     * @param joinPoint the join point representing method execution
     */
    @Before("servicePointcut()")
    public void logMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        logger.info("RECIPES LOGGER ===> Method: " + className + "." + methodName + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}
