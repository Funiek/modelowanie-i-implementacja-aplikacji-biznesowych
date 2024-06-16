package org.example.security;

import lombok.Getter;
import org.example.dto.ValidateAdminDto;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Gateway filter factory that checks if the incoming request is from an admin.
 */
@Component
public class AdminCheckGatewayFilterFactory extends AbstractGatewayFilterFactory<AdminCheckGatewayFilterFactory.Config> {

    private final WebClient.Builder builder;
    private final String IDENTITY_SERVICE_URL = "http://APPLICATION-GATEWAY/identity-service";

    /**
     * Constructor for AdminCheckGatewayFilterFactory.
     *
     * @param builder the WebClient builder to use for making HTTP requests
     */
    public AdminCheckGatewayFilterFactory(WebClient.Builder builder) {
        super(Config.class);
        this.builder = builder;
    }

    /**
     * Applies the filter based on the given configuration.
     *
     * @param config the configuration for this filter
     * @return the GatewayFilter that applies admin validation
     */
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String auth = exchange.getRequest().getHeaders().getFirst("Authorization");

            if (auth == null || !auth.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return Mono.empty();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(auth.substring(7));

            Mono<ValidateAdminDto> response = builder.build()
                    .post()
                    .uri(IDENTITY_SERVICE_URL + "/api/v1/auth/validate-admin")
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .retrieve().bodyToMono(ValidateAdminDto.class);

            return response.flatMap(isAdminResponse -> {
                boolean isAdmin = Boolean.TRUE.equals(isAdminResponse.getIsValid());

                if (!isAdmin) {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return Mono.empty();
                }

                return chain.filter(exchange);
            }).onErrorResume(error -> {
                exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                return exchange.getResponse().setComplete();
            });
        };
    }

    /**
     * Configuration class for AdminCheckGatewayFilterFactory.
     */
    @Getter
    public static class Config {
        /**
         * Role to be validated.
         */
        private String role;

        /**
         * Sets the role to be validated.
         *
         * @param role the role to set
         */
        public void setRole(String role) {
            this.role = role;
        }
    }
}