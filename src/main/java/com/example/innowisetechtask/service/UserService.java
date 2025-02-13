package com.example.innowisetechtask.service;

import com.example.innowisetechtask.dto.RegisterUserDto;

public interface UserService {

    void saveUser(RegisterUserDto registerUserDto);
    boolean existByEmail(String email);
}
