package com.example.rocketpop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.rocketpop.model.User;

@SpringBootTest
@ActiveProfiles("test")
public class UserDatabaseTests {

    @Autowired
    private UserDatabase database;

    @BeforeEach
    public void setUp() {
        database.deleteAllUsers();

        var user = new User("user1", "test");
        database.createUser(user);

        var user2 = new User("user2", "test");
        database.createUser(user2);
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
        var user = new User("user3", "test");
        assertTrue(database.createUser(user));

        var users = database.getAllUsers();
        assertEquals(3, users.size());

        var user3 = database.getUser("user3");
        assertNotNull(user3);
        assertEquals("user3", user3.getUsername());
    }

    @Test
    public void testCreateUserAlreadyExists() {
        var user = new User("user1", "test");
        assertFalse(database.createUser(user));
    }

    @Test @Disabled
    public void testUpdateUser() {
        fail("Not yet implemented");
        var user = database.getUser("user1");
        assertNotNull(user);
        //user.setUsername("user4");
        assertTrue(database.updateUser(user));

        var user4 = database.getUser("user4");
        assertNotNull(user4);
        assertEquals("user4", user4.getUsername());
    }

    @Test
    public void testUpdateUserNotFound() {
        var user = new User("user3", "test");
        assertNotNull(user);
        assertFalse(database.updateUser(user));
    }

    @Test @Disabled
    public void testDeleteUser() {
        var user = database.getUser("user2");
        assertNotNull(user);
        fail("Not yet implemented");
        //assertTrue(database.deleteUser(user.getId()));

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
}
