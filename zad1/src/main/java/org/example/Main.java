package org.example;

import org.example.model.Product;
import org.example.model.ImmutableTestEntity;
import org.example.services.TestDService;
import org.example.services.TimeTestService;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        // podpunkt 1
        TestService testService = new TestService();
        testService.testA();
        testService.testB("abc");
        testService.testC("abc",123);

        TestDService testDService = new TestDService();
        testDService.testD(1,1);

        // testE
        Product product = new Product(1);
        System.out.println("Product id: " + product.getId());

        testService.testF();
        testDService.testG();

        // podpunkt 3
        TimeTestService timeTestService = new TimeTestService();
        timeTestService.doSmth();
        TimeUnit.SECONDS.sleep(2);
        Exercise3Aspect.lastCallTime(timeTestService);
        timeTestService.doSmth();
        TimeUnit.SECONDS.sleep(2);
        Exercise3Aspect.lastCallTime(timeTestService);

        // podpunkt 4
        ImmutableTestEntity immutableTestEntity = new ImmutableTestEntity(2);
        try {
            immutableTestEntity.setId(3);
        } catch (Exception ex) {
            System.out.println("BLAD: " + ex.getMessage());
        }
        System.out.println("ImmutableTestService: " + immutableTestEntity.getId());
    }


}
