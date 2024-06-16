package org.example.repository;

import org.example.model.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Repository interface for performing CRUD operations on User entities.
 */
@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {

    /**
     * Finds a user by username.
     *
     * @param username the username of the user to find
     * @return Mono emitting the User entity if found, otherwise empty
     */
    Mono<User> findByUsername(String username);
}