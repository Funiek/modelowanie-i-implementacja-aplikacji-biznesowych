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
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * Service class that implements AuthenticationService.
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ReactiveAuthenticationManager authenticationManager;

    /**
     * Constructor for AuthenticationServiceImpl.
     *
     * @param userRepository the user repository
     * @param passwordEncoder the password encoder
     * @param jwtService the JWT service
     * @param authenticationManager the reactive authentication manager
     */
    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, ReactiveAuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a new user.
     *
     * @param request the registration request
     * @return Mono emitting an AuthenticationResponse with a JWT token upon successful registration
     */
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

    /**
     * Authenticates a user.
     *
     * @param request the authentication request
     * @return Mono emitting an AuthenticationResponse with a JWT token upon successful authentication
     */
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

    /**
     * Validates if the user is an admin based on the provided JWT token.
     *
     * @param token the JWT token
     * @return Mono emitting a ValidateAdminResponse indicating if the user is an admin
     */
    public Mono<ValidateAdminResponse> validateAdmin(String token) {
        boolean isValidAdmin = jwtService.validateAdmin(token);
        return Mono.just(ValidateAdminResponse.builder()
                .isValid(isValidAdmin)
                .build());
    }

    /**
     * Retrieves all users.
     *
     * @return Flux emitting all users
     */
    public Flux<User> getAll() {
        return userRepository.findAll();
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the user ID
     * @return Mono emitting void upon completion
     */
    public Mono<Void> delete(Long id) {
        return userRepository.deleteById(id);
    }

    /**
     * Finds the role of the user based on the provided JWT token.
     *
     * @param auth the JWT token
     * @return Mono emitting a RoleResponse with the user's role
     */
    public Mono<RoleResponse> findRole(String auth) {
        return Mono.just(
                RoleResponse.valueOf(jwtService.findRole(auth))
        );
    }

    /**
     * Updates a user's details.
     *
     * @param id the user ID
     * @param user the updated user details
     * @return Mono emitting the updated User
     */
    public Mono<User> update(Long id, User user) {
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    existingUser.setUserRole(user.getUserRole());
                    existingUser.setUsername(user.getUsername());
                    return userRepository.save(existingUser);
                })
                .switchIfEmpty(Mono.error(new UserNotFoundException(user.getUsername())));
    }

    /**
     * Finds a user by ID.
     *
     * @param id the user ID
     * @return Mono emitting the User if found, otherwise emits UserNotFoundException
     */
    public Mono<User> findById(Long id) {
        return userRepository.findById(id).switchIfEmpty(Mono.error(new UserNotFoundException()));
    }
}