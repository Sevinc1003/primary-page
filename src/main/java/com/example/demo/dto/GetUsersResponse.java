package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class GetUsersResponse {



    public GetUsersResponse(String name, String surname, String email, LocalDateTime registerDate,
            LocalDateTime lastLoginDate) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.registerDate = registerDate;
        this.lastLoginDate = lastLoginDate;
    }
    private String name;

    private String surname;

    private String email;

    private LocalDateTime registerDate;
    private LocalDateTime lastLoginDate;

}
