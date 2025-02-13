package com.example.innowisetechtask.mapper;

import com.example.innowisetechtask.dto.RegisterUserDto;
import com.example.innowisetechtask.dto.RoleDto;
import com.example.innowisetechtask.dto.UpdateUserDto;
import com.example.innowisetechtask.dto.UserDto;
import com.example.innowisetechtask.model.Role;
import com.example.innowisetechtask.model.User;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toNewEntity(RegisterUserDto registerUserDto);

    UserDto toDto(User user);

    UpdateUserDto toUpdatedUserDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User updateUser(@MappingTarget User user, UpdateUserDto updateUserDto);

    default RoleDto mapFromRoleToRoleDto(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}