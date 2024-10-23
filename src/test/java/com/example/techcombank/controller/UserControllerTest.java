package com.example.techcombank.controller;

import com.example.techcombank.models.User;
import com.example.techcombank.repositories.UserRepository;
import com.example.techcombank.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebMvcTest(UserController.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;  // Mock đối tượng UserService

    @Autowired
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;  // Inject mock vào UserController

    @Before
    public void setup() {
        // Khởi tạo các mock
        MockitoAnnotations.openMocks(this);

        // Thiết lập mockMvc để test API
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    // Test cho trường hợp có lỗi validation
    @Test
    public void testCreateUser_ValidationError() throws Exception {
        User newUser = new User();
        newUser.setUserName("testUser");

        MockMultipartFile mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image".getBytes());

        // Giả lập lỗi validation
        when(userService.createNewUser(any(User.class), any())).thenThrow(new RuntimeException("Validation error"));

        mockMvc.perform(multipart("/api/v1/User/createUser")
                .file(mockFile)
                .param("userName", newUser.getUserName())
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest()); // Kiểm tra status 400
    }

    // Test cho API /createUser
    @Test
    public void testCreateUser_Success() throws Exception {
        // Giả lập đối tượng User và file tải lên
        User newUser = new User();
        newUser.setUserName("testUser");
        newUser.setPassword("A19102002n");
        newUser.setLastName("An");
        newUser.setFirstName("An");

        MockMultipartFile mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image".getBytes());

        when(userService.createNewUser(any(User.class), any())).thenReturn(newUser);

        mockMvc.perform(multipart("/api/v1/User/createUser")
                .file(mockFile)
                .param("userName", newUser.getUserName())
                .param("password", newUser.getPassword())
                .param("firstName", newUser.getFirstName())
                .param("lastName", newUser.getLastName())
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())  // Kiểm tra status 200
                .andExpect(jsonPath("$.status").value("Oke"))  // Kiểm tra field "status"
                .andExpect(jsonPath("$.message").value("Create new user success"));  // Kiểm tra field "message"
    }

    @Test
    public void testGetAllUsers_Success() throws Exception {
        User user1 = new User();
        user1.setUserName("user1");
        user1.setFirstName("First1");
        user1.setLastName("Last1");

        User user2 = new User();
        user2.setUserName("user2");
        user2.setFirstName("First2");
        user2.setLastName("Last2");

        List<User> users = Arrays.asList(user1, user2);


        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/v1/User/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Kiểm tra status 200
                .andExpect(jsonPath("$.status").value("Oke"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].userName").value("user1"))
                .andExpect(jsonPath("$.data[1].userName").value("user2"));
    }

    @Test
    public void testUpdateUser_Success() throws Exception {
        Long userId = 1L;

        User existingUser = new User();
        existingUser.setUserName("oldUser");
        existingUser.setPassword("oldPassword");
        existingUser.setFirstName("OldFirst");
        existingUser.setLastName("OldLast");

        User updatedUser = new User();
        updatedUser.setUserName("newUser");
        updatedUser.setPassword("newPassword");
        updatedUser.setFirstName("NewFirst");
        updatedUser.setLastName("NewLast");

        when(userService.updateUser(any(User.class), any(Long.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/v1/User/updateUser/{userId}", userId)
                .param("userName", updatedUser.getUserName())
                .param("password", updatedUser.getPassword())
                .param("firstName", updatedUser.getFirstName())
                .param("lastName", updatedUser.getLastName())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())  // Kiểm tra status 200
                .andExpect(jsonPath("$.status").value("Oke"))
                .andExpect(jsonPath("$.message").value("Update user success"));
    }



    @Test
    public void testDeleteUser_Success() throws Exception {
        Long userId = 1L;


        when(userService.deleteUser(any(Long.class))).thenReturn(String.valueOf(true));


        mockMvc.perform(delete("/api/v1/User/deleteUser/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Check for 200 status
                .andExpect(jsonPath("$.status").value("Oke"))
                .andExpect(jsonPath("$.message").value("Delete user success"));
    }


}
