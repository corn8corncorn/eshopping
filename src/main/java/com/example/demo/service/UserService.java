package com.example.demo.service;

import java.util.List;

import com.example.demo.model.User;

public interface UserService {
    
    List<User> getAllUsers();
    
    User getUserById(Long id);
    
    User getUserByEmail(String email);
    
    void saveUser(User user);
    
    void updateUser(Long id, User updatedUser);
    
    void deleteUser(Long id);
}
