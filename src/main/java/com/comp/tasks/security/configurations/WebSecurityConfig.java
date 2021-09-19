package com.comp.tasks.security.configurations;

import com.comp.tasks.security.jwt.BasicAuthResolver;
import com.comp.tasks.security.jwt.JWTUtil;
import com.comp.tasks.security.jwt.JwtProperties;
import com.comp.tasks.security.jwt.SecurityContextRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EnableConfigurationProperties(JwtProperties.class)
public class WebSecurityConfig {
    JwtProperties jwtProperties;

    @Bean
    JWTUtil jwtUtil() {
        return new JWTUtil(jwtProperties);
    }

    @Bean
    SecurityContextRepository securityContextRepository() {
        return new SecurityContextRepository(jwtUtil(), jwtProperties);
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // Remove the ROLE_ prefix
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, BasicAuthResolver basicAuthResolver) {
        ServerHttpSecurity exchange = http
                .cors()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> {
                    swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                }))
                .accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> {
                    swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                }))
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .securityContextRepository(securityContextRepository());

        if (jwtProperties != null && jwtProperties.isEnableActuator()) {
            exchange.addFilterAfter(new AuthenticationWebFilter(basicAuthResolver.resolver()), SecurityWebFiltersOrder.HTTP_BASIC);
        }

        ServerHttpSecurity.AuthorizeExchangeSpec exchangeSpec = exchange
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll();


        if (jwtProperties != null && jwtProperties.getPublicPath() != null) {
            jwtProperties.getPublicPath().forEach(s -> exchangeSpec.pathMatchers(s).permitAll());
        }

        if (jwtProperties != null && jwtProperties.isEnableActuator()) {
            exchangeSpec.pathMatchers("/actuator/**").hasRole("ACTUATOR");
        }

        return exchangeSpec.anyExchange()
                .authenticated()
                .and()
                .build();
    }

    @Bean
    public BasicAuthResolver basicAuthResolver(MapReactiveUserDetailsService userDetailsService) {
        return new BasicAuthResolver(userDetailsService);
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username(jwtProperties.getBasicUsername())
                .password(jwtProperties.getBasicPassword())
                .roles("ACTUATOR")
                .build();
        return new MapReactiveUserDetailsService(user);
    }
}
