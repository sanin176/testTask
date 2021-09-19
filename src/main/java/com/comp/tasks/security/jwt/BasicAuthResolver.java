package com.comp.tasks.security.jwt;

import com.comp.tasks.security.exceptions.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BasicAuthResolver {
    private final MapReactiveUserDetailsService userDetailsService;

    public ReactiveAuthenticationManagerResolver<ServerWebExchange> resolver() {
        return exchange -> Mono.just(exchange)
                .filter(serverWebExchange -> serverWebExchange.getRequest().getPath().toString().startsWith("/actuator/"))
                .flatMap(data -> Mono.just(check()))
                .switchIfEmpty(Mono.error(new AuthenticationException()));
    }

    private ReactiveAuthenticationManager check() {
        return authentication -> validateUser(authentication)
                .map(b -> new CompAuthenticationToken(null, b.getUsername(), b.getAuthorities()))
                .cast(Authentication.class)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException(authentication.getPrincipal().toString())));
    }

    private Mono<UserDetails> validateUser(Authentication authentication) {
        return userDetailsService.findByUsername(authentication.getName())
                .flatMap(userDetails -> Mono.just(userDetails.getPassword().equals(authentication.getCredentials().toString()))
                        .filter(aBoolean -> aBoolean)
                        .map(aBoolean -> userDetails)
                        .switchIfEmpty(Mono.error(new AuthenticationException())));
    }
}
