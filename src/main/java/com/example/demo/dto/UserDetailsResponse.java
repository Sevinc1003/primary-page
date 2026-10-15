package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class UserDetailsResponse {

    private Long id;
    private String name;
    private String surname;

    private String email;

    private LocalDateTime registerDate;
    private LocalDateTime lastLoginDate;

}
