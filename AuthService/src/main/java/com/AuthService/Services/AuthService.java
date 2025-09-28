package com.AuthService.Services;

import com.AuthService.Dto.RegisterDto;
import com.AuthService.Dto.LoginDto;
import com.AuthService.Mapper.AuthMapper;
import com.AuthService.Entity.AppUser;
import com.AuthService.Repositorys.AuthRepo;
import com.AuthService.Security.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;

@Service
public class AuthService {

    private final AuthRepo authRepo;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authManager;

    private final JwtService jwtService;

    public AuthService(AuthRepo authRepo, PasswordEncoder passwordEncoder, AuthenticationManager authManager, JwtService jwtService) {
        this.authRepo = authRepo;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    public AppUser registerUser(RegisterDto dto) {
        String hashedPassword = passwordEncoder.encode(dto.getPassword()); // first hashed the password
        dto.setPassword(hashedPassword);
        AppUser user = AuthMapper.toEntity(dto);
        return authRepo.save(user);
    }

    public String loginUser(LoginDto dto) {
        Authentication authentication = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(userDetails);
        }
        return "Unable to login";
    }

}
