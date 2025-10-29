package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.Cart;
import com.example.demo.model.Customer;

/**
 * 購物車資料存取物件介面
 * 定義購物車相關的資料庫操作
 */
public interface CartDAO {

    /**
     * 儲存購物車
     * @param cart 要儲存的購物車
     * @return 儲存後的購物車
     */
    Cart save(Cart cart);

    /**
     * 根據ID查找購物車
     * @param id 購物車ID
     * @return 購物車物件，如果不存在則返回null
     */
    Cart findById(Long id);

    /**
     * 查找所有購物車
     * @return 所有購物車的列表
     */
    List<Cart> findAll();

    /**
     * 根據客戶查找購物車
     * @param customer 客戶
     * @return 該客戶的購物車，如果不存在則返回null
     */
    Cart findByCustomer(Customer customer);

    /**
     * 根據客戶ID查找購物車
     * @param customerId 客戶ID
     * @return 該客戶的購物車，如果不存在則返回null
     */
    Cart findByCustomerId(Long customerId);

    /**
     * 刪除購物車
     * @param id 要刪除的購物車ID
     */
    void delete(Long id);

    /**
     * 刪除購物車
     * @param cart 要刪除的購物車物件
     */
    void delete(Cart cart);

    /**
     * 檢查購物車是否存在
     * @param id 購物車ID
     * @return 如果存在返回true，否則返回false
     */
    boolean existsById(Long id);

    /**
     * 檢查客戶是否有購物車
     * @param customerId 客戶ID
     * @return 如果有購物車返回true，否則返回false
     */
    boolean existsByCustomerId(Long customerId);

    /**
     * 計算購物車總數
     * @return 購物車總數
     */
    long count();

    /**
     * 根據客戶ID計算購物車總數
     * @param customerId 客戶ID
     * @return 該客戶的購物車數量（通常為0或1）
     */
    long countByCustomerId(Long customerId);
}
