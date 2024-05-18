package org.example.security;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication auth) {
        final User user = userRepository.findByUsername(auth.getName());

        ...

        return new UsernamePasswordAuthenticationToken(user,
                result.getCredentials(), result.getAuthorities());
    }

    @Override
    public boolean supports(Class authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
