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
import com.example.rocketpop.util.PasswordHasher;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final Logger logger = LoggerFactory.getLogger(AdminControllerTests.class);

    private String adminToken;
    private String userToken;

    @Autowired
    private PasswordHasher passwordHasher;

    @BeforeEach
    public void setUp() {
        // Initialize MockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Create admin user
        String adminEncryptedPassword = passwordHasher.rsaEncrypt("admin123");
        String hashedAdminPassword = passwordHasher.hashPassword(adminEncryptedPassword, "");
        User adminUser = new User("admin", hashedAdminPassword, "");
        adminUser.setEmail("admin@test.com");
        adminUser.setTitle("admin");
        adminUser.setLocation(1);
        userDatabase.createUser(adminUser);

        // Create regular user
        String userEncryptedPassword = passwordHasher.rsaEncrypt("user123");
        String hashedUserPassword = passwordHasher.hashPassword(userEncryptedPassword, "");
        User testUser = new User("testuser", hashedUserPassword, "");
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
        logger.info("testCreateUserSuccess called");
        String encodedPassword = passwordHasher.rsaEncrypt("password123");
        String userJson = objectMapper.writeValueAsString(
                new User("newuser", encodedPassword, "user")
        );

        mockMvc.perform(post("/admin/user/create")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User created successfully"))
                .andExpect(jsonPath("$.username").value("newuser"));
    }

    @Test
    public void testCreateUserUnauthorized() throws Exception {
        logger.info("testCreateUserUnauthorized called");
        String encodedPassword = passwordHasher.rsaEncrypt("password123");
        String userJson = objectMapper.writeValueAsString(
                new User("newuser", encodedPassword, "user")
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
        logger.info("testCreateUserDuplicate called");
        String encodedPassword = passwordHasher.rsaEncrypt("password123");
        User user = new User("testuser", encodedPassword, "user");
        user.setEmail("newuser@test.com");
        user.setTitle("user");
        user.setLocation(2);
        String userJson = objectMapper.writeValueAsString(
                user
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
        logger.info("testCreateAdminSuccess called");
        String encodedPassword = passwordHasher.rsaEncrypt("admin123");
        User adminUser = new User("newadmin", encodedPassword, "admin");
        adminUser.setEmail("newadmin@test.com");
        adminUser.setTitle("admin");
        adminUser.setLocation(1);
        String adminJson = objectMapper.writeValueAsString(
                adminUser
        );

        mockMvc.perform(post("/admin/user/create")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(adminJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newadmin"))
                .andExpect(jsonPath("$.title").value("admin"));
    }

    @Test
    public void testCreateAdminUnauthorized() throws Exception {
        logger.info("testCreateAdminUnauthorized called");
        String encodedPassword = passwordHasher.rsaEncrypt("admin123");
        String adminJson = objectMapper.writeValueAsString(
                new User("newadmin", encodedPassword, "admin")
        );

        mockMvc.perform(post("/admin/user/create")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(adminJson))
                .andExpect(status().isUnauthorized());
    }

    // Test Get All Users
    @Test
    public void testGetAllUsers() throws Exception {
        logger.info("testGetAllUsers called");
        mockMvc.perform(get("/admin/user/getall")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].username", hasItems("admin", "testuser")));
    }

    @Test
    public void testGetAllUsersUnauthorized() throws Exception {
        logger.info("testGetAllUsersUnauthorized called");
        mockMvc.perform(get("/admin/user/getall")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testSearchUsers() throws Exception {
        logger.info("testSearchUsers called");
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
        logger.info("testGetUserSuccess called");
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
        logger.info("testGetUserNotFound called");
        mockMvc.perform(get("/admin/user/get/nonexistent")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    @Test
    public void testGetUserUnauthorized() throws Exception {
        logger.info("testGetUserUnauthorized called");
        mockMvc.perform(get("/admin/user/get/testuser")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isUnauthorized());
    }

    // Test Edit User
    @Test
    public void testEditUserSuccess() throws Exception {
        logger.info("testEditUserSuccess called");
        // Prepare updated user fields
        String encodedPassword = passwordHasher.rsaEncrypt("user123");
        User original = userDatabase.getUser("testuser");
        User updatedUser = new User(original.getUsername(), encodedPassword, original.getSalt());
        updatedUser.setId(original.getId());
        updatedUser.setFirstName("UpdatedFirst");
        updatedUser.setLastName("UpdatedLast");
        updatedUser.setTitle("manager");
        updatedUser.setDepartment(5);
        updatedUser.setEmail("updatedemail@test.com");
        updatedUser.setCountry("UpdatedCountry");
        updatedUser.setCity("UpdatedCity");
        updatedUser.setLocation(7);

        String userJson = objectMapper.writeValueAsString(updatedUser);

        mockMvc.perform(put("/admin/user/edit")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User updated successfully"))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("updatedemail@test.com"))
                .andExpect(jsonPath("$.title").value("manager"))
                .andExpect(jsonPath("$.location").value(7));

        // Fetch from DB and assert all fields
        User dbUser = userDatabase.getUser("testuser");
        org.junit.jupiter.api.Assertions.assertEquals("UpdatedFirst", dbUser.getFirstName());
        org.junit.jupiter.api.Assertions.assertEquals("UpdatedLast", dbUser.getLastName());
        org.junit.jupiter.api.Assertions.assertEquals("manager", dbUser.getTitle());
        org.junit.jupiter.api.Assertions.assertEquals(5, dbUser.getDepartment());
        org.junit.jupiter.api.Assertions.assertEquals("updatedemail@test.com", dbUser.getEmail());
        org.junit.jupiter.api.Assertions.assertEquals("UpdatedCountry", dbUser.getCountry());
        org.junit.jupiter.api.Assertions.assertEquals("UpdatedCity", dbUser.getCity());
        org.junit.jupiter.api.Assertions.assertEquals(7, dbUser.getLocation());
    }

    @Test
    public void testEditUserChangePassword() throws Exception {
        logger.info("testEditUserChangePassword called");
        User testUser = new User("testuser", passwordHasher.rsaEncrypt("user123"), "");
        testUser.setEmail("user@test.com");
        testUser.setTitle("user");
        testUser.setLocation(2);
        String userJson = objectMapper.writeValueAsString(
                testUser
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
        logger.info("testEditUserNotFound called");
        String encodedPassword = passwordHasher.rsaEncrypt("user123");
        String userJson = objectMapper.writeValueAsString(
                new User("nonexistent", encodedPassword, "user")
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
        logger.info("testEditUserUnauthorized called");
        String encodedPassword = passwordHasher.rsaEncrypt("user123");
        String userJson = objectMapper.writeValueAsString(
                new User("testuser", encodedPassword, "user")
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
        logger.info("testDeleteUserSuccess called");
        mockMvc.perform(post("/admin/user/delete/testuser")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User deleted successfully"))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        logger.info("testDeleteUserNotFound called");
        mockMvc.perform(post("/admin/user/delete/nonexistent")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    @Test
    public void testDeleteUserUnauthorized() throws Exception {
        logger.info("testDeleteUserUnauthorized called");
        mockMvc.perform(post("/admin/user/delete/testuser")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testDeleteUserWithoutToken() throws Exception {
        logger.info("testDeleteUserWithoutToken called");
        mockMvc.perform(post("/admin/user/delete/testuser"))
                .andExpect(status().isBadRequest());
    }
}
