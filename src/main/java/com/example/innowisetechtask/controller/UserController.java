package com.example.innowisetechtask.controller;

import com.example.innowisetechtask.dto.RegisterUserDto;
import com.example.innowisetechtask.dto.UpdatePasswordDto;
import com.example.innowisetechtask.dto.UpdateUserDto;
import com.example.innowisetechtask.dto.UserDto;
import com.example.innowisetechtask.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(Model model) {
        model.addAttribute("user", new RegisterUserDto());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(
            @Valid @ModelAttribute("user") RegisterUserDto registerUserDto,
            BindingResult bindingResult,
            Model model) {
        log.info("registerUser {}", registerUserDto);
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", registerUserDto);
            return "registration";
        }
        if (!registerUserDto.getPassword().equals(registerUserDto.getConfirmPassword())) {
            model.addAttribute("passwordsMustBeTheSame", "Passwords must be the same");
            model.addAttribute("user", registerUserDto);
            return "registration";
        }
        if (userService.findByEmail(registerUserDto.getEmail()).isPresent()) {
            model.addAttribute("userAlsoInSystem",
                    String.format("User with email %s already exist", registerUserDto.getEmail()));
            model.addAttribute("user", registerUserDto);
            return "registration";
        }
        userService.saveUser(registerUserDto);
        return "redirect:/login";
    }

    @GetMapping("/user")
    private String getUserPage(Model model, Principal principal) {
        UserDto userDto = userService.getUser(principal);
        model.addAttribute("user", userDto);
        return "user";
    }

    @GetMapping("/user/update")
    private String getUserPageForUpdating(Model model, Principal principal) {
        UpdateUserDto userDto = userService.getUpdatedUser(principal);
        model.addAttribute("user", userDto);
        return "update";
    }

    @PostMapping("/user/update")
    private String updateUser(
            @Valid @ModelAttribute("user") UpdateUserDto userDto,
            BindingResult bindingResult,
            Model model) {
        log.info("updated user {}", userDto);
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDto);
            log.info("error binding");
            return "update";
        }
        Optional<UserDto> userDB = userService.findByEmail(userDto.getEmail());
        if (userDB.isPresent() && !userDB.get().getId().equals(userDto.getId())) {
            model.addAttribute("userAlsoInSystem", String.format("User with email %s already exist", userDto.getEmail()));
            model.addAttribute("user", userDto);
            log.info("error email");
            return "update";
        }
        UserDto updatedUserDto = userService.updateUserPassword(userDto);
        log.info("updatedUserDto email {}", updatedUserDto.getEmail());
        log.info("user email {}", userDto.getEmail());
        log.info("updatedUserDto email equals userDto email: {}", updatedUserDto.getEmail().equals(userDto.getEmail()));

        return userDB.isPresent() ? "redirect:/user" : "redirect:/logout";
    }

    @GetMapping("/user/update/password")
    private String getPageForPasswordUpdating(Model model, Principal principal) {
        UpdatePasswordDto passwordDto = userService.getUserForPasswordUpdating(principal);
        model.addAttribute("user", passwordDto);
        return "updatePassword";
    }

    @PostMapping("/user/update/password")
    private String updateUserPassword(
            @Valid @ModelAttribute("user") UpdatePasswordDto updatePasswordDto,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", updatePasswordDto);
            return "updatePassword";
        }
        if (!updatePasswordDto.getNewPassword().equals(updatePasswordDto.getConfirmPassword())) {
            model.addAttribute("user", updatePasswordDto);
            model.addAttribute("passwordsMustBeTheSame", "Passwords must be the same");
            log.info("error binding");
            return "updatePassword";
        }
        if (!userService.updateUserPassword(updatePasswordDto)) {
            model.addAttribute("user", updatePasswordDto);
            model.addAttribute("passwordsMustBeTheSame", "You entered wrong old password");
            return "updatePassword";
        }
        return "redirect:/logout";
    }
}
