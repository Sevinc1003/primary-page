package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(unique = true, nullable = false, updatable = false)
    private String email;

    private String password;

    private LocalDateTime registerDate;
    private LocalDateTime lastLoginDate;

    @Column(nullable = false)
    private int failedAttempts = 0;

    private boolean isEnabled = true; //verification elemirik deye

    @PrePersist
    protected void onCreate() {
        this.registerDate = LocalDateTime.now();
    }

}
