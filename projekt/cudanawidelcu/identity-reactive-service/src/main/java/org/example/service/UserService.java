package org.example.service;

import org.example.exception.UserNotFoundException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.response.RoleResponse;
import org.example.security.JwtService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthorities()));
    }

    public Mono<User> save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Mono<User> findById(Long id) {
        return userRepository.findById(id).switchIfEmpty(Mono.error(new UserNotFoundException()));
    }

    public Flux<User> getAll() {
        return userRepository.findAll();
    }

    public Mono<Void> delete(Long id) {
        return userRepository.deleteById(id);
    }

    public Mono<RoleResponse> findRole(String auth) {
        return Mono.just(
                RoleResponse.valueOf(jwtService.findRole(auth))
        );
    }

    public Mono<User> update(Long id, User user) {
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    existingUser.setUserRole(user.getUserRole());
                    existingUser.setUsername(user.getUsername());
                    return userRepository.save(existingUser);
                })
                .switchIfEmpty(Mono.error(new UserNotFoundException(user.getUsername())));
    }
}