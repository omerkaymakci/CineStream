package com.cinestream.api_gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class JwtForwardingFilter implements GatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication())
                .cast(JwtAuthenticationToken.class)
                .flatMap(token -> {

                    String userId = token.getToken().getSubject();

                    Object realmAccess = token.getTokenAttributes().get("realm_access");
                    String roles = "";

                    if (realmAccess instanceof Map<?, ?> map) {
                        Object r = map.get("roles");
                        if (r != null) {
                            roles = r.toString();
                        }
                    }

                    ServerHttpRequest mutatedRequest =
                            exchange.getRequest()
                                    .mutate()
                                    .header("X-User-Id", userId)
                                    .header("X-User-Roles", roles)
                                    .build();

                    ServerWebExchange mutatedExchange =
                            exchange.mutate().request(mutatedRequest).build();

                    return chain.filter(mutatedExchange);
                });
    }
}
