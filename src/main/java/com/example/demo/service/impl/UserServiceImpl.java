package com.example.demo.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.UserDAO;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

/**
 * 用戶服務層實作類別
 * 負責處理用戶相關的業務邏輯，包含註冊、登入驗證、密碼加密等功能
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDAO userDAO;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 註冊新使用者
     * 驗證使用者名稱和電子郵件是否已存在，並對密碼進行加密
     * 
     * @param user 要註冊的使用者
     * @return 註冊成功的使用者
     * @throws IllegalArgumentException 如果使用者名稱或電子郵件已存在
     */
    @Override
    public User registerUser(User user) {
        logger.info("開始註冊新使用者 - username: {}, email: {}", user.getUsername(), user.getEmail());
        
        // 檢查使用者名稱是否已存在
        if (isUsernameExists(user.getUsername())) {
            logger.warn("註冊失敗 - 使用者名稱已存在: {}", user.getUsername());
            throw new IllegalArgumentException("使用者名稱已存在: " + user.getUsername());
        }

        // 檢查電子郵件是否已存在
        if (isEmailExists(user.getEmail())) {
            logger.warn("註冊失敗 - 電子郵件已存在: {}", user.getEmail());
            throw new IllegalArgumentException("電子郵件已存在: " + user.getEmail());
        }

        // 加密密碼
        logger.debug("開始加密使用者密碼");
        String encodedPassword = encodePassword(user.getPassword());
        user.setPassword(encodedPassword);
        logger.debug("密碼加密完成");

        // 儲存使用者
        User savedUser = userDAO.save(user);
        logger.info("使用者註冊成功 - userId: {}, username: {}", savedUser.getId(), savedUser.getUsername());
        return savedUser;
    }

    /**
     * 根據使用者名稱查找使用者
     * 
     * @param username 使用者名稱
     * @return 使用者物件，如果不存在則返回null
     */
    @Override
    public User getByUsername(String username) {
        logger.debug("查找使用者 - username: {}", username);
        User user = userDAO.getByUsername(username);
        logger.debug("使用者查找結果 - username: {}, found: {}", username, user != null);
        return user;
    }

    /**
     * 根據電子郵件查找使用者
     * 
     * @param email 電子郵件
     * @return 使用者物件，如果不存在則返回null
     */
    @Override
    public User getByEmail(String email) {
        logger.debug("查找使用者 - email: {}", email);
        User user = userDAO.getByEmail(email);
        logger.debug("使用者查找結果 - email: {}, found: {}", email, user != null);
        return user;
    }

    /**
     * 根據ID查找使用者
     * 
     * @param id 使用者ID
     * @return 使用者物件，如果不存在則返回null
     */
    @Override
    public User getById(Long id) {
        logger.debug("查找使用者 - userId: {}", id);
        User user = userDAO.getById(id);
        logger.debug("使用者查找結果 - userId: {}, found: {}", id, user != null);
        return user;
    }

    /**
     * 查找所有使用者
     * 
     * @return 所有使用者的列表
     */
    @Override
    public List<User> getAll() {
        logger.debug("查找所有使用者");
        List<User> users = userDAO.getAll();
        logger.info("查找到 {} 個使用者", users.size());
        return users;
    }

    /**
     * 更新使用者資訊
     * 
     * @param id 要更新的使用者ID
     * @param user 包含新使用者資料的物件
     * @return 更新後的使用者
     */
    @Override
    public User updateUser(Long id, User user) {
        logger.info("開始更新使用者 - userId: {}", id);
        
        // 根據 ID 取得現有使用者
        User existingUser = userDAO.getById(id);
        if (existingUser != null) {
            logger.debug("找到現有使用者 - username: {}", existingUser.getUsername());
            
            // 更新使用者資料
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                logger.debug("更新密碼");
                existingUser.setPassword(user.getPassword());
            }
            // 更新角色
            if (user.getRole() != null) {
                logger.debug("更新角色 - role: {}", user.getRole());
                existingUser.setRole(user.getRole());
            }
            // 更新啟用狀態
            if (user.getEnabled() != null) {
                logger.debug("更新啟用狀態 - enabled: {}", user.getEnabled());
                existingUser.setEnabled(user.getEnabled());
            }
            
            User updatedUser = userDAO.update(existingUser);
            logger.info("使用者更新成功 - userId: {}", id);
            return updatedUser;
        }
        
        logger.warn("使用者不存在 - userId: {}", id);
        return null;
    }

    /**
     * 刪除使用者
     * 
     * @param id 要刪除的使用者ID
     */
    @Override
    public void deleteUser(Long id) {
        logger.info("開始刪除使用者 - userId: {}", id);
        userDAO.deleteById(id);
        logger.info("使用者刪除成功 - userId: {}", id);
    }

    /**
     * 儲存使用者（不含註冊驗證邏輯）
     * 如果密碼未加密則先進行加密
     * 
     * @param user 使用者物件
     */
    @Override
    public void saveUser(User user) {
        logger.info("開始儲存使用者 - username: {}", user.getUsername());
        
        // 如果密碼不是加密過的，先加密
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            logger.debug("對密碼進行加密");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        userDAO.save(user);
        logger.info("使用者儲存成功 - userId: {}", user.getId());
    }

    /**
     * 驗證使用者密碼
     * 
     * @param rawPassword 原始密碼
     * @param encodedPassword 加密後的密碼
     * @return 如果密碼正確返回true，否則返回false
     */
    @Override
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        logger.debug("開始驗證密碼");
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        logger.debug("密碼驗證結果: {}", matches);
        return matches;
    }

    /**
     * 加密密碼
     * 
     * @param rawPassword 原始密碼
     * @return 加密後的密碼
     */
    @Override
    public String encodePassword(String rawPassword) {
        logger.debug("開始加密密碼");
        String encodedPassword = passwordEncoder.encode(rawPassword);
        logger.debug("密碼加密完成");
        return encodedPassword;
    }

    /**
     * 檢查使用者名稱是否已存在
     * 
     * @param username 使用者名稱
     * @return 如果存在返回true，否則返回false
     */
    @Override
    public boolean isUsernameExists(String username) {
        logger.debug("檢查使用者名稱是否存在 - username: {}", username);
        boolean exists = userDAO.existsByUsername(username);
        logger.debug("使用者名稱檢查結果 - username: {}, exists: {}", username, exists);
        return exists;
    }

    /**
     * 檢查電子郵件是否已存在
     * 
     * @param email 電子郵件
     * @return 如果存在返回true，否則返回false
     */
    @Override
    public boolean isEmailExists(String email) {
        logger.debug("檢查電子郵件是否存在 - email: {}", email);
        boolean exists = userDAO.existsByEmail(email);
        logger.debug("電子郵件檢查結果 - email: {}, exists: {}", email, exists);
        return exists;
    }
}
