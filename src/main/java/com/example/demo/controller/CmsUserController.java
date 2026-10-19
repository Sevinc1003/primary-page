package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.CmsUserService;
import com.example.demo.utils.log.ActionLogger;

@RestController
@RequestMapping("/api/cms/users")
public class CmsUserController {

    private final CmsUserService service;

    public CmsUserController(CmsUserService cmsUserService) {
        this.service = cmsUserService;
    }

    @PatchMapping("/update-email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUserEmail(
            @RequestParam String prevEmail, 
            @RequestParam String newEmail) {
        
        String actionName = "updateUserEmail (Hədəf: " + prevEmail + " -> " + newEmail + ")";

        try {
            service.updateUserEmail(prevEmail, newEmail);
            
            // Uğurlu əməliyyat loqlanır (200 OK)
            ActionLogger.logSuccess(actionName, HttpStatus.OK.value(), null);
            
            return ResponseEntity.ok("Email updated successfully.");
            
        } catch (Exception e) {
            // Xəta loqlanır
            ActionLogger.logError(actionName, HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            throw e;
        }
    }

    @PatchMapping("/update-fullname")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUserFullname(
            @RequestParam String email, 
            @RequestParam String newName, 
            @RequestParam String newSurname) {
        
        String actionName = "updateUserFullname (Hədəf: " + email + ")";

        try {
            service.updateUserFullname(email, newName, newSurname);
            
            ActionLogger.logSuccess(actionName, HttpStatus.OK.value(), null);
            
            return ResponseEntity.ok("Updated successfully.");
            
        } catch (Exception e) {
            ActionLogger.logError(actionName, HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            throw e;
        }
    }

    @PatchMapping("/change-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> setEnableUser(
            @RequestParam String email, 
            @RequestParam boolean status) {
        
        String actionName = "setEnableUser (Hədəf: " + email + ", Yeni status: " + status + ")";

        try {
            service.setEnableUser(email, status);
            String responseMessage = status ? "User is enabled." : "User is disabled.";
            
            ActionLogger.logSuccess(actionName, HttpStatus.OK.value(), null);
            
            return ResponseEntity.ok(responseMessage);
            
        } catch (Exception e) {
            ActionLogger.logError(actionName, HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            throw e;
        }
    }
}