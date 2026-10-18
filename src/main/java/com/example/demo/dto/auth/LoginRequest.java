package com.example.demo.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class LoginRequest {

    @Email (message = "email format is not correct")
    private String email;

    @NotBlank (message = "please insert password")
    private String password;

}
