package com.cinestream.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final String JWKS_URI =
            "http://localhost:9000/realms/cinestream/protocol/openid-connect/certs";

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        return NimbusReactiveJwtDecoder.withJwkSetUri(JWKS_URI).build();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http,
            ReactiveJwtAuthenticationConverter jwtAuthenticationConverter
    ) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges

                        .pathMatchers("/auth/**", "/oauth2/**", "/login").permitAll()

                        .pathMatchers(HttpMethod.GET, "/movie-service/movies/**")
                        .hasAnyRole("USER", "ADMIN")

                        .pathMatchers(HttpMethod.POST, "/movie-service/movies")
                        .hasRole("ADMIN")

                        .pathMatchers(HttpMethod.PUT, "/movie-service/movies/**")
                        .hasRole("ADMIN")

                        .pathMatchers(HttpMethod.DELETE, "/movie-service/movies/**")
                        .hasRole("ADMIN")

                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )
                )
                .build();
    }

    /**
     * JWT içindeki "roles" claim'ini
     * ROLE_ADMIN, ROLE_USER formatına çevirir
     */
    @Bean
    public ReactiveJwtAuthenticationConverter reactiveJwtAuthenticationConverter() {

        ReactiveJwtAuthenticationConverter converter =
                new ReactiveJwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {

            List<GrantedAuthority> authorities = new ArrayList<>();

            Map<String, Object> realmAccess =
                    jwt.getClaim("realm_access");

            if (realmAccess != null) {
                List<String> roles =
                        (List<String>) realmAccess.get("roles");

                if (roles != null) {
                    roles.forEach(role ->
                            authorities.add(
                                    new SimpleGrantedAuthority("ROLE_" + role)
                            )
                    );
                }
            }

            return Flux.fromIterable(authorities);
        });

        return converter;
    }

}


