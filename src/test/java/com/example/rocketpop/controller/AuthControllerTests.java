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
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
public class AuthControllerTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserDatabase userDatabase;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp() {
        // Initialize MockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Create test users
        User adminUser = new User("admin", passwordEncoder.encode("admin123"), "");
        adminUser.setEmail("admin@test.com");
        adminUser.setTitle("admin");
        userDatabase.createUser(adminUser);

        User testUser = new User("testuser", passwordEncoder.encode("user123"), "");
        testUser.setEmail("user@test.com");
        testUser.setTitle("user");
        userDatabase.createUser(testUser);
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
        String loginJson = objectMapper.writeValueAsString(
            new AuthController.LoginRequest() {{
                setUsername("admin");
                setPassword("admin123");
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
        String loginJson = objectMapper.writeValueAsString(
            new AuthController.LoginRequest() {{
                setUsername("nonexistent");
                setPassword("wrongpass");
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
        String loginJson = objectMapper.writeValueAsString(
            new AuthController.LoginRequest() {{
                setUsername("admin");
                setPassword("wrongpassword");
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
        String loginJson = objectMapper.writeValueAsString(
            new AuthController.LoginRequest() {{
                setUsername("");
                setPassword("admin123");
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
        String loginJson = objectMapper.writeValueAsString(
            new AuthController.LoginRequest() {{
                setUsername("testuser");
                setPassword("user123");
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
