package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
@EnableDiscoveryClient
public class RecipesServiceApplication {
    // TODO przepisanie na model reaktywny
    public static void main(String[] args) {
        SpringApplication.run(RecipesServiceApplication.class, args);
    }
}