package com.example.rocketpop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.rocketpop.model.User;

@SpringBootTest
@ActiveProfiles("test")
public class UserDatabaseTests {

    public static final Logger logger = LoggerFactory.getLogger(UserDatabase.class);

    @Autowired
    private UserDatabase database;

    @BeforeEach
    public void setUp() {
        var user = new User("user1", "test", "salt");
        database.createUser(user);

        var user2 = new User("user2", "test", "salt");
        database.createUser(user2);
    }

    @AfterEach
    public void tearDown() {
        database.deleteAllUsers();
    }

    @Test
    public void testGetUser() {
        var user = database.getUser("user1");
        assertNotNull(user);
        assertEquals("user1", user.getUsername());
    }

    @Test
    public void testGetUserNotFound() {
        var user = database.getUser("user3");
        assertNull(user);
    }

    @Test
    public void testGetAllUsers() {
        var users = database.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    public void testCreateUser() {
        var user = new User("user3", "test", "salt");
        assertTrue(database.createUser(user));

        var users = database.getAllUsers();
        assertEquals(3, users.size());

        var user3 = database.getUser("user3");
        assertNotNull(user3);
        assertEquals("user3", user3.getUsername());
    }

    @Test
    public void testCreateUserAlreadyExists() {
        var user = new User("user1", "test", "salt");
        assertFalse(database.createUser(user));
    }

    @Test
    public void testUpdateUser() {
        logger.info("testUpdateUser");
        var user = database.getUser("user1");
        assertNotNull(user, "User is null");
        user.setUsername("user4");
        assertTrue(database.updateUser(user), "User not updated");

        var user4 = database.getUser("user4");
        assertNotNull(user4, "User is null");
        assertEquals("user4", user4.getUsername());
    }

    @Test 
    public void testUpdateUserNotFound() {
        var user = new User("user3", "test", "salt");
        assertNotNull(user);
        assertFalse(database.updateUser(user), "User not updated");
    }

    @Test
    public void testDeleteUser() {
        var user = database.getUser("user2");
        assertNotNull(user);
        assertTrue(database.deleteUser(user.getId()));

        var users = database.getAllUsers();
        assertEquals(1, users.size());

        var user2 = database.getUser("user2");
        assertNull(user2);
    }

    @Test
    public void testDeleteUserNotFound() {
        assertFalse(database.deleteUser(1));
    }

    @Test
    public void testDeleteAllUsers() {
        assertEquals(2, database.deleteAllUsers());
        var users = database.getAllUsers();
        assertEquals(0, users.size());
    }

    @Test
    public void testGetUserSalt() {
        // Existing user
        String salt = database.getUserSalt("user1");
        assertEquals("salt", salt);

        // Non-existent user
        String missingSalt = database.getUserSalt("nonexistent");
        assertNull(missingSalt);
    }

}
