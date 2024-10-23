package com.example.techcombank.database;

import com.example.techcombank.models.User;
import com.example.techcombank.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Set<String> roleUser = new HashSet<>();
                roleUser.add("user");
//                User user1 = new User("TruongAn", "1234", "An", "Truong", "", "roleUser");
//                User user2 = new User("TruongAn2", "1234", "An", "Truong", "", "roleUser");
//                logger.info("insert data: " + userRepository.save(user1));
//                logger.info("insert data: " + userRepository.save(user2));
            }
        };
    }
}
