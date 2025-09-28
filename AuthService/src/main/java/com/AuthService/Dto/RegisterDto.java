package com.AuthService.Dto;

import lombok.*;

@Getter @Setter @ToString
public class RegisterDto {
    private String email;
    private String password;
    private String username;
}
