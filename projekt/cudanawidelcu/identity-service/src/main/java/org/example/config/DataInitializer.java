package org.example.config;

import org.example.model.Role;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.findByUsername("admin") == null) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin"));
            user.setRole(Role.ADMIN);

            userRepository.save(user);
        }
    }
}
