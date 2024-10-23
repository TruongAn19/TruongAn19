package com.example.techcombank.services;

import com.example.techcombank.enums.Role;
import com.example.techcombank.models.User;
import com.example.techcombank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
@Service
public class UserDataGeneratorService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void generateLargeUserData(int numberOfUsers) {
        for (int i = 0; i < numberOfUsers; i++) {
            User user = new User();
            user.setUserName("user" + i);
            user.setPassword(passwordEncoder.encode("password" + i));
            user.setFirstName("FirstName" + i);
            user.setLastName("LastName" + i);
            user.setRoles(Set.of(Role.USER.name()));
            userRepository.save(user);
        }
    }
}
