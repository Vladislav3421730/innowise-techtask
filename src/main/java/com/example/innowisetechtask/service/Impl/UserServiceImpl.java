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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void saveUser(RegisterUserDto registerUserDto) {
        User user = userMapper.toNewEntity(registerUserDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("user {}", user);

        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(() ->
                new RoleNotFoundException("Role user not found in db"));
        user.setRoles(Set.of(role));
        userRepository.save(user);
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(userMapper::toDto);
    }

    @Override
    public UserDto getUser(Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow(() ->
                new UserNotFoundException(String.format("User with email %s wasn't found", principal.getName())));
        return userMapper.toDto(user);
    }

    @Override
    public UpdatePasswordDto getUserForPasswordUpdating(Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow(() ->
                new UserNotFoundException(String.format("User with email %s wasn't found", principal.getName())));
        return new UpdatePasswordDto(user.getId());
    }

    @Override
    public UpdateUserDto getUpdatedUser(Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow(() ->
                new UserNotFoundException(String.format("User with email %s wasn't found, updating impossible", principal.getName())));
        UpdateUserDto updateUserDto = userMapper.toUpdatedUserDto(user);
        log.info("info {}", updateUserDto);
        return updateUserDto;
    }

    @Override
    public boolean updateUserPassword(UpdatePasswordDto passwordDto) {
        User user = userRepository.findById(passwordDto.getId()).orElseThrow(() ->
                new UserNotFoundException("User not found"));
        if(!passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public UserDto updateUserPassword(UpdateUserDto updateUserDto) {
        return Optional.ofNullable(updateUserDto)
                .map(UpdateUserDto::getId)
                .flatMap(userRepository::findById)
                .map(user -> userMapper.updateUser(user, updateUserDto))
                .map(userRepository::save)
                .map(userMapper::toDto)
                .orElseThrow(() -> {
                    log.error("User can not update");
                    return new UserNotFoundException("User not found");
                });
    }
}
