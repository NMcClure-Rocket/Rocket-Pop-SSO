package com.example.rocketpop.controller;

import com.example.rocketpop.model.User;
import com.example.rocketpop.repository.UserDatabase;
import com.example.rocketpop.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserControllerTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserDatabase userDatabase;

    @Autowired
    private JWTUtil jwtUtil;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private String userToken;
    private String adminToken;

    @BeforeEach
    public void setUp() {
        // Initialize MockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Create test users
        User adminUser = new User("admin", passwordEncoder.encode("admin123"), "");
        adminUser.setEmail("admin@test.com");
        adminUser.setTitle("admin");
        adminUser.setLocation(1);
        userDatabase.createUser(adminUser);

        User testUser = new User("testuser", passwordEncoder.encode("user123"), "");
        testUser.setEmail("user@test.com");
        testUser.setTitle("user");
        testUser.setLocation(2);
        userDatabase.createUser(testUser);

        // Generate tokens
        adminToken = jwtUtil.generateAdminToken("admin", "admin@test.com");
        userToken = jwtUtil.generateUserToken("testuser", "user@test.com", "user");
    }

    @AfterEach
    public void tearDown() {
        userDatabase.deleteAllUsers();
    }

    @Test
    public void testGetSelfWithValidToken() throws Exception {
        mockMvc.perform(get("/user/info")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("user@test.com"))
                .andExpect(jsonPath("$.title").value("user"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.location").value(2));
    }

    @Test
    public void testGetSelfWithAdminToken() throws Exception {
        mockMvc.perform(get("/user/info")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.email").value("admin@test.com"))
                .andExpect(jsonPath("$.title").value("admin"))
                .andExpect(jsonPath("$.location").value(1));
    }

    @Test
    public void testGetSelfWithoutToken() throws Exception {
        mockMvc.perform(get("/user/info"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetSelfWithInvalidToken() throws Exception {
        mockMvc.perform(get("/user/info")
                .header("Authorization", "Bearer invalidtoken123"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid or expired token"));
    }

    @Test
    public void testChangePasswordSuccess() throws Exception {
        String passwordChangeJson = objectMapper.writeValueAsString(
            new UserController.PasswordChangeRequest() {{
                setOldPassword("user123");
                setNewPassword("newpassword123");
            }}
        );

        mockMvc.perform(post("/user/updatepassword")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(passwordChangeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password updated successfully"));
    }

    @Test
    public void testChangePasswordWrongOldPassword() throws Exception {
        String passwordChangeJson = objectMapper.writeValueAsString(
            new UserController.PasswordChangeRequest() {{
                setOldPassword("wrongpassword");
                setNewPassword("newpassword123");
            }}
        );

        mockMvc.perform(post("/user/updatepassword")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(passwordChangeJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Current password is incorrect"));
    }

    @Test
    public void testChangePasswordWithoutToken() throws Exception {
        String passwordChangeJson = objectMapper.writeValueAsString(
            new UserController.PasswordChangeRequest() {{
                setOldPassword("user123");
                setNewPassword("newpassword123");
            }}
        );

        mockMvc.perform(post("/user/updatepassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(passwordChangeJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testChangePasswordWithInvalidToken() throws Exception {
        String passwordChangeJson = objectMapper.writeValueAsString(
            new UserController.PasswordChangeRequest() {{
                setOldPassword("user123");
                setNewPassword("newpassword123");
            }}
        );

        mockMvc.perform(post("/user/updatepassword")
                .header("Authorization", "Bearer invalidtoken123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(passwordChangeJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid or expired token"));
    }

    @Test
    public void testChangePasswordEmptyNewPassword() throws Exception {
        String passwordChangeJson = objectMapper.writeValueAsString(
            new UserController.PasswordChangeRequest() {{
                setOldPassword("user123");
                setNewPassword("");
            }}
        );

        mockMvc.perform(post("/user/updatepassword")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(passwordChangeJson))
                .andExpect(status().isBadRequest());
    }
}
