package com.example.rocketpop.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.rocketpop.model.User;
import com.example.rocketpop.repository.UserDatabase;
import com.example.rocketpop.util.PasswordHasher;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
@ActiveProfiles("test")
public class AuthControllerTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserDatabase userDatabase;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final Logger logger = LoggerFactory.getLogger(AuthControllerTests.class);

    @BeforeEach
    public void setUp() {
        // Initialize MockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Create test users
        PasswordHasher passwordHasher = new PasswordHasher();
        String encodedPassword = passwordHasher.rsaEncrypt("admin123");
        User adminUser = new User("admin", encodedPassword, "");
        adminUser.setEmail("admin@test.com");
        adminUser.setTitle("admin");
        userDatabase.createUser(adminUser);

        encodedPassword = passwordHasher.rsaEncrypt("user123");
        User testUser = new User("testuser", encodedPassword, "");
        testUser.setEmail("user@test.com");
        testUser.setTitle("user");
        userDatabase.createUser(testUser);
        logger.info("Created test users");
        logger.info("User count: {}", userDatabase.getAllUsers().size());
    }

    @AfterEach
    public void tearDown() {
        userDatabase.deleteAllUsers();
    }

    @Test
    public void testPing() throws Exception {
        mockMvc.perform(get("/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("online"))
                .andExpect(jsonPath("$.message").value("Rocket Pop SSO API is running"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void testLoginSuccess() throws Exception {
        PasswordHasher passwordHasher = new PasswordHasher();
        String encodedPassword = passwordHasher.rsaEncrypt("admin123");
        String loginJson = objectMapper.writeValueAsString(
            new AuthController.LoginRequest() {{
                setUsername("admin");
                setPassword(encodedPassword);
            }}
        );

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    public void testLoginInvalidUsername() throws Exception {
        PasswordHasher passwordHasher = new PasswordHasher();
        String encodedPassword = passwordHasher.rsaEncrypt("wrongpass");
        String loginJson = objectMapper.writeValueAsString(
            new AuthController.LoginRequest() {{
                setUsername("nonexistent");
                setPassword(encodedPassword);
            }}
        );

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid username or password"));
    }

    @Test
    public void testLoginInvalidPassword() throws Exception {
        PasswordHasher passwordHasher = new PasswordHasher();
        String encodedPassword = passwordHasher.rsaEncrypt("wrongpassword");
        String loginJson = objectMapper.writeValueAsString(
            new AuthController.LoginRequest() {{
                setUsername("admin");
                setPassword(encodedPassword);
            }}
        );

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid username or password"));
    }

    @Test
    public void testLoginEmptyUsername() throws Exception {
        PasswordHasher passwordHasher = new PasswordHasher();
        String encodedPassword = passwordHasher.rsaEncrypt("admin123");
        String loginJson = objectMapper.writeValueAsString(
            new AuthController.LoginRequest() {{
                setUsername("");
                setPassword(encodedPassword);
            }}
        );

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testLoginEmptyPassword() throws Exception {
        String loginJson = objectMapper.writeValueAsString(
            new AuthController.LoginRequest() {{
                setUsername("admin");
                setPassword("");
            }}
        );

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testLoginUserRole() throws Exception {
        PasswordHasher passwordHasher = new PasswordHasher();
        String encodedPassword = passwordHasher.rsaEncrypt("user123");
        String loginJson = objectMapper.writeValueAsString(
            new AuthController.LoginRequest() {{
                setUsername("testuser");
                setPassword(encodedPassword);
            }}
        );

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.message").value("Login successful"));
    }
}
