package com.cinestream.api_gateway.config;

import com.cinestream.api_gateway.filter.JwtForwardingFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/auth/**",
                                "/oauth2/**",
                                "/login"
                        ).permitAll()
                        .pathMatchers("/movies/**").authenticated()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults())
                )
                .build();
    }

    @Configuration
    public class GatewayConfig {

        @Bean
        public RouteLocator routes(RouteLocatorBuilder builder) {
            return builder.routes()
                    .route("movie_service", r -> r.path("/movie-service/**")
                            .filters(f -> f.stripPrefix(1)) // /movie-service kısmını backend’e iletir
                            .uri("http://localhost:8081") // movie-service’in portu
                    )
                    .build();
        }

    }


}

