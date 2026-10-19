package com.example.demo.dto.auth;

import java.time.LocalDateTime;

import com.example.demo.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class LoginResponse {
    

    public LoginResponse() {
    }
    private String token;
    private String email;
    private Role role;
    private LocalDateTime expiryDate;

}
