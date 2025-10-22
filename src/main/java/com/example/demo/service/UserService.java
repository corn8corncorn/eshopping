package com.example.demo.service;

import java.util.List;

import com.example.demo.model.User;

public interface UserService {

    /**
     * 註冊新使用者
     * @param user 要註冊的使用者
     * @return 註冊成功的使用者
     * @throws IllegalArgumentException 如果使用者名稱或電子郵件已存在
     */
    User registerUser(User user);

    /**
     * 根據使用者名稱查找使用者
     * @param username 使用者名稱
     * @return 使用者物件，如果不存在則返回null
     */
    User getByUsername(String username);

    /**
     * 根據電子郵件查找使用者
     * @param email 電子郵件
     * @return 使用者物件，如果不存在則返回null
     */
    User getByEmail(String email);

    /**
     * 根據ID查找使用者
     * @param id 使用者ID
     * @return 使用者物件，如果不存在則返回null
     */
    User getById(Long id);

    /**
     * 查找所有使用者
     * @return 所有使用者的列表
     */
    List<User> getAll();

    /**
     * 更新使用者資訊
     * @param user 要更新的使用者
     * @return 更新後的使用者
     */
    User updateUser(Long id,User user);

    /**
     * 儲存使用者（不含註冊驗證邏輯）
     * @param user 使用者
     */
    void saveUser(User user);

    /**
     * 刪除使用者
     * @param id 要刪除的使用者ID
     */
    void deleteUser(Long id);

    /**
     * 驗證使用者密碼
     * @param rawPassword 原始密碼
     * @param encodedPassword 加密後的密碼
     * @return 如果密碼正確返回true，否則返回false
     */
    boolean verifyPassword(String rawPassword, String encodedPassword);

    /**
     * 加密密碼
     * @param rawPassword 原始密碼
     * @return 加密後的密碼
     */
    String encodePassword(String rawPassword);

    /**
     * 檢查使用者名稱是否已存在
     * @param username 使用者名稱
     * @return 如果存在返回true，否則返回false
     */
    boolean isUsernameExists(String username);

    /**
     * 檢查電子郵件是否已存在
     * @param email 電子郵件
     * @return 如果存在返回true，否則返回false
     */
    boolean isEmailExists(String email);
}