package com.example.innowisetechtask.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthday;
    private String password;
    private Set<RoleDto> roles;
}
