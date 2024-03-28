package org.example;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public aspect TestAspect {
    // a) Metody zwracające liczbę typu float
    @Pointcut("execution(float *.*(..))")
    private void returnFloatTestA() {}

    @Before("returnFloatTestA()")
    public void beforeReturnFloatTestA() {
        System.out.println("pointcut: test_a");
    }

    // b) Metody z jednym parametrem typu String
    @Pointcut("execution(* *(String))")
    private void singleStringTestB() {}

    @Before("singleStringTestB()")
    public void beforeSingleStringParamTestB() {
        System.out.println("pointcut: test_b");
    }
}
