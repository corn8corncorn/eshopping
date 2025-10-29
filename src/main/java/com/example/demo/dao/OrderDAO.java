package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.Customer;
import com.example.demo.model.Order;
import com.example.demo.model.Order.OrderStatus;
import com.example.demo.model.Order.PaymentStatus;

/**
 * 訂單資料存取物件介面
 * 定義訂單相關的資料庫操作
 */
public interface OrderDAO {

    /**
     * 儲存訂單
     * @param order 要儲存的訂單
     * @return 儲存後的訂單
     */
    Order save(Order order);

    /**
     * 根據ID查找訂單
     * @param id 訂單ID
     * @return 訂單物件，如果不存在則返回null
     */
    Order findById(Long id);

    /**
     * 查找所有訂單
     * @return 所有訂單的列表
     */
    List<Order> findAll();

    /**
     * 根據訂單編號查找訂單
     * @param orderNumber 訂單編號
     * @return 訂單物件，如果不存在則返回null
     */
    Order findByOrderNumber(String orderNumber);

    /**
     * 根據客戶查找訂單
     * @param customer 客戶
     * @return 該客戶的所有訂單列表
     */
    List<Order> findByCustomer(Customer customer);

    /**
     * 根據客戶ID查找訂單
     * @param customerId 客戶ID
     * @return 該客戶的所有訂單列表
     */
    List<Order> findByCustomerId(Long customerId);

    /**
     * 根據訂單狀態查找訂單
     * @param status 訂單狀態
     * @return 指定狀態的訂單列表
     */
    List<Order> findByStatus(OrderStatus status);

    /**
     * 根據付款狀態查找訂單
     * @param paymentStatus 付款狀態
     * @return 指定付款狀態的訂單列表
     */
    List<Order> findByPaymentStatus(PaymentStatus paymentStatus);

    /**
     * 刪除訂單
     * @param id 要刪除的訂單ID
     */
    void delete(Long id);

    /**
     * 檢查訂單是否存在
     * @param id 訂單ID
     * @return 如果存在返回true，否則返回false
     */
    boolean existsById(Long id);

    /**
     * 計算訂單總數
     * @return 訂單總數
     */
    long count();

    /**
     * 根據客戶ID計算訂單總數
     * @param customerId 客戶ID
     * @return 該客戶的訂單總數
     */
    long countByCustomerId(Long customerId);

    /**
     * 根據訂單狀態計算訂單總數
     * @param status 訂單狀態
     * @return 指定狀態的訂單總數
     */
    long countByStatus(OrderStatus status);

    /**
     * 根據付款狀態計算訂單總數
     * @param paymentStatus 付款狀態
     * @return 指定付款狀態的訂單總數
     */
    long countByPaymentStatus(PaymentStatus paymentStatus);
}
