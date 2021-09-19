package com.comp.tasks.security.jwt;

import com.comp.tasks.security.exceptions.AuthenticationException;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityContextRepository implements ServerSecurityContextRepository {
    JWTUtil jwtUtil;
    JwtProperties properties;

    @Override
    public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange swe) {
        return Mono.just(swe.getRequest())
                .flatMap(this::getToken)
                .flatMap(this::getAuthentication)
                .subscribeOn(Schedulers.boundedElastic())
                .switchIfEmpty(Mono.error(new AuthenticationException()))
                .map(SecurityContextImpl::new);
    }

    private Mono<String> getToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null) {
            if (authHeader.startsWith("Bearer ")) {
                return Mono.just(authHeader.substring(7));
            }
        } else if (request.getQueryParams().containsKey("token") && properties.isEnableAuthByTokenInTheRequestBody()) {
            return Mono.justOrEmpty(request.getQueryParams().getFirst("token"));
        }
        return Mono.empty();
    }

    private Mono<Authentication> getAuthentication(String token) {
        if (!jwtUtil.validateToken(token)) {
            log.debug("Token Expired: " + token);
            return Mono.error(new AuthenticationException("Token Expired"));
        }
        Claims claims = jwtUtil.getAllClaimsFromToken(token);
        String role = claims.get("role", String.class);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return Mono.just(new CompAuthenticationToken(String.valueOf(claims.get("username")), claims.getSubject(), authorities))
                .doOnNext(authToken -> ReactiveSecurityContextHolder.getContext()
                        .doOnNext(securityContext -> securityContext.setAuthentication(authToken)).subscribe())
                .cast(Authentication.class);
    }
}
