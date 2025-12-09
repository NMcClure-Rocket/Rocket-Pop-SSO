package com.example.rocketpop.repository;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.example.rocketpop.model.User;

import org.springframework.stereotype.Repository;

@Repository
public class UserDatabase implements Database {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDatabase.class);

    // @Value("${private.key}")
    // private String privateKeyString;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String GETUSERQUERY = "SELECT * FROM users WHERE username = ?";
    private static final String GETALLUSERQUERY = "SELECT * FROM users";
    private static final String SEARCHUSERSQUERY = "SELECT * FROM users WHERE username LIKE ?";
    private static final String CREATEUSERQUERY = "INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GETUSERIDBYUSERNAMEQUERY = "SELECT id FROM users WHERE username = ?";
    private static final String UPDATEUSERQUERY = "UPDATE users SET username = ?, password = ?, salt = ?, first_name = ?, last_name = ?, title = ?, department = ?, email = ?, country = ?, city = ?, location = ? WHERE id = ?";
    private static final String DELETEUSERQUERY = "DELETE FROM users WHERE id = ?";
    private static final String DELETEUSERBYUSERNAMEQUERY = "DELETE FROM users WHERE username = ?";
    private static final String DELETEALLUSERSQUERY = "DELETE FROM users";

    @Override
    public User getUser(String username) {
        LOGGER.info("getUser called with username: {}", username);
        Object[] args = {username};
        List<User> users = jdbcTemplate.query(GETUSERQUERY, args, new UserMapper());

        if (users.size() == 0) {
            LOGGER.info("No user found with username: {}", username);
            for (User user : getAllUsers()) {
                LOGGER.info("User: {}", user.getUsername());
            }
            return null;
        }
        LOGGER.info("Found user: {}", users.get(0));
        return users.get(0);
    }

    @Override
    public List<User> getAllUsers() {
        LOGGER.info("getAllUsers called");
        List<User> users = jdbcTemplate.query(GETALLUSERQUERY, new UserMapper());
        if (users.size() == 0) {
            LOGGER.info("No users found");
            return new ArrayList<User>();
        }
        return users;
    }

    @Override
    public boolean createUser(User user) {
        LOGGER.info("createUser called with username: {}", user.getUsername());
        if (userExists(user.getUsername())) {
            LOGGER.info("User already exists");
            return false;
        }
        Object[] args = {
            user.getFirstName(),
            user.getLastName(),
            user.getTitle(),
            user.getDepartment(),
            user.getEmail(),
            user.getCountry(),
            user.getCity(),
            user.getLocation(),
            user.getUsername(),
            user.getPassword(),
            user.getSalt()
        };
        int count = -1;

            count = jdbcTemplate.update(CREATEUSERQUERY, args);

        if (count < 1) {
            LOGGER.info("User not created");
            return false;
        }
        LOGGER.info("User created");
        return true;
    }

    @Override
    public boolean updateUser(User user) {
        LOGGER.info("updateUser called with user: {}", user.getUsername());

        Object []args = new Object[] {
            user.getUsername(),
                user.getPassword(),
                user.getSalt(),
                user.getFirstName(),
                user.getLastName(),
                user.getTitle(),
                user.getDepartment(),
                user.getEmail(),
                user.getCountry(),
                user.getCity(),
                user.getLocation(),
                user.getId()
        };

        int count = -1;
        try {
            count = jdbcTemplate.update(UPDATEUSERQUERY, args);
        } catch (DataAccessException e) {
            LOGGER.error("Error updating user: {}", e.getMessage());
            return false;
        }

        if (count == 0) {
            LOGGER.info("User not updated");
            return false;
        }
        LOGGER.info("User updated");
        return true;
    }

    @Override
    public boolean deleteUser(int id) {
        LOGGER.info("deleteUser called with id: {}", id);
        Object[] args = {id};
        int count = jdbcTemplate.update(DELETEUSERQUERY, args);

        if (count == 0) {
            LOGGER.info("User not deleted");
            return false;
        }
        LOGGER.info("User deleted");
        return true;
    }
    
    public int deleteAllUsers() {
        LOGGER.info("deleteAllUsers called");
        int count = jdbcTemplate.update(DELETEALLUSERSQUERY);
        return count;
    }

    public boolean deleteUserByUsername(String username) {
        LOGGER.info("deleteUserByUsername called with username: {}", username);
        Object[] args = {username};
        int count = jdbcTemplate.update(DELETEUSERBYUSERNAMEQUERY, args);
        
        if (count == 0) {
            LOGGER.info("User not deleted");
            return false;
        }
        LOGGER.info("User deleted");
        return true;
    }

    public List<User> searchUsers(String username) {
        LOGGER.info("searchUsers called with username: {}", username);
        Object[] args = {"%" + username + "%"};
        List<User> users = jdbcTemplate.query(SEARCHUSERSQUERY, new UserMapper(), args);
        if (users.size() == 0) {
            LOGGER.info("No users found");
            return new ArrayList<User>();
        }
        return users;
    }

    public static final class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            var user = new User(rs.getString("username"), rs.getString("password"), rs.getString("salt"));
            user.setId(rs.getInt("id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setTitle(rs.getString("title"));
            user.setDepartment(rs.getInt("department"));
            user.setEmail(rs.getString("email"));
            user.setCountry(rs.getString("country"));
            user.setCity(rs.getString("city"));
            user.setLocation(rs.getInt("location"));
            return user;
        }
    }

    private boolean userExists(String username) {
        LOGGER.info("userExists called with username: {}", username);
        Object[] args = {username};
        List<User> count = jdbcTemplate.query(GETUSERQUERY, args, new UserMapper());
        return count.size() > 0;
    }

    /**
     * Decrypt username and password sent by user, then generate and return a salted hash of the password.
     */
    public void hashPassword(String ciphertext) {
        Base64.Decoder decoder = Base64.getDecoder();
        String plaintext = null;
        PrivateKey privateKey = null;

        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            byte[] privateKeyByte = decoder.decode(privateKeyString);
            privateKey = factory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyByte));
        } catch (Exception e) {
            System.out.println(e);
        }

        // Decrypt ciphertext
        try {
            byte[] cipherBytes = decoder.decode(ciphertext);
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptBytes = decryptCipher.doFinal(cipherBytes);
            plaintext = new String(decryptBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println(e);
        }

        // Isolate password
        String[] splitPlaintext = plaintext.split(",");
        String username = splitPlaintext[0];
        String rawPassword = splitPlaintext[1];

        // Hash password
        SecureRandom rng = new SecureRandom();
        byte[] salt = new byte[16];
        byte[] hashedPassword = null;
        try{
            rng.nextBytes(salt);
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            hashedPassword = md.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.out.println(e);
        }

        // TODO: Call DB
        
    }
}
