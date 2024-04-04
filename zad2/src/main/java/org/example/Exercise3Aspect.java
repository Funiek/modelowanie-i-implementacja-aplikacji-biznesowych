package org.example;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.example.annotation.NotNull;

import java.lang.reflect.Field;


@Aspect
public class Exercise3Aspect {
    @Pointcut("execution(* set*(..))")
    public void setPointcut(){}

    @Pointcut("@annotation(org.example.annotation.NotNull)")
    public void notNullPointcut(){}

    // nie mogę znaleźć przyczyny dlaczego te dwa warunki uwzględnione łącznie nie dają wyniku
    // a oddzielnie się wywołują, więc stworzyłem obejście
    @Pointcut("setPointcut() && notNullPointcut()")
    public void setNotNullPointcut(){}

    @Before("setPointcut() && args(value)")
    public void checkNotNull(JoinPoint joinPoint, Object value) {
        String methodName = joinPoint.getSignature().getName();
        String fieldName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);

        Class<?> targetClass = joinPoint.getTarget().getClass();
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            if (field.isAnnotationPresent(NotNull.class) && value == null) {
                throw new NullPointerException("Null assigned to field with @NotNull");
            }
        } catch (NoSuchFieldException ignored) {
        }
    }
}
