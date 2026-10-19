package com.example.demo.service.impl;

import java.util.Optional;

import com.example.demo.entity.User;
import com.example.demo.exception.customExceptions.EmailAlreadyTakenException;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CmsUserService;

import jakarta.validation.constraints.Email;

public class CmsUserServiceImpl implements CmsUserService{

    private final UserRepository repo;

    public CmsUserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }


    @Override
    public void updateUserEmail(String prevEmail, String newEmail) {

        if(repo.existsByEmail(newEmail)){
            throw new EmailAlreadyTakenException();
        }

        User user = repo.findByEmail(prevEmail).get();
        user.setEmail(newEmail);
        repo.save(user);
    }

    @Override
    public void updateUserFullname(String email, String newName, String newSurname) {

        
        User user = repo.findByEmail(email).get();

        user.setName(newName);
        user.setSurname(newSurname);

        repo.save(user);
    }


    @Override
    public void setEnableUser(String email, boolean status) {

        User user = repo.findByEmail(email).get();

        user.setEnabled(status);

        repo.save(user);
    }


}
