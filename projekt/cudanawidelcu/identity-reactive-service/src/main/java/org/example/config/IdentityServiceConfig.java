package org.example.config;

import org.example.service.UserService;
import org.example.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for setting up identity service related beans.
 */
@Configuration
public class IdentityServiceConfig {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for IdentityServiceConfig.
     *
     * @param userService the UserService instance to use for authentication
     * @param passwordEncoder the PasswordEncoder instance to use for encoding passwords
     */
    public IdentityServiceConfig(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Provides a reactive authentication manager bean.
     *
     * @return the ReactiveAuthenticationManager instance configured with UserService and PasswordEncoder
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() throws Exception {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userService);

        authenticationManager.setPasswordEncoder(passwordEncoder);

        return authenticationManager;
    }
}