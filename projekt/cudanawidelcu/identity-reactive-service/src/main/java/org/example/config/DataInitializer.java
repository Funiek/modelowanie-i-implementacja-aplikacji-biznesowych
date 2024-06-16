package org.example.config;

import org.example.model.UserRole;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * Initializes data upon application startup, specifically creating an admin user if not already present.
 */
@Component
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for DataInitializer.
     *
     * @param userRepository the repository for accessing user data
     * @param passwordEncoder the password encoder for encrypting passwords
     */
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Executes data initialization upon application startup.
     *
     * @param args the application arguments passed to the application
     * @throws Exception if an error occurs during data initialization
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        userRepository.findByUsername("admin")
                .switchIfEmpty(Mono.defer(() -> {
                    User user = new User();
                    user.setUsername("admin");
                    user.setPassword(passwordEncoder.encode("admin"));
                    user.setUserRole(UserRole.ADMIN);
                    user.setCreatedAt(LocalDate.now());

                    return userRepository.save(user);
                }))
                .subscribe();
    }
}