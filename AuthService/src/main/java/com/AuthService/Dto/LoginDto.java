package com.AuthService.Dto;

import lombok.*;
 
@Getter @Setter @ToString
public class LoginDto {  
    private String email;
    private String password;
}
