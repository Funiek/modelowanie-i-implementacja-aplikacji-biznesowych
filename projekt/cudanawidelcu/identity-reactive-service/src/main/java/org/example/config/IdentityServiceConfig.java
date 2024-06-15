package org.example.config;

import org.example.service.UserService;
import org.example.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class IdentityServiceConfig {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public IdentityServiceConfig(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() throws Exception {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userService);

        authenticationManager.setPasswordEncoder(passwordEncoder);

        return authenticationManager;
    }
}
