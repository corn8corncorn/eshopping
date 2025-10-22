package com.example.demo.dao;

import java.util.List;
//import java.util.Optional;

import com.example.demo.model.User;

public interface UserDAO {

    /**
     * 儲存新使用者
     * @param user 要儲存的使用者
     * @return 儲存後的使用者（包含生成的ID）
     */
    User save(User user);

    /**
     * 根據ID取得使用者
     * @param id 使用者ID
     * @return 使用者物件，如果不存在則返回null
     */
    User getById(Long id);

    /**
     * 根據使用者名稱取得使用者
     * @param username 使用者名稱
     * @return 使用者物件，如果不存在則返回null
     */
    User getByUsername(String username);

    /**
     * 根據電子郵件取得使用者
     * @param email 電子郵件
     * @return 使用者物件，如果不存在則返回null
     */
    User getByEmail(String email);

    /**
     * 取得所有使用者
     * @return 所有使用者的列表
     */
    List<User> getAll();

    /**
     * 更新使用者資訊
     * @param user 要更新的使用者
     * @return 更新後的使用者
     */
    User update(User user);

    /**
     * 根據ID刪除使用者
     * @param id 要刪除的使用者ID
     */
    void deleteById(Long id);

    /**
     * 檢查使用者名稱是否已存在
     * @param username 使用者名稱
     * @return 如果存在返回true，否則返回false
     */
    boolean existsByUsername(String username);

    /**
     * 檢查電子郵件是否已存在
     * @param email 電子郵件
     * @return 如果存在返回true，否則返回false
     */
    boolean existsByEmail(String email);
}