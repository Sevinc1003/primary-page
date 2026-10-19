package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.*;
import com.example.demo.service.UserService;
import com.example.demo.utils.log.ActionLogger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/details")
    public ResponseEntity<UserDetailsResponse> getDetails(
            Authentication authentication) {

        String email = authentication.getName();

        try {
            UserDetailsResponse response = service.getDetails(email);
            
            ActionLogger.logSuccess("getDetails", HttpStatus.OK.value(), email);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ActionLogger.logError("getDetails", HttpStatus.BAD_REQUEST.value(), e.getMessage(), email);
            throw e;
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(@Valid @RequestBody UpdateProfileRequest req,
            Authentication authentication) {

        String email = authentication.getName();

        try {
            service.updateProfile(req, email);
            
            ActionLogger.logSuccess("updateProfile", HttpStatus.NO_CONTENT.value(), email);
            
            return ResponseEntity.noContent().build();
            
        } catch (Exception e) {
            ActionLogger.logError("updateProfile", HttpStatus.BAD_REQUEST.value(), e.getMessage(), email);
            throw e;
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest req,
            Authentication authentication,
            HttpServletRequest request) {

        String email = authentication.getName();

        try {
            service.changePassword(req, email);

            HttpSession session = request.getSession(false);

            if (session != null) {
                session.invalidate();
            }

            ActionLogger.logSuccess("changePassword", HttpStatus.NO_CONTENT.value(), email);

            return ResponseEntity.noContent().build();
            
        } catch (Exception e) {
            ActionLogger.logError("changePassword", HttpStatus.BAD_REQUEST.value(), e.getMessage(), email);
            throw e;
        }
    }

    @GetMapping("/page/{page}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Page<GetUsersResponse>> getUsers(
            @PathVariable int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Page<GetUsersResponse> response = service.getUsers(page, size);
            
            ActionLogger.logSuccess("getUsers (səhifə: " + page + ")", HttpStatus.OK.value(), null);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ActionLogger.logError("getUsers (səhifə: " + page + ")", HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            throw e;
        }
    }
}