package com.example.innowisetechtask.repository;

import com.example.innowisetechtask.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
