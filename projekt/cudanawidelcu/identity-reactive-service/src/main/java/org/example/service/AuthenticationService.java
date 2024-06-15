package org.example.service;

import org.example.exception.UserNotFoundException;
import org.example.model.User;
import org.example.request.AuthenticationRequest;
import org.example.request.RegisterRequest;
import org.example.response.AuthenticationResponse;
import org.example.response.RoleResponse;
import org.example.response.ValidateAdminResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthenticationService {

    Mono<AuthenticationResponse> register(RegisterRequest request);

    Mono<AuthenticationResponse> authenticate(AuthenticationRequest request);

    Mono<ValidateAdminResponse> validateAdmin(String token);

    Flux<User> getAll();

    Mono<Void> delete(Long id);

    Mono<RoleResponse> findRole(String auth);

    Mono<User> update(Long id, User user);

    Mono<User> findById(Long id);
}