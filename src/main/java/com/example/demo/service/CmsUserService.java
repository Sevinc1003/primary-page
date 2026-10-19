package com.example.demo.service;

public interface CmsUserService {

    public void updateUserEmail(String prevEmail, String newEmail);

    public void updateUserFullname(String email, String newName, String newSurname);

    public void setEnableUser(String email, boolean status);



}
