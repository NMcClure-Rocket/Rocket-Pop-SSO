package com.example.rocketpop.repository;

import java.util.List;

import com.example.rocketpop.model.User;

/**
 * Database interface. This interface defines functionality to interact with a 
 * database for users. 
 */
public interface Database {

    /**
     * Get user by username
     * @param username Username of user to retrieve
     * @return User object
     */
    User getUser(String username);

    /**
     * Get all users
     * @return List of User objects
     */
    List<User> getAllUsers();

    /**
     * Creates a new user
     * @param user User object
     * @return True if user was created successfully, false otherwise
     */
    boolean createUser(User user);

    /**
     * Updates an existing user. Will return fault if user does not exist.
     * @param user User object
     * @return True if user was updated successfully, false otherwise
     */
    boolean updateUser(User user);

    /**
     * Deletes a user by id
     * @param id Id of user to delete
     * @return True if user was deleted successfully, false otherwise
     */
    boolean deleteUser(int id);
}
