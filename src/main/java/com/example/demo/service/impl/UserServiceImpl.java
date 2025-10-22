package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.UserDAO;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User registerUser(User user) {
        // 檢查使用者名稱是否已存在
        if (isUsernameExists(user.getUsername())) {
            throw new IllegalArgumentException("使用者名稱已存在: " + user.getUsername());
        }

        // 檢查電子郵件是否已存在
        if (isEmailExists(user.getEmail())) {
            throw new IllegalArgumentException("電子郵件已存在: " + user.getEmail());
        }

        // 加密密碼
        String encodedPassword = encodePassword(user.getPassword());
        user.setPassword(encodedPassword);

        // 儲存使用者
        return userDAO.save(user);
    }

    @Override
    public User getByUsername(String username) {
        return userDAO.getByUsername(username);
    }

    @Override
    public User getByEmail(String email) {
        return userDAO.getByEmail(email);
    }

    @Override
    public User getById(Long id) {
        return userDAO.getById(id);
    }

    @Override
    public List<User> getAll() {
        return userDAO.getAll();
    }

    @Override
    public User updateUser(Long id, User user) {
        // 根據 ID 取得現有使用者
        User existingUser = userDAO.getById(id);
        if (existingUser != null) {
            // 更新使用者資料
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(user.getPassword());
            }
            return userDAO.update(existingUser);
        }
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        userDAO.deleteById(id);
    }

    @Override
    public void saveUser(User user) {
        // 如果密碼不是加密過的，先加密
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userDAO.save(user);
    }

    @Override
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        // 使用 BCrypt 驗證密碼
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public String encodePassword(String rawPassword) {
        // 使用 BCrypt 加密密碼
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean isUsernameExists(String username) {
        return userDAO.existsByUsername(username);
    }

    @Override
    public boolean isEmailExists(String email) {
        return userDAO.existsByEmail(email);
    }
}