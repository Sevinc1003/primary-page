package com.example.demo.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class LoginRequest {

    public LoginRequest() {
    }

    @Email (message = "email format is not correct")
    private String email;

    @NotBlank (message = "please insert password")
    private String password;

}
