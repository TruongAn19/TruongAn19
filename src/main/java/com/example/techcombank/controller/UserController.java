package com.example.techcombank.controller;


import com.example.techcombank.models.ResponseObject;
import com.example.techcombank.models.User;
import com.example.techcombank.repositories.UserRepository;
import com.example.techcombank.services.UserDataGeneratorService;
import com.example.techcombank.services.UserService;
import com.example.techcombank.validator.constraint_group.AdvanceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.hateoas.EntityModel;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/User")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController extends BaseController {

    private static final String STATUS_OK = "Oke";
    private static final String ALL_USERS = "Get all user success";
    private static final String QUERY_SUCCESS = "Query user success";
    private static final String INFO_USER = "This is you profile";
    private static final String CREATE_SUCCESS = "Create new user success";
    private static final String UPDATE_SUCCESS = "Update user success";
    private static final String DELETE_SUCCESS = "Delete user success";
    private static final String USER_FOUND = "User found";

    @Autowired
    private UserDataGeneratorService userDataGeneratorService;
    private final UserRepository repository;
    private final UserService userService;
    private final PagedResourcesAssembler<User> pagedResourcesAssembler;
    @Autowired
    public UserController(UserRepository repository, UserService userService, PagedResourcesAssembler<User> pagedResourcesAssembler) {
        this.repository = repository;
        this.userService = userService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping(path = "/users")
    public ResponseEntity<ResponseObject> getAllUsers() {
        List<User> allUser = userService.getAllUsers();
        return buildResponse(STATUS_OK, ALL_USERS, allUser);
    }

    @GetMapping("/userss")
    public PagedModel<EntityModel<User>> getUsers(@PageableDefault(size = 10) Pageable pageable) {
        Page<User> usersPage = userService.getAllUserss(pageable);
        return pagedResourcesAssembler.toModel(usersPage);
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<ResponseObject> getUserById(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return buildResponse(STATUS_OK, QUERY_SUCCESS, user);
    }

    @GetMapping(path = "/myInfo")
    public ResponseEntity<ResponseObject> getMyInfo() {
        return buildResponse(STATUS_OK, INFO_USER, userService.getMyInfo());
    }

    @PostMapping(path = "/createUser")
    public ResponseEntity<ResponseObject> createUser(
            @Validated(AdvanceInfo.class) @ModelAttribute User newUser,
            BindingResult result,
            @RequestParam("image") MultipartFile file) {

        handleValidationErrors(result);
        User user = userService.createNewUser(newUser, file);
        return buildResponse(STATUS_OK, CREATE_SUCCESS, user);
    }

    @PostMapping("/generate")
    public String generateUsers(@RequestParam int numberOfUsers) {
        long startTime = System.currentTimeMillis();
        userDataGeneratorService.generateLargeUserData(numberOfUsers);
        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        System.out.println("Time taken to generate " + numberOfUsers + " users: " + timeTaken + " ms");
        return numberOfUsers + " users have been generated." + "Time taken to generate " + numberOfUsers + " users: " + timeTaken + " ms";
    }

    @PutMapping(path = "/updateUser/{userId}")
    public ResponseEntity<ResponseObject> updateUser(@ModelAttribute User updateNewUser, @PathVariable Long userId) {
        User updatedUser = userService.updateUser(updateNewUser, userId);
        return buildResponse(STATUS_OK, UPDATE_SUCCESS, updatedUser);
    }

    @DeleteMapping(path = "/deleteUser/{userId}")
    public ResponseEntity<ResponseObject> deleteUser(@PathVariable Long userId) {
        String deleteUser = userService.deleteUser(userId);
        return buildResponse(STATUS_OK, DELETE_SUCCESS, userService.getAllUsers());
    }

    @GetMapping("/getUserByUserName")
    public ResponseEntity<ResponseObject> getUserByUserName(@RequestParam String userName) {
        List<User> user = userService.getUserByUserName(userName);
        return buildResponse(STATUS_OK, USER_FOUND, user);
    }

    // Helper method to build ResponseEntity
    private ResponseEntity<ResponseObject> buildResponse(String status, String message, Object data) {
        ResponseObject responseObject = new ResponseObject(status, message, data);
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

}
