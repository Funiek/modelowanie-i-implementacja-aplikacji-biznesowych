package pl.cudanawidelcu.microservices.recipes.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void publicMethodsPointcut() {}

    @Before("publicMethodsPointcut()")
    public void logMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        System.out.println("PANCIAKOWY LOGGER ===> Method: " + className + "." + methodName + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}
