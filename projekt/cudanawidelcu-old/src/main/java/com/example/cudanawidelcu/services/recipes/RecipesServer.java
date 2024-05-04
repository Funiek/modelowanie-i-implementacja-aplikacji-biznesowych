package com.example.cudanawidelcu.services.recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

import java.util.logging.Logger;


@SpringBootApplication
@EnableDiscoveryClient
public class RecipesServer {

	protected Logger logger = Logger.getLogger(RecipesServer.class.getName());

	/**
	 * Run the application using Spring Boot and an embedded servlet engine.
	 * 
	 * @param args
	 *            Program arguments - ignored.
	 */
	public static void main(String[] args) {
		// Tell server to look for accounts-server.properties or
		// recipes-server.yml
		System.setProperty("spring.config.name", "recipes-server");

		SpringApplication.run(RecipesServer.class, args);
	}
}
