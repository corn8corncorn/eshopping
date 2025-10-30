package com.example.demo.service;

import java.math.BigDecimal;
import java.util.List;

import com.example.demo.model.Customer;
import com.example.demo.model.Order;
import com.example.demo.model.Order.OrderStatus;
import com.example.demo.model.Order.PaymentMethod;
import com.example.demo.model.Order.PaymentStatus;

/**
 * 訂單服務層介面
 * 封裝訂單相關的業務邏輯
 */
public interface OrderService {

    /**
     * 取得所有訂單
     * @return 所有訂單的列表
     */
    List<Order> getAll();

    /**
     * 根據ID取得訂單
     * @param id 訂單ID
     * @return 訂單物件，如果不存在則返回null
     */
    Order getById(Long id);

    /**
     * 根據訂單編號取得訂單
     * @param orderNumber 訂單編號
     * @return 訂單物件，如果不存在則返回null
     */
    Order getByOrderNumber(String orderNumber);

    /**
     * 根據客戶取得所有訂單
     * @param customer 客戶
     * @return 該客戶的所有訂單列表
     */
    List<Order> getByCustomer(Customer customer);

    /**
     * 根據客戶ID取得所有訂單
     * @param customerId 客戶ID
     * @return 該客戶的所有訂單列表
     */
    List<Order> getByCustomerId(Long customerId);

    /**
     * 根據訂單狀態取得訂單列表
     * @param status 訂單狀態
     * @return 指定狀態的訂單列表
     */
    List<Order> getByStatus(OrderStatus status);

    /**
     * 根據付款狀態取得訂單列表
     * @param paymentStatus 付款狀態
     * @return 指定付款狀態的訂單列表
     */
    List<Order> getByPaymentStatus(PaymentStatus paymentStatus);

    /**
     * 建立新訂單
     * @param customer 客戶
     * @param recipientName 收件人姓名
     * @param shippingAddress 收件地址
     * @param paymentMethod 付款方式
     * @return 新建立的訂單
     */
    Order createOrder(Customer customer, PaymentMethod paymentMethod);

    /**
     * 儲存訂單
     * @param order 要儲存的訂單
     * @return 儲存後的訂單
     */
    Order saveOrder(Order order);

    /**
     * 更新訂單
     * @param id 訂單ID
     * @param order 要更新的訂單
     * @return 更新後的訂單
     */
    Order updateOrder(Long id, Order order);

    /**
     * 刪除訂單
     * @param id 要刪除的訂單ID
     */
    void deleteOrder(Long id);

    /**
     * 更新訂單狀態
     * @param orderId 訂單ID
     * @param status 新狀態
     * @return 更新後的訂單
     */
    Order updateOrderStatus(Long orderId, OrderStatus status);

    /**
     * 更新付款狀態
     * @param orderId 訂單ID
     * @param paymentStatus 新付款狀態
     * @return 更新後的訂單
     */
    Order updatePaymentStatus(Long orderId, PaymentStatus paymentStatus);

    /**
     * 設定訂單運費
     * @param orderId 訂單ID
     * @param shippingFee 運費
     * @return 更新後的訂單
     */
    Order setShippingFee(Long orderId, BigDecimal shippingFee);

    /**
     * 設定訂單折扣
     * @param orderId 訂單ID
     * @param discountAmount 折扣金額
     * @return 更新後的訂單
     */
    Order setDiscountAmount(Long orderId, BigDecimal discountAmount);

    /**
     * 重新計算訂單金額
     * @param orderId 訂單ID
     * @return 更新後的訂單
     */
    Order recalculateOrderAmount(Long orderId);

    /**
     * 取消訂單
     * @param orderId 訂單ID
     * @param reason 取消原因
     * @return 更新後的訂單
     */
    Order cancelOrder(Long orderId, String reason);

    /**
     * 確認訂單
     * @param orderId 訂單ID
     * @return 更新後的訂單
     */
    Order confirmOrder(Long orderId);

    /**
     * 處理訂單（開始處理）
     * @param orderId 訂單ID
     * @return 更新後的訂單
     */
    Order processOrder(Long orderId);

    /**
     * 出貨訂單
     * @param orderId 訂單ID
     * @param trackingNumber 追蹤號碼
     * @return 更新後的訂單
     */
    Order shipOrder(Long orderId, String trackingNumber);

    /**
     * 完成訂單（送達）
     * @param orderId 訂單ID
     * @return 更新後的訂單
     */
    Order completeOrder(Long orderId);

    /**
     * 檢查訂單是否可以取消
     * @param orderId 訂單ID
     * @return 如果可以取消返回true，否則返回false
     */
    boolean canCancelOrder(Long orderId);

    /**
     * 檢查訂單是否可以修改
     * @param orderId 訂單ID
     * @return 如果可以修改返回true，否則返回false
     */
    boolean canModifyOrder(Long orderId);

    /**
     * 取得客戶的訂單統計
     * @param customerId 客戶ID
     * @return 訂單統計資訊（總訂單數、總金額等）
     */
    OrderStatistics getCustomerOrderStatistics(Long customerId);

    /**
     * 訂單統計資訊內部類
     */
    class OrderStatistics {
        private Long totalOrders;
        private BigDecimal totalAmount;
        private Long pendingOrders;
        private Long completedOrders;
        private Long cancelledOrders;

        // 建構子
        public OrderStatistics() {}

        public OrderStatistics(Long totalOrders, BigDecimal totalAmount, Long pendingOrders, Long completedOrders, Long cancelledOrders) {
            this.totalOrders = totalOrders;
            this.totalAmount = totalAmount;
            this.pendingOrders = pendingOrders;
            this.completedOrders = completedOrders;
            this.cancelledOrders = cancelledOrders;
        }

        // Getters and Setters
        public Long getTotalOrders() { return totalOrders; }
        public void setTotalOrders(Long totalOrders) { this.totalOrders = totalOrders; }

        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

        public Long getPendingOrders() { return pendingOrders; }
        public void setPendingOrders(Long pendingOrders) { this.pendingOrders = pendingOrders; }

        public Long getCompletedOrders() { return completedOrders; }
        public void setCompletedOrders(Long completedOrders) { this.completedOrders = completedOrders; }

        public Long getCancelledOrders() { return cancelledOrders; }
        public void setCancelledOrders(Long cancelledOrders) { this.cancelledOrders = cancelledOrders; }
    }
}
