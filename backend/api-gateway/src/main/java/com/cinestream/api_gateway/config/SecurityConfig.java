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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeExchange(exchanges -> exchanges

                        // CORS preflight istekleri auth gerektirmesin
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .pathMatchers("/auth/**", "/oauth2/**", "/login").permitAll()

                        // Movie endpoints
                        .pathMatchers(HttpMethod.GET, "/movie-service/movies/**")
                        .hasAnyRole("USER", "ADMIN")
                        .pathMatchers(HttpMethod.POST, "/movie-service/movies")
                        .hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/movie-service/movies/**")
                        .hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/movie-service/movies/**")
                        .hasRole("ADMIN")

                        // Genre endpoints
                        .pathMatchers(HttpMethod.GET, "/genres/**")
                        .hasAnyRole("USER", "ADMIN")
                        .pathMatchers(HttpMethod.POST, "/genres")
                        .hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/genres/**")
                        .hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/genres/**")
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

    /**
     * Allow the frontend (http://localhost:3000) to call the gateway from the
     * browser, including preflight requests with Authorization headers.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}


