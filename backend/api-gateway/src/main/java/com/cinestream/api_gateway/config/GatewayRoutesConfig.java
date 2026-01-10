package com.cinestream.api_gateway.config;

import com.cinestream.api_gateway.filter.JwtForwardingFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;


@Configuration
public class GatewayRoutesConfig {

    private final JwtForwardingFilter jwtForwardingFilter;

    public GatewayRoutesConfig(JwtForwardingFilter jwtForwardingFilter) {
        this.jwtForwardingFilter = jwtForwardingFilter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("movie_service", r -> r.path("/movie-service/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(jwtForwardingFilter)
                        )
                        .uri("http://localhost:8081")
                )
                .route("auth_service", r -> r.path("/auth-service/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://localhost:9000")
                )
                .build();
    }
}
