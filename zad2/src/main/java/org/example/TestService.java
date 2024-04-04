package org.example;

import org.example.annotation.NotNullArgs;
import org.example.annotation.PublicsLogger;

@PublicsLogger
public class TestService {

    @NotNullArgs
    public void test1(String text, int x) {}

    public void test2(String text, int x) throws IllegalStateException {
        throw new IllegalStateException("Test wyjatku");
    }
}
