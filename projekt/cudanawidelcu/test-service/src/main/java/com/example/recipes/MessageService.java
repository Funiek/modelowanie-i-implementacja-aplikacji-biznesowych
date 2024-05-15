package com.example.recipes;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Flux<Message> getAll() {
        return messageRepository.findAll();
    }

    public Mono<Message> save(Message message) {
        return messageRepository.save(message);
    }
}
