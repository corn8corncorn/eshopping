package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Customer;
import com.example.demo.model.User;

/**
 * 客戶服務層介面
 * 封裝客戶相關的業務邏輯
 */
public interface CustomerService {

    /**
     * 取得所有客戶
     */
    List<Customer> getAll();

    /**
     * 依 ID 取得客戶
     */
    Customer getById(Long id);

    /**
     * 依 User 取得客戶
     */
    Customer getByUser(User user);

    /**
     * 新增客戶
     */
    void save(Customer customer);

    /**
     * 更新客戶
     */
    void update(Long id, Customer customer);

    /**
     * 刪除客戶
     */
    void delete(Long id);

    /**
     * 為新註冊用戶建立客戶資料
     */
    Customer createCustomerForUser(User user, String fullName);
}
