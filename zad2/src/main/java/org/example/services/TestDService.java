package org.example.services;

public class TestDService {
    private void testDButPrivate() {}
    public void testD(int x, int y) {
        testDButPrivate();
    }

    public void testG() {}
}
