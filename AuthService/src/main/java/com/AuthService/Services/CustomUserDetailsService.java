package com.AuthService.Services;

import com.AuthService.Repositorys.AuthRepo;
import com.AuthService.Security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthRepo authRepo;

    public CustomUserDetailsService(AuthRepo authRepo) {
        this.authRepo = authRepo;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) {
        return authRepo.findByEmail(username).map(CustomUserDetails::new).orElseThrow(
            () -> new UsernameNotFoundException("User not present in our db")
        );
    }
}
