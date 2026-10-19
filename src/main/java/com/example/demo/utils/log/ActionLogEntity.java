package com.example.demo.utils.log;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "action_logs")
@Getter 
@Setter 
public class ActionLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;          // Əməliyyatı edənin emaili
    private String methodName;     // Hansı metod işlədilib (məs: login, setEnableUser)
    private int statusCode;        // 200, 400, 401 və s.
    
    @Column(length = 1000)
    private String errorMessage;   // Xəta varsa, mətni

    private LocalDateTime createdAt; // Nə vaxt
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    
}