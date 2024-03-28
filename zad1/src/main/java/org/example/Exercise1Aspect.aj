package org.example;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public aspect Exercise1Aspect {
    // a) metody zwracające liczbę typu float
    @Pointcut("execution(float *.*(..))")
    private void returnFloatTestA() {}

    @Before("returnFloatTestA()")
    public void beforeReturnFloatTestA() {
        System.out.println("pointcut: testA");
    }

    // b) metody z jednym parametrem typu String
    @Pointcut("execution(* *(String))")
    private void singleStringTestB() {}

    @Before("singleStringTestB()")
    public void beforeSingleStringParamTestB() {
        System.out.println("pointcut: testB");
    }

    // c) metody z dwoma parametrami dowolnych typów
    @Pointcut("execution(* *(*, *))")
    private void twoParamsTestC() {}

    @Before("twoParamsTestC()")
    public void beforeTwoParamsTestC() {
        System.out.println("pointcut: testC");
    }

    // d) metody publicznie w klasach *Service w pakiecie services
    @Pointcut("execution(public * *..services.*Service.*(..))")
    private void publicMethodsInServiceTestD() {}

    @Before("publicMethodsInServiceTestD()")
    public void beforePublicMethodInServiceTestD() { System.out.println("pointcut: testD"); }

    // e) metody set* w klasach z pakietu model, które nie są publiczne
    @Pointcut("execution(* *..model.*.set*(..)) && !execution(public * *..model.*.set*(..))")
    private void nonPublicSetMethodsInModelTestE() {}

    @Before("nonPublicSetMethodsInModelTestE()")
    public void beforeNonPublicSetMethodInModelTestE() {
        System.out.println("pointcut: testE");
    }

    // f) metody z adnotacją @Depreciated
    @Pointcut("@annotation(Deprecated)")
    private void deprecatedTestF() {}

    @Before("deprecatedTestF()")
    public void beforeDeprecatedTestF() {
        System.out.println("pointcut: testF");
    }

    // g) metody typu d, które nie są typu c  - wykorzystaj łączenie przecięć za pomocą operatorów logicznych
    @Pointcut("publicMethodsInServiceTestD() && !twoParamsTestC()")
    private void publicMethodsInServiceNotTwoParamsTestG() {}

    @Before("publicMethodsInServiceNotTwoParamsTestG()")
    public void beforePublicMethodsInServiceNotTwoParamsTestG() {
        System.out.println("pointcut: testG");
    }
}
