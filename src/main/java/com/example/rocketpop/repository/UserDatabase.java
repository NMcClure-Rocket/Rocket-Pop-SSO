package com.example.rocketpop.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.example.rocketpop.model.User;

import org.springframework.stereotype.Repository;

@Repository
public class UserDatabase implements Database {
    public static final Logger logger = LoggerFactory.getLogger(UserDatabase.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String getUserQuery = "SELECT * FROM users WHERE username = ?";
    private static final String getAllUsersQuery = "SELECT * FROM users";
    private static final String createUserQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
    private static final String updateUserQuery = "UPDATE users SET username = ?, password = ? WHERE id = ?";
    private static final String deleteUserQuery = "DELETE FROM users WHERE id = ?";
    private static final String deleteAllUsersQuery = "DELETE FROM users";

    @Override
    public User getUser(String username) {
        logger.info("getUser called with username: {}", username);
        Object[] args = {username};
        List<User> users = jdbcTemplate.query(getUserQuery, args, new UserMapper());

        if (users.size() == 0) {
            logger.info("No user found with username: {}", username);
            return null;
        }
        logger.info("Found user: {}", users.get(0));
        return users.get(0);
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("getAllUsers called");
        List<User> users = jdbcTemplate.query(getAllUsersQuery, new UserMapper());
        if (users.size() == 0) {
            logger.info("No users found");
            return new ArrayList<User>();
        }
        return users;
    }

    @Override
    public boolean createUser(User user) {
        logger.info("createUser called with username: {}", user.getUsername());
        Object[] args = {user.getUsername(), user.getPassword()};
        int count = jdbcTemplate.update(createUserQuery, args);

        if (count == 0) {
            logger.info("User not created");
            return false;
        }
        logger.info("User created");
        return true;
    }

    @Override
    public boolean updateUser(User user) {
        logger.info("updateUser called with user: {}", user.getUsername());
        Object[] args = {user.getUsername(), user.getPassword(), user.getId()};
        int count = jdbcTemplate.update(updateUserQuery, args);

        if (count == 0) {
            logger.info("User not updated");
            return false;
        }
        logger.info("User updated");
        return true;
    }

    @Override
    public boolean deleteUser(int id) {
        logger.info("deleteUser called with id: {}", id);
        Object[] args = {id};
        int count = jdbcTemplate.update(deleteUserQuery, args);

        if (count == 0) {
            logger.info("User not deleted");
            return false;
        }
        logger.info("User deleted");
        return true;
    }
    
    public int deleteAllUsers() {
        logger.info("deleteAllUsers called");
        int count = jdbcTemplate.update(deleteAllUsersQuery);
        return count;
    }

    public static final class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password")
            );
        }
    }
}
