package com.example.demo.service.impl;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ChangePasswordRequest;
import com.example.demo.dto.GetUsersResponse;
import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.dto.UserDetailsResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
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


    public Page<GetUsersResponse> getUsers(int page, int size) {
            Pageable pageable = PageRequest.of(page, size);
            return repo.getUsers(pageable);
    }

    // public void updateUser

}
