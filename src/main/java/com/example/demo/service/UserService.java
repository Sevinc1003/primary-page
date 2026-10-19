package com.example.demo.service;

import org.springframework.data.domain.Page;

import org.springframework.stereotype.Service;

import com.example.demo.dto.ChangePasswordRequest;
import com.example.demo.dto.GetUsersResponse;
import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.dto.UserDetailsResponse;


@Service
public interface UserService {

    

    public UserDetailsResponse getDetails(String email);

    public void updateProfile(UpdateProfileRequest req, String email);

    public void changePassword(ChangePasswordRequest req, String email);

    public Page<GetUsersResponse> getUsers(int page, int size);

}
