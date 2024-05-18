package org.example;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {
    // TODO obsluga uzytkownikow
    // TODO autoryzacja
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}