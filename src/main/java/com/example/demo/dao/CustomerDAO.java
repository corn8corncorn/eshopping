package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.Customer;
import com.example.demo.model.User;

/**
 * 客戶資料存取層 DAO 介面
 * 定義對客戶的基本 CRUD 操作
 */
public interface CustomerDAO {

    /**
     * 取得所有客戶
     * @return 客戶清單
     */
    List<Customer> getAll();

    /**
     * 依 ID 取得客戶
     * @param id 客戶 ID
     * @return 客戶，若不存在則為 null
     */
    Customer getById(Long id);

    /**
     * 依 User 取得客戶
     * @param user 用戶
     * @return 客戶，若不存在則為 null
     */
    Customer getByUser(User user);

    /**
     * 新增或更新客戶
     * @param customer 客戶實體
     */
    void save(Customer customer);

    /**
     * 依 ID 刪除客戶
     * @param id 客戶 ID
     */
    void delete(Long id);
}
