package com.AuthService.Security;

import com.AuthService.Services.CustomUserDetailsService;
import com.AuthService.Services.JwtService;
import io.jsonwebtoken.Jwt;
import net.devh.boot.grpc.server.security.authentication.BearerAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@Configuration
public class GrpcSecurityConfig {

    @Bean
    public GrpcAuthenticationReader authenticationReader(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        return new JwtGrpcAuthenticationReader(jwtService, userDetailsService);
    }

}
