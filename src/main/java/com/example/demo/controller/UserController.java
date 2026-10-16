package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.*;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

        private final UserService service;


    @GetMapping("/details")
    public ResponseEntity<UserDetailsResponse> getDetails(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(service.getDetails(email));
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(@Valid @RequestBody UpdateProfileRequest req,
            Authentication authentication) {

        String email = authentication.getName();

        service.updateProfile(req, email);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest req,
            Authentication authentication,
            HttpServletRequest request) {

        String email = authentication.getName();

        service.changePassword(req, email);

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/page/{page}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Page<GetUsersResponse>> getUsers(
            @PathVariable int page,
            @RequestParam(defaultValue = "10") int size) {
        
        return ResponseEntity.ok(service.getUsers(page, size));
    }
}
