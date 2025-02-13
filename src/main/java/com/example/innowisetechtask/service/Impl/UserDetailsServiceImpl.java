package com.example.innowisetechtask.service.Impl;

import com.example.innowisetechtask.model.User;
import com.example.innowisetechtask.repository.UserRepository;
import com.example.innowisetechtask.wrapper.UserDetailsWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found ", email)));
        return new UserDetailsWrapper(user);
    }

}
