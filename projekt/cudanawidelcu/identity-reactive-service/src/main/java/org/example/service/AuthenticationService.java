package org.example.service;

import org.example.exception.UserNotFoundException;
import org.example.model.UserRole;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.request.AuthenticationRequest;
import org.example.request.RegisterRequest;
import org.example.response.AuthenticationResponse;
import org.example.response.RoleResponse;
import org.example.response.ValidateAdminResponse;
import org.example.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ReactiveAuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, ReactiveAuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public Mono<AuthenticationResponse> register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .userRole(UserRole.USER)
                .createdAt(LocalDate.now())
                .build();

        return userRepository.save(user)
                .map(savedUser -> {
                    String jwtToken = jwtService.generateToken(savedUser);
                    return AuthenticationResponse.builder()
                            .token(jwtToken)
                            .build();
                });
    }

    public Mono<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        return authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                )
                .flatMap(authentication -> userRepository.findByUsername(request.getUsername())
                        .map(user -> {
                            String jwtToken = jwtService.generateToken(user);
                            return AuthenticationResponse.builder()
                                    .token(jwtToken)
                                    .build();
                        }));
    }

    public Mono<ValidateAdminResponse> validateAdmin(String token) {
        boolean isValidAdmin = jwtService.validateAdmin(token);
        return Mono.just(ValidateAdminResponse.builder()
                .isValid(isValidAdmin)
                .build());
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

    public Mono<User> findById(Long id) {
        return userRepository.findById(id).switchIfEmpty(Mono.error(new UserNotFoundException()));
    }
}

