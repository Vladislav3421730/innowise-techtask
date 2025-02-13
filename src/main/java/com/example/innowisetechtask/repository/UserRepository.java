package com.example.innowisetechtask.repository;

import com.example.innowisetechtask.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
