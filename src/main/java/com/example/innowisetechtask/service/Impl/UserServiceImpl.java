package com.example.innowisetechtask.service.Impl;

import com.example.innowisetechtask.dto.RegisterUserDto;
import com.example.innowisetechtask.mapper.UserMapper;
import com.example.innowisetechtask.model.User;
import com.example.innowisetechtask.model.Role;
import com.example.innowisetechtask.repository.RoleRepository;
import com.example.innowisetechtask.repository.UserRepository;
import com.example.innowisetechtask.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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
    public void saveUser(RegisterUserDto registerUserDto) {
        User user = userMapper.toNewEntity(registerUserDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("user {}",user);
        Role role = roleRepository.findByName("ROLE_USER");
        user.setRoles(Set.of(role));
        userRepository.save(user);
    }

    @Override
    public boolean existByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public UserDto getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetailsWrapper userDetailsWrapper = (UserDetailsWrapper) authentication.getPrincipal();
            User user = userDetailsWrapper.getUser();
            return userMapper.toDTO(user);
        }
        throw new UserNotFoundException("User wasn't found in Context");
    }
}
