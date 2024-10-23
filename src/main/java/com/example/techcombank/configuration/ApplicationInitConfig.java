package com.example.techcombank.configuration;

import com.example.techcombank.enums.Role;
import com.example.techcombank.models.User;
import com.example.techcombank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.HashSet;

@Configuration
public class ApplicationInitConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {
           if(userRepository.findByUserName("admin").isEmpty()) {
               var roles = new HashSet<String>();
               roles.add(Role.ADMIN.name());
               User userAdmin = new User();
               userAdmin.setUserName("admin");
               userAdmin.setPassword(passwordEncoder.encode("admin"));
               userAdmin.setFirstName("Admin");
               userAdmin.setLastName("Admin");
               userAdmin.setRoles(roles);
               userRepository.save(userAdmin);
           }
        };
    }
}
