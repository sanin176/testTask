package com.comp.tasks.security.jwt;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "security.jwt")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtProperties {
    String secret;
    String refreshSecret;
    String expiration;
    String refreshExpiration;
    boolean enableAuthByTokenInTheRequestBody;
    boolean enableRsocketConnectionAuthByJwt;
    boolean enableActuator;
    List<String> publicPath;
    String basicUsername;
    String basicPassword;
}
