package org.example;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public aspect CustomAspects {
    // a) Metody zwracające liczbę typu float
    @Pointcut("execution(float *.*(..))")
    private void returnFloatMethods() {}

    @Before("returnFloatMethods()")
    public void beforeReturnFloatMethod() {
        System.out.println("A float returning method was called.");
    }

    // b) Metody z jednym parametrem typu String
    @Pointcut("execution(* *(String))")
    private void singleStringParamMethods() {}

    @Before("singleStringParamMethods()")
    public void beforeSingleStringParamMethod() {
        System.out.println("A method with a single String parameter was called.");
    }

    // c) Metody z dwoma parametrami dowolnych typów
    @Pointcut("execution(* *(*, *))")
    private void twoParamsMethods() {}

    @Before("twoParamsMethods()")
    public void beforeTwoParamsMethod() {
        System.out.println("A method with two parameters was called.");
    }

    // d) Metody publiczne w klasach *Service w pakiecie services
    @Pointcut("execution(public * services.*Service.*(..))")
    private void publicMethodsInService() {}

    @Before("publicMethodsInService()")
    public void beforePublicMethodInService() {
        System.out.println("A public method in a Service class was called.");
    }

    // e) Metody set* w klasach z pakietu model, które nie są publiczne
    @Pointcut("execution(* model.*.set*(..)) && !within(public *)")
    private void nonPublicSetMethodsInModel() {}

    @Before("nonPublicSetMethodsInModel()")
    public void beforeNonPublicSetMethodInModel() {
        System.out.println("A non-public set method in a Model class was called.");
    }

    // f) Metody z adnotacją @Deprecated
    @Pointcut("@annotation(Deprecated)")
    private void deprecatedMethods() {}

    @Before("deprecatedMethods()")
    public void beforeDeprecatedMethod() {
        System.out.println("A deprecated method was called.");
    }

    // g) Metody typu d, które nie są typu c
    @Pointcut("publicMethodsInService() && !twoParamsMethods()")
    private void publicServiceNotTwoParams() {}

    @Before("publicServiceNotTwoParams()")
    public void beforePublicServiceNotTwoParam() {
        System.out.println("A public method in a Service class that does not have two parameters was called.");
    }


    @Around("execution(public * org.example.*.*(..)) && !execution(public * org.example.*.get*(..)) && !execution(public * your.package.name.*.set*(..))")
    public Object measureMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        System.out.println(joinPoint.getSignature() + " executed in " + executionTime + "ms");
        return proceed;
    }

}
