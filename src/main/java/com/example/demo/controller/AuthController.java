package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.auth.LoginRequest;
import com.example.demo.dto.auth.LoginResponse;
import com.example.demo.dto.auth.RegisterRequest;
import com.example.demo.service.AuthService;
import com.example.demo.utils.log.ActionLogger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest req) {

        try {
            service.register(req);
            

            ActionLogger.logSuccess("register", HttpStatus.CREATED.value(), req.getEmail());
            
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Registration successful");
                    
        } catch (Exception e) {

            ActionLogger.logError("register", HttpStatus.BAD_REQUEST.value(), e.getMessage(), req.getEmail());
            throw e; 
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest req) {

        try {
            LoginResponse response = service.login(req);
            
            ActionLogger.logSuccess("login", HttpStatus.OK.value(), req.getEmail());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ActionLogger.logError("login", HttpStatus.UNAUTHORIZED.value(), e.getMessage(), req.getEmail());
            throw e;
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);

            if (session != null) {
                session.invalidate();
            }

            ActionLogger.logSuccess("logout", HttpStatus.OK.value(), null);

            return ResponseEntity.ok("Logged out successfully");
            
        } catch (Exception e) {
            ActionLogger.logError("logout", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            throw e;
        }
    }
}