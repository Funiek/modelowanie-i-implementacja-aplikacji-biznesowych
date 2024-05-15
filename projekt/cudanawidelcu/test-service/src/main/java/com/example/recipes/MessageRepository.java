package com.example.recipes;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface MessageRepository extends R2dbcRepository<Message, Long> {
}
