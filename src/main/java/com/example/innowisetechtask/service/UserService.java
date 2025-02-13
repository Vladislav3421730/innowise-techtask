package com.example.innowisetechtask.service;

import com.example.innowisetechtask.dto.RegisterUserDto;
import com.example.innowisetechtask.dto.UpdatePasswordDto;
import com.example.innowisetechtask.dto.UpdateUserDto;
import com.example.innowisetechtask.dto.UserDto;

import java.security.Principal;
import java.util.Optional;

public interface UserService {

    void saveUser(RegisterUserDto registerUserDto);

    Optional<UserDto> findByEmail(String email);

    UserDto getUser(Principal principal);

    UpdatePasswordDto getUserForPasswordUpdating(Principal principal);

    UpdateUserDto getUpdatedUser(Principal principal);

    UserDto updateUserPassword(UpdateUserDto updateUserDto);

    boolean updateUserPassword(UpdatePasswordDto passwordDto);

}
