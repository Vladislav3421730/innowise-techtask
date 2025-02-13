package com.example.innowisetechtask.dto;

import com.example.innowisetechtask.validation.NotDigit;
import com.example.innowisetechtask.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterUserDto {

    @NotBlank(message = "firstName must be not blank")
    @NotDigit(message = "firstName must not contain numbers")
    private String firstName;

    @NotBlank(message = "lastName must be not blank")
    @NotDigit(message = "lastName must not contain numbers")
    private String lastName;

    @Email(message = "Email must contain '@'")
    @NotBlank(message = "email must be not blank")
    private String email;

    @NotBlank(message = "password must be not blank")
    @Password
    private String password;

    @NotBlank(message = "confirm Password must be not blank")
    private String confirmPassword;

    @NotNull(message = "birthday must be not null")
    @PastOrPresent(message = "Birthday must be in the past or present")
    private LocalDate birthday;
}
