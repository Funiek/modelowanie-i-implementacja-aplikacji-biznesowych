package org.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Mono;

/**
 * Controller class for handling hello endpoint.
 */
@RestController
@RequestMapping("/api/v1/hello")
@EnableWebFlux
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class HelloController {

    /**
     * Endpoint that returns a greeting message.
     *
     * @return Mono emitting a String with the greeting message
     */
    @GetMapping
    public Mono<String> hello() {
        return Mono.just("Hello from secured endpoint");
    }
}