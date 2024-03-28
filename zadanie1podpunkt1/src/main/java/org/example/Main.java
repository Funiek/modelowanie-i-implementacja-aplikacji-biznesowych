package org.example;

public class Main {
    public static void main(String[] args) {
        TestService testService = new TestService();
        testService.test_a();
        testService.test_b("abc");
        System.out.println("Hello world!");
    }
}