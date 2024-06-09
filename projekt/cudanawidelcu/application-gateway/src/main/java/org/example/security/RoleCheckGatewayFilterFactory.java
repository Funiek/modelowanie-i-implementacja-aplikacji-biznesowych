package org.example.security;

import lombok.Getter;
import org.example.dto.RoleDto;
import org.example.dto.ValidateAdminDto;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class RoleCheckGatewayFilterFactory extends AbstractGatewayFilterFactory<RoleCheckGatewayFilterFactory.Config> {

    private final WebClient.Builder builder;
    private final String IDENTITY_SERVICE_URL = "http://APPLICATION-GATEWAY/identity-service";

    public RoleCheckGatewayFilterFactory(WebClient.Builder builder) {
        super(Config.class);
        this.builder = builder;
    }

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

            Mono<RoleDto> response = builder.build()
                    .post()
                    .uri(IDENTITY_SERVICE_URL + "/api/v1/auth/role")
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .retrieve().bodyToMono(RoleDto.class);

            return response.flatMap(roleDto -> {
                if (roleDto == RoleDto.NONE) {
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

    @Getter
    public static class Config {
        private String role;

        public void setRole(String role) {
            this.role = role;
        }
    }
}
