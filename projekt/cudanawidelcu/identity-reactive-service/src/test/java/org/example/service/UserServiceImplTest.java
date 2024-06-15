package org.example.service;
import org.example.model.User;
import org.example.model.UserRole;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUsername() {
        // Given
        User user = new User(1L, "testUser", "testPassword", LocalDate.now(), UserRole.USER);
        when(userRepository.findByUsername("testUser")).thenReturn(Mono.just(user));

        // When
        Mono<UserDetails> userDetailsMono = userService.findByUsername("testUser");

        // Then
        StepVerifier.create(userDetailsMono)
                .expectNextMatches(userDetails ->
                        userDetails.getUsername().equals("testUser") &&
                                userDetails.getPassword().equals("testPassword") &&
                                userDetails.getAuthorities().size() == 1 &&
                                userDetails.getAuthorities().iterator().next().getAuthority().equals(UserRole.USER.name())
                )
                .verifyComplete();
    }

    @Test
    void save_shouldSaveUser() {
        // Given
        User user = new User(null, "newUser", "newPassword", LocalDate.now(), UserRole.USER);
        User savedUser = new User(1L, "newUser", "encodedPassword", LocalDate.now(), UserRole.USER);

        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(savedUser));

        // When
        Mono<User> savedUserMono = userService.save(user);

        // Then
        StepVerifier.create(savedUserMono)
                .expectNextMatches(saved -> saved.getId() != null && saved.getId() == 1L && saved.getPassword().equals("encodedPassword"))
                .verifyComplete();
    }
}
