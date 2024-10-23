package com.example.techcombank.repositories;

import com.example.techcombank.models.Role;
import com.example.techcombank.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;


public interface RoleRepository extends JpaRepository<Role, String> {

    Set<Role> findByUsers_Id(Long userId);  // Correctly refers to the users' id

}
