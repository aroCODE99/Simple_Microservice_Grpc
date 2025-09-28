package com.AuthService.Mapper;

import com.AuthService.Entity.AppUser;
import com.AuthService.Dto.RegisterDto;

public class AuthMapper {
    public static AppUser toEntity(RegisterDto dto) {
        var user = new AppUser();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setUsername(dto.getUsername());
        return user;
    }
}
