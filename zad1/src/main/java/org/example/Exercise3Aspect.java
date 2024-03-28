package org.example;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
public class Exercise3Aspect {
    private static final Map<Object, Long> lastCallTimes = new ConcurrentHashMap<>();

    @Pointcut("execution(public * *..services.*Service.*(..))")
    public void servicesPointcut() {}

    @Around("servicesPointcut()")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        Object result = joinPoint.proceed();

        lastCallTimes.put(target, System.currentTimeMillis());

        return result;
    }

    public static void lastCallTime(Object object) {
        Long time = lastCallTimes.get(object);
        if (time != null) {
            // Konwersja timestampu na obiekt LocalDateTime
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());

            // Formatowanie daty i czasu
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            String formattedDateTime = dateTime.format(formatter);

            System.out.println("last time method executed in class: " + formattedDateTime);
        } else {
            System.out.println("method in this class were not executed yet");
        }
    }
}
