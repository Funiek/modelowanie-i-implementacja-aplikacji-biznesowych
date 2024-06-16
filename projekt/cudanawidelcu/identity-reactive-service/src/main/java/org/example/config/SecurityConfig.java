package org.example.config;


import org.example.security.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Configuration class for security settings using Spring Security's WebFlux support.
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final ReactiveAuthenticationManager reactiveAuthenticationManager;

    /**
     * Constructor for SecurityConfig.
     *
     * @param jwtRequestFilter the JWT request filter for authentication
     * @param reactiveAuthenticationManager the reactive authentication manager
     */
    public SecurityConfig(JwtRequestFilter jwtRequestFilter, ReactiveAuthenticationManager reactiveAuthenticationManager) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;
    }

    /**
     * Configures security filters and rules.
     *
     * @param http the ServerHttpSecurity to configure
     * @return the configured SecurityWebFilterChain
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorize -> authorize
                        .pathMatchers("/api/v1/auth/authenticate", "/api/v1/auth/register").permitAll()
                        .anyExchange().authenticated()
                )
                .authenticationManager(reactiveAuthenticationManager)
                .addFilterAt(jwtRequestFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}