package com.example.demo.service;

import java.math.BigDecimal;
import java.util.List;

import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;

/**
 * 訂單項目服務層介面
 * 封裝訂單項目相關的業務邏輯
 */
public interface OrderItemService {

    /**
     * 取得所有訂單項目
     * @return 所有訂單項目的列表
     */
    List<OrderItem> getAll();

    /**
     * 根據ID取得訂單項目
     * @param id 訂單項目ID
     * @return 訂單項目物件，如果不存在則返回null
     */
    OrderItem getById(Long id);

    /**
     * 根據訂單取得所有訂單項目
     * @param order 訂單
     * @return 該訂單的所有項目列表
     */
    List<OrderItem> getByOrder(Order order);

    /**
     * 根據訂單ID取得所有訂單項目
     * @param orderId 訂單ID
     * @return 該訂單的所有項目列表
     */
    List<OrderItem> getByOrderId(Long orderId);

    /**
     * 根據商品取得所有訂單項目
     * @param product 商品
     * @return 包含該商品的所有訂單項目列表
     */
    List<OrderItem> getByProduct(Product product);

    /**
     * 根據商品ID取得所有訂單項目
     * @param productId 商品ID
     * @return 包含該商品的所有訂單項目列表
     */
    List<OrderItem> getByProductId(Long productId);

    /**
     * 建立新的訂單項目
     * @param order 所屬訂單
     * @param product 商品
     * @param quantity 數量
     * @return 新建立的訂單項目
     */
    OrderItem createOrderItem(Order order, Product product, Integer quantity);

    /**
     * 儲存訂單項目
     * @param orderItem 要儲存的訂單項目
     * @return 儲存後的訂單項目
     */
    OrderItem saveOrderItem(OrderItem orderItem);

    /**
     * 更新訂單項目
     * @param id 訂單項目ID
     * @param orderItem 要更新的訂單項目
     * @return 更新後的訂單項目
     */
    OrderItem updateOrderItem(Long id, OrderItem orderItem);

    /**
     * 刪除訂單項目
     * @param id 要刪除的訂單項目ID
     */
    void deleteOrderItem(Long id);

    /**
     * 更新訂單項目數量
     * @param orderItemId 訂單項目ID
     * @param quantity 新數量
     * @return 更新後的訂單項目
     */
    OrderItem updateQuantity(Long orderItemId, Integer quantity);

    /**
     * 更新訂單項目單價
     * @param orderItemId 訂單項目ID
     * @param unitPrice 新單價
     * @return 更新後的訂單項目
     */
    OrderItem updateUnitPrice(Long orderItemId, BigDecimal unitPrice);

    /**
     * 重新計算訂單項目小計
     * @param orderItemId 訂單項目ID
     * @return 更新後的訂單項目
     */
    OrderItem recalculateSubtotal(Long orderItemId);

    /**
     * 批量更新訂單項目
     * @param orderItems 要更新的訂單項目列表
     * @return 更新後的訂單項目列表
     */
    List<OrderItem> updateOrderItems(List<OrderItem> orderItems);

    /**
     * 批量刪除訂單項目
     * @param orderItemIds 要刪除的訂單項目ID列表
     */
    void deleteOrderItems(List<Long> orderItemIds);

    /**
     * 檢查商品庫存是否足夠
     * @param productId 商品ID
     * @param quantity 需要的數量
     * @return 如果庫存足夠返回true，否則返回false
     */
    boolean checkStockAvailability(Long productId, Integer quantity);

    /**
     * 檢查訂單項目是否可以修改
     * @param orderItemId 訂單項目ID
     * @return 如果可以修改返回true，否則返回false
     */
    boolean canModifyOrderItem(Long orderItemId);

    /**
     * 取得訂單項目的商品銷售統計
     * @param productId 商品ID
     * @return 商品銷售統計資訊
     */
    ProductSalesStatistics getProductSalesStatistics(Long productId);

    /**
     * 取得訂單的總項目數
     * @param orderId 訂單ID
     * @return 訂單的總項目數
     */
    Integer getTotalItemCount(Long orderId);

    /**
     * 取得訂單的總商品數量
     * @param orderId 訂單ID
     * @return 訂單的總商品數量
     */
    Integer getTotalQuantity(Long orderId);

    /**
     * 商品銷售統計資訊內部類
     */
    class ProductSalesStatistics {
        private Long totalOrders;
        private Integer totalQuantity;
        private BigDecimal totalRevenue;
        private BigDecimal averagePrice;

        // 建構子
        public ProductSalesStatistics() {}

        public ProductSalesStatistics(Long totalOrders, Integer totalQuantity, BigDecimal totalRevenue, BigDecimal averagePrice) {
            this.totalOrders = totalOrders;
            this.totalQuantity = totalQuantity;
            this.totalRevenue = totalRevenue;
            this.averagePrice = averagePrice;
        }

        // Getters and Setters
        public Long getTotalOrders() { return totalOrders; }
        public void setTotalOrders(Long totalOrders) { this.totalOrders = totalOrders; }

        public Integer getTotalQuantity() { return totalQuantity; }
        public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }

        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

        public BigDecimal getAveragePrice() { return averagePrice; }
        public void setAveragePrice(BigDecimal averagePrice) { this.averagePrice = averagePrice; }
    }
}
