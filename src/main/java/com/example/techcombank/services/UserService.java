package com.example.techcombank.services;

import com.example.techcombank.enums.Role;
import com.example.techcombank.exception.ApiException;
import com.example.techcombank.exception.ErrorCode;
import com.example.techcombank.models.ResponseObject;
import com.example.techcombank.models.User;
import com.example.techcombank.repositories.RoleRepository;
import com.example.techcombank.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IStorage storage;


//    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> getAllUsers(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("userName: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return userRepository.findAll();
    }
    //    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<User> getAllUserss(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

//    @PostAuthorize("returnObject.userName == authentication.name")
    public User findById(Long userId)
    {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_CANNOT_FIND));
    }

    public User getMyInfo() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUserName(name)
                .stream().findFirst()
                .orElseThrow(() -> new ApiException(ErrorCode.CANNOT_FIND_USERNAME));
    }

    public User createNewUser(User user, MultipartFile file) {
        existUserName(user.getUserName());
        return saveUser(user, file);
    }

    private void existUserName(String userName) {
        if (userRepository.findByUserName(userName).size() > 0) {
            throw new ApiException(ErrorCode.USER_EXISTED);
        }
    }

    private User saveUser(User user, MultipartFile file) {
        User newUser = new User();
        byte[] generateFilename = storage.storeFile(file);
        newUser.setUserName(user.getUserName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setImage(user.getImage());
        newUser.setRoles(Set.of(Role.USER.name()));
//    newUser.setImage(generateFilename);

        return userRepository.save(newUser);
    }


    public User updateUser(User request, Long userId) {
        Optional<User> existUser = userRepository.findById(userId);

        if(existUser.isPresent()) {
            User updateNewUser = existUser.get();
            updateNewUser.setUserName(request.getUserName());
            updateNewUser.setPassword(passwordEncoder.encode(request.getPassword()));
            updateNewUser.setFirstName(request.getFirstName());
            updateNewUser.setLastName(request.getLastName());
            updateNewUser.setImage(request.getImage());

            return userRepository.save(updateNewUser);
        }
        throw new ApiException(ErrorCode.USER_CANNOT_FIND);
    }

    public String deleteUser(Long userId) {
        boolean existedUser = userRepository.existsById(userId);
         if(existedUser) {
              userRepository.deleteById(userId);
              return "Xoa thanh cong";
         }
        throw new ApiException(ErrorCode.USER_CANNOT_FIND);
    }

    public List<User> getUserByUserName(String userName) {
        List<User> user = userRepository.findByUserName(userName);
        if (user.isEmpty()) {
            throw new ApiException(ErrorCode.CANNOT_FIND_USERNAME);
        }
        return user;
    }

    public Page<User> getUsersWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

}
