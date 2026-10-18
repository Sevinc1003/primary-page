package com.example.demo.dto.auth;

import java.time.LocalDateTime;

import com.example.demo.enums.Role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class LoginResponse {

    private String token;
    private String email;
    private Role role;
    private LocalDateTime expiryDate;

}
