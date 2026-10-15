package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ChangePasswordRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.dto.UserDetailsResponse;
import com.example.demo.entity.User;
import com.example.demo.exception.customExceptions.PasswordsAreNotMatchingException;
import com.example.demo.exception.customExceptions.UserAlreadyExistsException;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtTokenProvider;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;

    private final JwtTokenProvider jwtTokenProvider;

    public void register(RegisterRequest req) {

        if (!req.getPassword().equals(req.getRepeatPassword())) {

            throw new PasswordsAreNotMatchingException("Passwords are not matching.");

        }
        if (repo.existsByEmail(req.getEmail())) {
            throw new UserAlreadyExistsException(
                    "An account with this email already exists.");
        }

        User user = new User();

        user.setEmail(req.getEmail().trim().toLowerCase());
        user.setName(req.getName());
        user.setSurname(req.getSurname());
        user.setPassword(req.getPassword());

        repo.save(user);

    }

    public String login(LoginRequest req) {

        User user = repo.findByEmail(
                req.getEmail().trim().toLowerCase()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getLockTime() != null) {
            if (user.getLockTime().plusSeconds(30).isBefore(LocalDateTime.now())) {

                user.setLockTime(null);
                user.setFailedAttempts(0);
                user.setEnabled(true);
                repo.save(user);

            } else {

                throw new BadCredentialsException("your profile is blocked for 30 seconds.");
            }
        }

        if (!user.isEnabled()) {
            throw new BadCredentialsException("Account is disabled.");
        }

        if (!req.getPassword().equals(user.getPassword())) {

            user.setFailedAttempts(user.getFailedAttempts() + 1);

            if (user.getFailedAttempts() == 3) {
                user.setEnabled(false);
                user.setLockTime(LocalDateTime.now()); 
            }

            repo.save(user);

            throw new BadCredentialsException(
                    "Password or email is incorrect.");
        }

        user.setLockTime(null);
        user.setFailedAttempts(0);
        user.setLastLoginDate(LocalDateTime.now());
        repo.save(user);

        return jwtTokenProvider.generateToken(user.getEmail());
    }

    public UserDetailsResponse getDetails(String email) {
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        UserDetailsResponse response = new UserDetailsResponse();

        response.setEmail(email);
        response.setId(user.getId());
        response.setName(user.getName());
        response.setSurname(user.getSurname());

        response.setRegisterDate(user.getRegisterDate());
        response.setLastLoginDate(user.getLastLoginDate());

        return response;
    }

    public void updateProfile(UpdateProfileRequest req, String email) {

        User user = repo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setName(req.getName());
        user.setSurname(req.getSurname());

        repo.save(user);

    }

    public void changePassword(ChangePasswordRequest req, String email) {

        if (!req.getNewPassword().equals(req.getRepeatNewPassword())) {

            throw new BadCredentialsException("Passwords are not matching.");

        }

        User user = repo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!req.getCurrentPassword().equals(user.getPassword())) {

            throw new BadCredentialsException("Your current passwd is wrong");
        }

        user.setPassword(req.getNewPassword());
        repo.save(user);


    }

}
