package com.example.innowisetechtask.controller;

import com.example.innowisetechtask.dto.RegisterUserDto;
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
        if (userService.existByEmail(registerUserDto.getEmail())) {
            model.addAttribute("userAlsoInSystem",
                    String.format("User with email %s already exist", registerUserDto.getEmail()));
            model.addAttribute("user", registerUserDto);
            return "registration";

        }
        userService.saveUser(registerUserDto);
        return "redirect:/login";
    }

    @GetMapping("/user")
    private String getUserPage() {
        return "user";
    }
}
