package com.example.innowisetechtask.dto;

import com.example.innowisetechtask.model.Role;
import com.example.innowisetechtask.validation.NotDigit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserDto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "first_name")
    @NotBlank(message = "firstName must be not blank")
    @NotDigit(message = "firstName must not contain numbers")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "lastName must be not blank")
    @NotDigit(message = "lastName must not contain numbers")
    private String lastName;

    @Column(name = "email", unique = true)
    @Email(message = "Email must contain '@'")
    @NotBlank(message = "Email must not be blank")
    private String email;

    @Column(name = "birthday")
    @PastOrPresent(message = "Birthday must be in the past or present")
    @NotNull(message = "birthday must be not null")
    private LocalDate birthday;

    @Column(name = "password")
    @NotBlank(message = "password must be not blank")
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
