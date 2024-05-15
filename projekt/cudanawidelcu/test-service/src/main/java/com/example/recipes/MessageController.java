package com.example.recipes;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
@EnableWebFlux
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public Flux<Message> getAll() {
        return messageService.getAll();
    }

    @PostMapping
    public Mono<Message> save(@RequestBody Message message) {
        return messageService.save(message);
    }
}
