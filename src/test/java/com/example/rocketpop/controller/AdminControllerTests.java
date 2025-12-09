package com.example.rocketpop.controller;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.rocketpop.model.User;
import com.example.rocketpop.repository.UserDatabase;
import com.example.rocketpop.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
public class AdminControllerTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserDatabase userDatabase;

    @Autowired
    private JWTUtil jwtUtil;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private String adminToken;
    private String userToken;

    @BeforeEach
    public void setUp() {
        // Initialize MockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Create admin user
        User adminUser = new User("admin", passwordEncoder.encode("admin123"), "");
        adminUser.setEmail("admin@test.com");
        adminUser.setTitle("admin");
        adminUser.setLocation(1);
        userDatabase.createUser(adminUser);

        // Create regular user
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

    // Test Create User
    @Test
    public void testCreateUserSuccess() throws Exception {
        String userJson = objectMapper.writeValueAsString(
            new AdminController.UserRequest() {{
                setUsername("newuser");
                setPassword("password123");
                setEmail("newuser@test.com");
                setTitle("user");
            }}
        );

        mockMvc.perform(post("/admin/user/create")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User created successfully"))
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("newuser@test.com"))
                .andExpect(jsonPath("$.title").value("user"));
    }

    @Test
    public void testCreateUserUnauthorized() throws Exception {
        String userJson = objectMapper.writeValueAsString(
            new AdminController.UserRequest() {{
                setUsername("newuser");
                setPassword("password123");
                setEmail("newuser@test.com");
                setTitle("user");
            }}
        );

        mockMvc.perform(post("/admin/user/create")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Unauthorized - Admin access required"));
    }

    @Test
    public void testCreateUserDuplicate() throws Exception {
        String userJson = objectMapper.writeValueAsString(
            new AdminController.UserRequest() {{
                setUsername("testuser");
                setPassword("password123");
                setEmail("duplicate@test.com");
                setTitle("user");
            }}
        );

        mockMvc.perform(post("/admin/user/create")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Username already exists"));
    }

    // Test Create Admin
    @Test
    public void testCreateAdminSuccess() throws Exception {
        String adminJson = objectMapper.writeValueAsString(
            new AdminController.UserRequest() {{
                setUsername("newadmin");
                setPassword("admin123");
                setEmail("newadmin@test.com");
            }}
        );

        mockMvc.perform(post("/admin/adminuser/create")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(adminJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Admin user created successfully"))
                .andExpect(jsonPath("$.username").value("newadmin"))
                .andExpect(jsonPath("$.title").value("admin"));
    }

    @Test
    public void testCreateAdminUnauthorized() throws Exception {
        String adminJson = objectMapper.writeValueAsString(
            new AdminController.UserRequest() {{
                setUsername("newadmin");
                setPassword("admin123");
                setEmail("newadmin@test.com");
            }}
        );

        mockMvc.perform(post("/admin/adminuser/create")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(adminJson))
                .andExpect(status().isUnauthorized());
    }

    // Test Get All Users
    @Test
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/admin/user/getall")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].username", hasItems("admin", "testuser")));
    }

    @Test
    public void testGetAllUsersUnauthorized() throws Exception {
        mockMvc.perform(get("/admin/user/getall")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testSearchUsers() throws Exception {
        mockMvc.perform(get("/admin/user/getall")
                .header("Authorization", "Bearer " + adminToken)
                .param("username", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username").value("testuser"));
    }

    // Test Get Specific User
    @Test
    public void testGetUserSuccess() throws Exception {
        mockMvc.perform(get("/admin/user/get/testuser")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("user@test.com"))
                .andExpect(jsonPath("$.title").value("user"))
                .andExpect(jsonPath("$.location").value(2));
    }

    @Test
    public void testGetUserNotFound() throws Exception {
        mockMvc.perform(get("/admin/user/get/nonexistent")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    @Test
    public void testGetUserUnauthorized() throws Exception {
        mockMvc.perform(get("/admin/user/get/testuser")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isUnauthorized());
    }

    // Test Edit User
    @Test
    public void testEditUserSuccess() throws Exception {
        String userJson = objectMapper.writeValueAsString(
            new AdminController.UserRequest() {{
                setUsername("testuser");
                setEmail("newemail@test.com");
                setTitle("manager");
                setLocation("3");
            }}
        );

        mockMvc.perform(put("/admin/user/edit")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User updated successfully"))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("newemail@test.com"))
                .andExpect(jsonPath("$.title").value("manager"))
                .andExpect(jsonPath("$.location").value(3));
    }

    @Test
    public void testEditUserChangePassword() throws Exception {
        String userJson = objectMapper.writeValueAsString(
            new AdminController.UserRequest() {{
                setUsername("testuser");
                setPassword("newpassword456");
                setEmail("user@test.com");
                setTitle("user");
            }}
        );

        mockMvc.perform(put("/admin/user/edit")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User updated successfully"));
    }

    @Test
    public void testEditUserNotFound() throws Exception {
        String userJson = objectMapper.writeValueAsString(
            new AdminController.UserRequest() {{
                setUsername("nonexistent");
                setEmail("test@test.com");
                setTitle("user");
            }}
        );

        mockMvc.perform(put("/admin/user/edit")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    @Test
    public void testEditUserUnauthorized() throws Exception {
        String userJson = objectMapper.writeValueAsString(
            new AdminController.UserRequest() {{
                setUsername("testuser");
                setEmail("newemail@test.com");
            }}
        );

        mockMvc.perform(put("/admin/user/edit")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isUnauthorized());
    }

    // Test Delete User
    @Test
    public void testDeleteUserSuccess() throws Exception {
        mockMvc.perform(post("/admin/user/delete/testuser")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User deleted successfully"))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        mockMvc.perform(post("/admin/user/delete/nonexistent")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    @Test
    public void testDeleteUserUnauthorized() throws Exception {
        mockMvc.perform(post("/admin/user/delete/testuser")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testDeleteUserWithoutToken() throws Exception {
        mockMvc.perform(post("/admin/user/delete/testuser"))
                .andExpect(status().isBadRequest());
    }
}
