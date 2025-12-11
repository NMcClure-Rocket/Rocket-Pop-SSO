package com.example.rocketpop.service;

import com.example.rocketpop.model.User;
import com.example.rocketpop.repository.UserDatabase;
import com.example.rocketpop.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTests {

    @Mock
    private UserDatabase userDatabase;

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private User adminUser;
    private User managerUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setEmail("test@example.com");
        testUser.setTitle("user");
        testUser.setSalt("somesalt");
        
        adminUser = new User();
        adminUser.setId(2);
        adminUser.setUsername("adminuser");
        adminUser.setPassword("adminpass");
        adminUser.setEmail("admin@example.com");
        adminUser.setTitle("admin");
        
        managerUser = new User();
        managerUser.setId(3);
        managerUser.setUsername("managername");
        managerUser.setPassword("managerpass");
        managerUser.setEmail("manager@example.com");
        managerUser.setTitle("manager");
    }

    // ========== authenticateUser Tests ==========

    @Test
    public void testAuthenticateUser_ValidUser() {
        when(userDatabase.getUser("testuser")).thenReturn(testUser);
        when(jwtUtil.generateUserToken(any(User.class))).thenReturn("user.token.here");
        
        String token = userService.authenticateUser("testuser", "password123");
        
        assertNotNull(token);
        assertEquals("user.token.here", token);
        verify(userDatabase).getUser("testuser");
        verify(jwtUtil).generateUserToken(any(User.class));
    }

    @Test
    public void testAuthenticateUser_AdminUser() {
        when(userDatabase.getUser("adminuser")).thenReturn(adminUser);
        when(jwtUtil.generateAdminToken(any(User.class))).thenReturn("admin.token.here");
        
        String token = userService.authenticateUser("adminuser", "adminpass");
        
        assertNotNull(token);
        assertEquals("admin.token.here", token);
        verify(userDatabase).getUser("adminuser");
        verify(jwtUtil).generateAdminToken(any(User.class));
    }

    @Test
    public void testAuthenticateUser_ManagerUser() {
        when(userDatabase.getUser("managername")).thenReturn(managerUser);
        when(jwtUtil.generateUserToken(any(User.class))).thenReturn("manager.token.here");
        
        String token = userService.authenticateUser("managername", "managerpass");
        
        assertNotNull(token);
        verify(jwtUtil).generateUserToken(any(User.class));
    }

    @Test
    public void testAuthenticateUser_UserNotFound() {
        when(userDatabase.getUser("nonexistent")).thenReturn(null);
        
        assertThrows(RuntimeException.class, () -> {
            userService.authenticateUser("nonexistent", "anypassword");
        });
    }

    @Test
    public void testAuthenticateUser_WrongPassword() {
        when(userDatabase.getUser("testuser")).thenReturn(testUser);
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.authenticateUser("testuser", "wrongpassword");
        });
        
        assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    public void testAuthenticateUser_NullEmail() {
        User userWithoutEmail = new User();
        userWithoutEmail.setUsername("nomail");
        userWithoutEmail.setPassword("pass");
        userWithoutEmail.setEmail(null);
        userWithoutEmail.setTitle("user");
        
        when(userDatabase.getUser("nomail")).thenReturn(userWithoutEmail);
        when(jwtUtil.generateUserToken(any(User.class))).thenReturn("token");
        
        userService.authenticateUser("nomail", "pass");
        
        verify(jwtUtil).generateUserToken(any(User.class));
    }

    @Test
    public void testAuthenticateUser_NullTitle() {
        User userWithoutTitle = new User();
        userWithoutTitle.setUsername("notitle");
        userWithoutTitle.setPassword("pass");
        userWithoutTitle.setEmail("notitle@example.com");
        userWithoutTitle.setTitle(null);
        
        when(userDatabase.getUser("notitle")).thenReturn(userWithoutTitle);
        when(jwtUtil.generateUserToken(any(User.class))).thenReturn("token");
        
        userService.authenticateUser("notitle", "pass");
        
        verify(jwtUtil).generateUserToken(any(User.class));
    }

    // ========== getUserByUsername Tests ==========

    @Test
    public void testGetUserByUsername_Success() {
        when(userDatabase.getUser("testuser")).thenReturn(testUser);
        
        User result = userService.getUserByUsername("testuser");
        
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userDatabase).getUser("testuser");
    }

    @Test
    public void testGetUserByUsername_NotFound() {
        when(userDatabase.getUser("nonexistent")).thenReturn(null);
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserByUsername("nonexistent");
        });
        
        assertEquals("User not found", exception.getMessage());
    }

    // ========== getUserFromToken Tests ==========

    @Test
    public void testGetUserFromToken_UserToken() {
        when(jwtUtil.isAdminToken("user.token")).thenReturn(false);
        when(jwtUtil.extractId("user.token", false)).thenReturn("1");
        when(userDatabase.getUserById("1")).thenReturn(testUser);
        
        User result = userService.getUserFromToken("user.token");
        
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    public void testGetUserFromToken_AdminToken() {
        when(jwtUtil.isAdminToken("admin.token")).thenReturn(true);
        when(jwtUtil.extractId("admin.token", true)).thenReturn("2");
        when(userDatabase.getUserById("2")).thenReturn(adminUser);
        
        User result = userService.getUserFromToken("admin.token");
        
        assertNotNull(result);
        assertEquals("adminuser", result.getUsername());
    }

    // ========== updatePassword Tests ==========

    @Test
    public void testUpdatePassword_Success() {
        when(userDatabase.getUser("testuser")).thenReturn(testUser);
        when(userDatabase.updateUser(any(User.class))).thenReturn(true);
        
        userService.updatePassword("testuser", "password123", "newpassword");
        
        verify(userDatabase).updateUser(argThat(user -> 
            user.getPassword().equals("newpassword") && user.getSalt().equals("")
        ));
    }

    @Test
    public void testUpdatePassword_WrongOldPassword() {
        when(userDatabase.getUser("testuser")).thenReturn(testUser);
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updatePassword("testuser", "wrongold", "newpassword");
        });
        
        assertEquals("Current password is incorrect", exception.getMessage());
    }

    @Test
    public void testUpdatePassword_UserNotFound() {
        when(userDatabase.getUser("nonexistent")).thenReturn(null);
        
        assertThrows(RuntimeException.class, () -> {
            userService.updatePassword("nonexistent", "old", "new");
        });
    }

    // ========== createUser Tests ==========

    @Test
    public void testCreateUser_Success() {
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("newpass");
        
        when(userDatabase.getUser("newuser")).thenReturn(null).thenReturn(newUser);
        when(userDatabase.createUser(newUser)).thenReturn(true);
        
        User result = userService.createUser(newUser);
        
        assertNotNull(result);
        assertEquals("newuser", result.getUsername());
        verify(userDatabase).createUser(newUser);
    }

    @Test
    public void testCreateUser_UsernameExists() {
        User newUser = new User();
        newUser.setUsername("testuser");
        
        when(userDatabase.getUser("testuser")).thenReturn(testUser);
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(newUser);
        });
        
        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    public void testCreateUser_CreationFails() {
        User newUser = new User();
        newUser.setUsername("newuser");
        
        when(userDatabase.getUser("newuser")).thenReturn(null);
        when(userDatabase.createUser(newUser)).thenReturn(false);
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(newUser);
        });
        
        assertEquals("Failed to create user", exception.getMessage());
    }

    // ========== updateUser Tests ==========

    @Test
    public void testUpdateUser_Success() {
        when(userDatabase.userExists("testuser")).thenReturn(true);
        when(userDatabase.updateUser(testUser)).thenReturn(true);
        when(userDatabase.getUser("testuser")).thenReturn(testUser);
        
        User result = userService.updateUser(testUser);
        
        assertNotNull(result);
        verify(userDatabase).updateUser(testUser);
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        when(userDatabase.userExists("nonexistent")).thenReturn(false);
        
        User user = new User();
        user.setUsername("nonexistent");
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(user);
        });
        
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testUpdateUser_UpdateFails() {
        when(userDatabase.userExists("testuser")).thenReturn(true);
        when(userDatabase.updateUser(testUser)).thenReturn(false);
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(testUser);
        });
        
        assertEquals("Failed to update user", exception.getMessage());
    }

    // ========== deleteUser Tests ==========

    @Test
    public void testDeleteUser_Success() {
        when(userDatabase.getUser("testuser")).thenReturn(testUser);
        when(userDatabase.deleteUser(1)).thenReturn(true);
        
        userService.deleteUser("testuser");
        
        verify(userDatabase).deleteUser(1);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        when(userDatabase.getUser("nonexistent")).thenReturn(null);
        
        assertThrows(RuntimeException.class, () -> {
            userService.deleteUser("nonexistent");
        });
    }

    // ========== getAllUsers Tests ==========

    @Test
    public void testGetAllUsers() {
        List<User> users = Arrays.asList(testUser, adminUser, managerUser);
        when(userDatabase.getAllUsers()).thenReturn(users);
        
        List<User> result = userService.getAllUsers();
        
        assertEquals(3, result.size());
        verify(userDatabase).getAllUsers();
    }

    @Test
    public void testGetAllUsers_EmptyList() {
        when(userDatabase.getAllUsers()).thenReturn(Arrays.asList());
        
        List<User> result = userService.getAllUsers();
        
        assertEquals(0, result.size());
    }

    // ========== getUsernames Tests ==========

    @Test
    public void testGetUsernames() {
        List<String> usernames = Arrays.asList("user1", "user2", "user3");
        when(userDatabase.getUsernames()).thenReturn(usernames);
        
        List<String> result = userService.getUsernames();
        
        assertEquals(3, result.size());
        assertTrue(result.contains("user1"));
        verify(userDatabase).getUsernames();
    }

    // ========== searchUsers Tests ==========

    @Test
    public void testSearchUsers_WithQuery() {
        List<User> matchingUsers = Arrays.asList(testUser);
        when(userDatabase.searchUsers("test")).thenReturn(matchingUsers);
        
        List<User> result = userService.searchUsers("test");
        
        assertEquals(1, result.size());
        verify(userDatabase).searchUsers("test");
    }

    @Test
    public void testSearchUsers_NullQuery() {
        List<User> allUsers = Arrays.asList(testUser, adminUser);
        when(userDatabase.getAllUsers()).thenReturn(allUsers);
        
        List<User> result = userService.searchUsers(null);
        
        assertEquals(2, result.size());
        verify(userDatabase).getAllUsers();
    }

    @Test
    public void testSearchUsers_EmptyQuery() {
        List<User> allUsers = Arrays.asList(testUser, adminUser);
        when(userDatabase.getAllUsers()).thenReturn(allUsers);
        
        List<User> result = userService.searchUsers("");
        
        assertEquals(2, result.size());
        verify(userDatabase).getAllUsers();
    }

    // ========== validateAdminToken Tests ==========

    @Test
    public void testValidateAdminToken_Valid() {
        when(jwtUtil.cleanToken("Bearer admin.token")).thenReturn("admin.token");
        when(jwtUtil.isAdminToken("admin.token")).thenReturn(true);
        when(jwtUtil.validateToken("admin.token", true)).thenReturn(true);
        
        boolean result = userService.validateAdminToken("Bearer admin.token");
        
        assertTrue(result);
    }

    @Test
    public void testValidateAdminToken_NotAdminToken() {
        when(jwtUtil.cleanToken("user.token")).thenReturn("user.token");
        when(jwtUtil.isAdminToken("user.token")).thenReturn(false);
        
        boolean result = userService.validateAdminToken("user.token");
        
        assertFalse(result);
    }

    @Test
    public void testValidateAdminToken_InvalidToken() {
        when(jwtUtil.cleanToken("admin.token")).thenReturn("admin.token");
        when(jwtUtil.isAdminToken("admin.token")).thenReturn(true);
        when(jwtUtil.validateToken("admin.token", true)).thenReturn(false);
        
        boolean result = userService.validateAdminToken("admin.token");
        
        assertFalse(result);
    }

    // ========== validateUserToken Tests ==========

    @Test
    public void testValidateUserToken_ValidUserToken() {
        when(jwtUtil.cleanToken("Bearer user.token")).thenReturn("user.token");
        when(jwtUtil.isAdminToken("user.token")).thenReturn(false);
        when(jwtUtil.validateToken("user.token", false)).thenReturn(true);
        
        boolean result = userService.validateUserToken("Bearer user.token");
        
        assertTrue(result);
    }

    @Test
    public void testValidateUserToken_ValidAdminToken() {
        when(jwtUtil.cleanToken("admin.token")).thenReturn("admin.token");
        when(jwtUtil.isAdminToken("admin.token")).thenReturn(true);
        when(jwtUtil.validateToken("admin.token", true)).thenReturn(true);
        
        boolean result = userService.validateUserToken("admin.token");
        
        assertTrue(result);
    }

    @Test
    public void testValidateUserToken_Invalid() {
        when(jwtUtil.cleanToken("invalid.token")).thenReturn("invalid.token");
        when(jwtUtil.isAdminToken("invalid.token")).thenReturn(false);
        when(jwtUtil.validateToken("invalid.token", false)).thenReturn(false);
        
        boolean result = userService.validateUserToken("invalid.token");
        
        assertFalse(result);
    }

    // ========== getUserSalt Tests ==========

    @Test
    public void testGetUserSalt() {
        when(userDatabase.getUserSalt("testuser")).thenReturn("somesalt");
        
        String salt = userService.getUserSalt("testuser");
        
        assertEquals("somesalt", salt);
        verify(userDatabase).getUserSalt("testuser");
    }

    @Test
    public void testGetUserSalt_EmptySalt() {
        when(userDatabase.getUserSalt("testuser")).thenReturn("");
        
        String salt = userService.getUserSalt("testuser");
        
        assertEquals("", salt);
    }

    @Test
    public void testGetUserSalt_NullSalt() {
        when(userDatabase.getUserSalt("testuser")).thenReturn(null);
        
        String salt = userService.getUserSalt("testuser");
        
        assertNull(salt);
    }
}
