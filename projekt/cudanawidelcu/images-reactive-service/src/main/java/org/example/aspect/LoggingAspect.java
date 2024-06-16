package org.example.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Aspect for logging method calls within Spring service components.
 */
@Aspect
@Component
public class LoggingAspect {

    protected Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    /**
     * Pointcut definition for all methods within Spring service components.
     */
    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void servicePointcut() {}

    /**
     * Advice to log method calls matched by the 'servicePointcut' pointcut.
     *
     * @param joinPoint the join point representing the intercepted method
     */
    @Before("servicePointcut()")
    public void logMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        logger.info("IMAGES LOGGER ===> Method: " + className + "." + methodName + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}