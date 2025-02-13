package com.example.innowisetechtask.dto;

import com.example.innowisetechtask.validation.NotDigit;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserDto {
    private Long id;
    @NotBlank(message = "first name must be not blank")
    @NotDigit(message = "first name must not contain numbers")
    private String firstName;

    @NotBlank(message = "last name must be not blank")
    @NotDigit(message = "last name must not contain numbers")
    private String lastName;
    @Email(message = "Email must contain '@'")
    @NotBlank(message = "email must be not blank")
    private String email;
    @NotNull(message = "birthday must be not null")
    @PastOrPresent(message = "Birthday must be in the past or present")
    private LocalDate birthday;
}
