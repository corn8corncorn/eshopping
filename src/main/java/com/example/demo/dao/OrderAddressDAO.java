package com.example.demo.dao;

import com.example.demo.model.Order;
import com.example.demo.model.OrderAddress;

/**
 * 訂單地址資料存取物件介面
 * 定義訂單地址相關的資料庫操作
 */
public interface OrderAddressDAO {

    /**
     * 儲存訂單地址
     * @param orderAddress 要儲存的訂單地址
     * @return 儲存後的訂單地址
     */
    OrderAddress save(OrderAddress orderAddress);

    /**
     * 根據ID查找訂單地址
     * @param id 訂單地址ID
     * @return 訂單地址物件，如果不存在則返回null
     */
    OrderAddress findById(Long id);

    /**
     * 根據訂單查找訂單地址
     * @param order 訂單
     * @return 訂單地址物件，如果不存在則返回null
     */
    OrderAddress findByOrder(Order order);

    /**
     * 根據訂單ID查找訂單地址
     * @param orderId 訂單ID
     * @return 訂單地址物件，如果不存在則返回null
     */
    OrderAddress findByOrderId(Long orderId);

    /**
     * 刪除訂單地址
     * @param id 要刪除的訂單地址ID
     */
    void delete(Long id);
}

