package com.AuthService.Security;

import com.AuthService.Services.CustomUserDetailsService;
import com.AuthService.Services.JwtService;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Nullable;

public class JwtGrpcAuthenticationReader implements GrpcAuthenticationReader {

    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;

    public JwtGrpcAuthenticationReader(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Nullable
    @Override
    public Authentication readAuthentication(ServerCall<?, ?> call, Metadata headers) throws AuthenticationException {
        Metadata.Key<String> AUTHORIZATION = Metadata.Key.of("AUTHORIZATION", Metadata.ASCII_STRING_MARSHALLER);
        String authHeader = headers.get(AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UsernameNotFoundException("User not authenticated ");
        }
        String token = authHeader.substring(7);
        String userEmail = jwtService.extractSubject(token);
        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        return new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities()
        );
    }
}
