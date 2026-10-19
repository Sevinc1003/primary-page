package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.auth.LoginRequest;
import com.example.demo.dto.auth.LoginResponse;
import com.example.demo.dto.auth.RegisterRequest;


@Service
public interface AuthService {

    public void register(RegisterRequest req);
    public LoginResponse login(LoginRequest req);
}
