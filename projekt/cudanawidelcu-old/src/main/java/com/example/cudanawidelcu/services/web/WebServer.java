package com.example.cudanawidelcu.services.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

/**
 * Accounts web-server. Works as a microservice client, fetching data from the
 * Account-Service. Uses the Discovery Server (Eureka) to find the microservice.
 * 
 * @author Paul Chapman
 */
@SpringBootApplication(exclude = { HibernateJpaAutoConfiguration.class, //
        DataSourceAutoConfiguration.class })
@EnableDiscoveryClient
@ComponentScan(useDefaultFilters = false) // Disable component scanner
public class WebServer {
    public static final String RECIPES_SERVICE_URL = "http://RECIPES-SERVICE";

    public static void main(String[] args) {
        // Tell server to look for web-server.properties or web-server.yml
        System.setProperty("spring.config.name", "web-server");
        SpringApplication.run(WebServer.class, args);
    }

    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
