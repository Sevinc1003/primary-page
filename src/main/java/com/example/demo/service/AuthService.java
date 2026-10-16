package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.exception.customExceptions.PasswordsAreNotMatchingException;
import com.example.demo.exception.customExceptions.UserAlreadyExistsException;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtTokenProvider;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class AuthService {

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


}
