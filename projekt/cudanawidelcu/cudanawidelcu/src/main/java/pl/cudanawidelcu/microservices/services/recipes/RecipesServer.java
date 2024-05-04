package pl.cudanawidelcu.microservices.services.recipes;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

import pl.cudanawidelcu.microservices.accounts.AccountRepository;
import pl.cudanawidelcu.microservices.accounts.AccountsConfiguration;
import pl.cudanawidelcu.microservices.recipes.RecipesConfiguration;

@SpringBootApplication
@EnableDiscoveryClient
@Import(RecipesConfiguration.class)
public class RecipesServer {
    protected Logger logger = Logger.getLogger(RecipesServer.class.getName());

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "recipes-server");
        SpringApplication.run(RecipesServer.class, args);
    }
}
