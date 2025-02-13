package com.example.innowisetechtask.dto;

import com.example.innowisetechtask.validation.Password;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePasswordDto {

    private Long id;
    @NotBlank(message = "old password should be not blank")
    private String oldPassword;
    @NotBlank(message = "new password should be not blank")
    @Password
    private String newPassword;
    @NotBlank(message = "confirm password should be not blank")
    private String confirmPassword;

    public UpdatePasswordDto(Long id) {
        this.id = id;
    }
}
