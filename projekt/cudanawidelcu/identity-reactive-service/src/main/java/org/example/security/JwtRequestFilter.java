package org.example.security;

import org.example.service.JwtService;
import org.example.service.JwtServiceImpl;
import org.example.service.UserService;
import org.example.service.UserServiceImpl;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.util.List;

@Component
public class JwtRequestFilter implements WebFilter {

    private final UserService userService;
    private final JwtService jwtService;

    public JwtRequestFilter(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    @NonNull
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        List<String> headerList = request.getHeaders().get("Authorization");

        if (headerList == null || headerList.isEmpty()) {
            return chain.filter(exchange);
        }

        String authorizationHeader = headerList.get(0);

        if (!authorizationHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String jwt = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(jwt);

        if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            return chain.filter(exchange);
        }

        return userService.findByUsername(username)
                .flatMap(userDetails -> {
                    if (!jwtService.isTokenValid(jwt, userDetails)) {
                        return chain.filter(exchange);
                    }

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authenticationToken));
                });
    }

}
