package com.example.demo.service.impl;

import java.time.LocalDateTime;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.auth.LoginRequest;
import com.example.demo.dto.auth.LoginResponse;
import com.example.demo.dto.auth.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.exception.customExceptions.PasswordsAreNotMatchingException;
import com.example.demo.exception.customExceptions.UserAlreadyExistsException;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.AuthService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    

    public AuthServiceImpl(UserRepository repo, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

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
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(Role.USER);

        repo.save(user);

    }

    public LoginResponse login(LoginRequest req) {

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

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
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


        String token = jwtTokenProvider.generateToken(user.getEmail());

        LoginResponse response = new LoginResponse();

        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setToken(token);
        response.setExpiryDate(LocalDateTime.now().plusHours(1));

        return response;
    }

}

