package com.example.innowisetechtask.mapper;

import com.example.innowisetechtask.dto.RegisterUserDto;
import com.example.innowisetechtask.dto.UserDto;
import com.example.innowisetechtask.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toNewEntity(RegisterUserDto registerUserDto);
    UserDto toDto(User user);
}