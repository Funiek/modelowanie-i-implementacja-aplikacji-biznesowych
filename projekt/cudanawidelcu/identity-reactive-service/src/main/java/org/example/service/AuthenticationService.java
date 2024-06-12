package org.example.service;

import org.example.exception.UserNotAdminException;
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
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ReactiveAuthenticationManager authenticationManager;

    public AuthenticationService(UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService, ReactiveAuthenticationManager authenticationManager) {
        this.userService = userService;
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

        return userService.save(user)
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
                .flatMap(authentication -> userService.findByUsername(request.getUsername())
                        .map(user -> {
                            String jwtToken = jwtService.generateToken(user);
                            return AuthenticationResponse.builder()
                                    .token(jwtToken)
                                    .build();
                        }));
    }

    public Mono<ValidateAdminResponse> validateAdmin(String token) throws UserNotAdminException {
        boolean isValidAdmin = jwtService.validateAdmin(token);
        return Mono.just(ValidateAdminResponse.builder()
                .isValid(isValidAdmin)
                .build())
                .switchIfEmpty(Mono.error(new UserNotAdminException()));
    }


}

