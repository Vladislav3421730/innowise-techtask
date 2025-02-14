package com.example.innowisetechtask.service.Impl;

import com.example.innowisetechtask.dto.RegisterUserDto;
import com.example.innowisetechtask.dto.UpdatePasswordDto;
import com.example.innowisetechtask.dto.UpdateUserDto;
import com.example.innowisetechtask.dto.UserDto;
import com.example.innowisetechtask.exception.RoleNotFoundException;
import com.example.innowisetechtask.exception.UserNotFoundException;
import com.example.innowisetechtask.mapper.UserMapper;
import com.example.innowisetechtask.model.User;
import com.example.innowisetechtask.model.Role;
import com.example.innowisetechtask.repository.RoleRepository;
import com.example.innowisetechtask.repository.UserRepository;
import com.example.innowisetechtask.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void saveUser(RegisterUserDto registerUserDto) {
        log.info("Attempting to save user with email: {}", registerUserDto.getEmail());
        User user = userMapper.toNewEntity(registerUserDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(() -> {
            log.error("Role 'ROLE_USER' not found in the database.");
            return new RoleNotFoundException("Role 'ROLE_USER' not found in the database.");
        });

        user.setRoles(Set.of(role));
        userRepository.save(user);
        log.info("User with email: {} has been successfully saved.", user.getEmail());
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        log.info("Searching for user with email: {}", email);
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            log.info("User with email: {} found.", email);
        } else {
            log.warn("User with email: {} not found.", email);
        }
        return user.map(userMapper::toDto);
    }

    @Override
    public UserDto getUser(Principal principal) {
        String email = principal.getName();
        log.info("Retrieving user with email: {}", email);
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("User with email: {} wasn't found.", email);
            return new UserNotFoundException(String.format("User with email %s wasn't found", email));
        });
        return userMapper.toDto(user);
    }

    @Override
    public UpdatePasswordDto getUserForPasswordUpdating(Principal principal) {
        String email = principal.getName();
        log.info("Retrieving user for password update with email: {}", email);
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("User with email: {} wasn't found.", email);
            return new UserNotFoundException(String.format("User with email %s wasn't found", email));
        });
        return new UpdatePasswordDto(user.getId());
    }

    @Override
    public UpdateUserDto getUpdatedUser(Principal principal) {
        String email = principal.getName();
        log.info("Retrieving user for updating with email: {}", email);
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("User with email: {} wasn't found, updating impossible.", email);
            return new UserNotFoundException(String.format("User with email %s wasn't found, updating impossible", email));
        });
        UpdateUserDto updateUserDto = userMapper.toUpdatedUserDto(user);
        log.info("User data retrieved for update: {}", updateUserDto);
        return updateUserDto;
    }

    @Override
    public boolean updateUserPassword(UpdatePasswordDto passwordDto) {
        log.info("Attempting to update password for user ID: {}", passwordDto.getId());
        User user = userRepository.findById(passwordDto.getId()).orElseThrow(() -> {
            log.error("User with ID: {} not found.", passwordDto.getId());
            return new UserNotFoundException("User not found");
        });
        if (!passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())) {
            log.warn("Old password does not match for user ID: {}", passwordDto.getId());
            return false;
        }
        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);
        log.info("Password updated successfully for user ID: {}", passwordDto.getId());
        return true;
    }

    @Override
    @Transactional
    public UserDto updateUser(UpdateUserDto updateUserDto) {
        log.info("Attempting to update user with ID: {}", updateUserDto.getId());
        return Optional.of(updateUserDto)
                .map(UpdateUserDto::getId)
                .flatMap(userRepository::findById)
                .map(user -> {
                    User updatedUser = userMapper.updateUser(user, updateUserDto);
                    log.info("User with ID: {} has been updated.", updatedUser.getId());
                    return updatedUser;
                })
                .map(userRepository::save)
                .map(userMapper::toDto)
                .orElseThrow(() -> {
                    log.error("User with ID: {} cannot be updated because they were not found.", updateUserDto.getId());
                    return new UserNotFoundException("User not found");
                });
    }
}
