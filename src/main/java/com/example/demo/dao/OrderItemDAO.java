package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;

/**
 * 訂單項目資料存取物件介面
 * 定義訂單項目相關的資料庫操作
 */
public interface OrderItemDAO {

    /**
     * 儲存訂單項目
     * @param orderItem 要儲存的訂單項目
     * @return 儲存後的訂單項目
     */
    OrderItem save(OrderItem orderItem);

    /**
     * 批量儲存訂單項目
     * @param orderItems 要儲存的訂單項目列表
     * @return 儲存後的訂單項目列表
     */
    List<OrderItem> saveAll(List<OrderItem> orderItems);

    /**
     * 根據ID查找訂單項目
     * @param id 訂單項目ID
     * @return 訂單項目物件，如果不存在則返回null
     */
    OrderItem findById(Long id);

    /**
     * 查找所有訂單項目
     * @return 所有訂單項目的列表
     */
    List<OrderItem> findAll();

    /**
     * 根據訂單查找訂單項目
     * @param order 訂單
     * @return 該訂單的所有項目列表
     */
    List<OrderItem> findByOrder(Order order);

    /**
     * 根據訂單ID查找訂單項目
     * @param orderId 訂單ID
     * @return 該訂單的所有項目列表
     */
    List<OrderItem> findByOrderId(Long orderId);

    /**
     * 根據商品查找訂單項目
     * @param product 商品
     * @return 包含該商品的所有訂單項目列表
     */
    List<OrderItem> findByProduct(Product product);

    /**
     * 根據商品ID查找訂單項目
     * @param productId 商品ID
     * @return 包含該商品的所有訂單項目列表
     */
    List<OrderItem> findByProductId(Long productId);

    /**
     * 刪除訂單項目
     * @param id 要刪除的訂單項目ID
     */
    void delete(Long id);

    /**
     * 批量刪除訂單項目
     * @param ids 要刪除的訂單項目ID列表
     */
    void deleteAllById(List<Long> ids);

    /**
     * 檢查訂單項目是否存在
     * @param id 訂單項目ID
     * @return 如果存在返回true，否則返回false
     */
    boolean existsById(Long id);

    /**
     * 計算訂單項目總數
     * @return 訂單項目總數
     */
    long count();

    /**
     * 根據訂單ID計算訂單項目總數
     * @param orderId 訂單ID
     * @return 該訂單的項目總數
     */
    long countByOrderId(Long orderId);

    /**
     * 根據商品ID計算訂單項目總數
     * @param productId 商品ID
     * @return 包含該商品的訂單項目總數
     */
    long countByProductId(Long productId);

    /**
     * 根據訂單ID計算總商品數量
     * @param orderId 訂單ID
     * @return 該訂單的總商品數量
     */
    int sumQuantityByOrderId(Long orderId);

    /**
     * 根據商品ID計算總銷售數量
     * @param productId 商品ID
     * @return 該商品的總銷售數量
     */
    int sumQuantityByProductId(Long productId);
}
