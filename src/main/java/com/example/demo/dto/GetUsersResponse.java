package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
public class GetUsersResponse {

    private String name;

    private String surname;

    private String email;

    private LocalDateTime registerDate;
    private LocalDateTime lastLoginDate;

}
