package org.example;

import org.example.model.Entity;
import org.example.services.TestDService;
import org.example.services.TimeTestService;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        TestService testService = new TestService();
        testService.test1("a", 2);

        // zadanie 1 test
        try {
            testService.test1(null, 2);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // zadanie 2 test
        try {
            testService.test2("a", 2);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // zadanie 3 test
        Entity entity = new Entity();
        try {
            entity.setNameWithAnnotation(null);
            entity.setNameWithoutAnnotation(null);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


}
