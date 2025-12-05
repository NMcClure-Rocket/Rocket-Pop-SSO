package com.example.rocketpop.repository;

import com.example.rocketpop.model.User;
import java.util.List;

public interface Database {

    public User getUser(String username);

    public List<User> getAllUsers();

    public boolean createUser(User user);

    public boolean updateUser(User user);

    public boolean deleteUser(int id);
}
