package com.cinestream.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("movie_service", r -> r.path("/movie-service/**")
                        .filters(f -> f.stripPrefix(1)) // /movie-service kısmını backend’e iletir
                        .uri("http://localhost:8081") // movie-service’in portu
                )
                .route("auth_service", r -> r.path("/auth-service/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://localhost:9000")
                )
                .build();
    }

}
