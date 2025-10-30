package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.OrderDAO;
import com.example.demo.model.Customer;
import com.example.demo.model.Order;
import com.example.demo.model.Order.OrderStatus;
import com.example.demo.model.Order.PaymentStatus;
import com.example.demo.service.OrderService;

/**
 * 訂單服務層實作類
 * 實作訂單相關的業務邏輯
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDAO orderDAO;

    /**
     * 取得所有訂單
     * @return 所有訂單的列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<Order> getAll() {
        logger.info("取得所有訂單");
        List<Order> orders = orderDAO.findAll();
        logger.debug("取得訂單數量: {}", orders.size());
        return orders;
    }

    /**
     * 根據ID取得訂單
     * @param id 訂單ID
     * @return 訂單物件，如果不存在則返回null
     */
    @Override
    @Transactional(readOnly = true)
    public Order getById(Long id) {
        logger.info("根據ID取得訂單 - orderId: {}", id);
        Order order = orderDAO.findById(id);
        logger.debug("訂單查詢結果 - orderId: {}, found: {}", id, order != null);
        return order;
    }

    /**
     * 根據訂單編號取得訂單
     * @param orderNumber 訂單編號
     * @return 訂單物件，如果不存在則返回null
     */
    @Override
    @Transactional(readOnly = true)
    public Order getByOrderNumber(String orderNumber) {
        logger.info("根據訂單編號取得訂單 - orderNumber: {}", orderNumber);
        Order order = orderDAO.findByOrderNumber(orderNumber);
        logger.debug("訂單查詢結果 - orderNumber: {}, found: {}", orderNumber, order != null);
        return order;
    }

    /**
     * 根據客戶取得所有訂單
     * @param customer 客戶
     * @return 該客戶的所有訂單列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<Order> getByCustomer(Customer customer) {
        logger.info("根據客戶取得訂單 - customerId: {}", customer.getId());
        List<Order> orders = orderDAO.findByCustomer(customer);
        logger.debug("客戶訂單查詢結果 - customerId: {}, orderCount: {}", customer.getId(), orders.size());
        return orders;
    }

    /**
     * 根據客戶ID取得所有訂單
     * @param customerId 客戶ID
     * @return 該客戶的所有訂單列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<Order> getByCustomerId(Long customerId) {
        logger.info("根據客戶ID取得訂單 - customerId: {}", customerId);
        List<Order> orders = orderDAO.findByCustomerId(customerId);
        logger.debug("客戶訂單查詢結果 - customerId: {}, orderCount: {}", customerId, orders.size());
        return orders;
    }

    /**
     * 根據訂單狀態取得訂單列表
     * @param status 訂單狀態
     * @return 指定狀態的訂單列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<Order> getByStatus(OrderStatus status) {
        logger.info("根據狀態取得訂單 - status: {}", status);
        List<Order> orders = orderDAO.findByStatus(status);
        logger.debug("狀態訂單查詢結果 - status: {}, orderCount: {}", status, orders.size());
        return orders;
    }

    /**
     * 根據付款狀態取得訂單列表
     * @param paymentStatus 付款狀態
     * @return 指定付款狀態的訂單列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<Order> getByPaymentStatus(PaymentStatus paymentStatus) {
        logger.info("根據付款狀態取得訂單 - paymentStatus: {}", paymentStatus);
        List<Order> orders = orderDAO.findByPaymentStatus(paymentStatus);
        logger.debug("付款狀態訂單查詢結果 - paymentStatus: {}, orderCount: {}", paymentStatus, orders.size());
        return orders;
    }

    /**
     * 建立新訂單
     * @param customer 客戶
     * @param recipientName 收件人姓名
     * @param shippingAddress 收件地址
     * @param paymentMethod 付款方式
     * @return 新建立的訂單
     */
    @Override
    public Order createOrder(Customer customer, Order.PaymentMethod paymentMethod) {
        logger.info("建立新訂單 - customerId: {}, paymentMethod: {}", 
                   customer.getId(), paymentMethod);
        
        Order order = new Order(customer, paymentMethod);
        Order savedOrder = orderDAO.save(order);
        
        logger.info("訂單建立成功 - orderId: {}, orderNumber: {}", savedOrder.getId(), savedOrder.getOrderNumber());
        return savedOrder;
    }

    /**
     * 儲存訂單
     * @param order 要儲存的訂單
     * @return 儲存後的訂單
     */
    @Override
    public Order saveOrder(Order order) {
        logger.info("儲存訂單 - orderId: {}, orderNumber: {}", order.getId(), order.getOrderNumber());
        Order savedOrder = orderDAO.save(order);
        logger.debug("訂單儲存成功 - orderId: {}", savedOrder.getId());
        return savedOrder;
    }

    /**
     * 更新訂單
     * @param id 訂單ID
     * @param order 要更新的訂單
     * @return 更新後的訂單
     */
    @Override
    public Order updateOrder(Long id, Order order) {
        logger.info("更新訂單 - orderId: {}", id);
        
        Order existingOrder = orderDAO.findById(id);
        if (existingOrder == null) {
            logger.warn("訂單不存在 - orderId: {}", id);
            throw new IllegalArgumentException("訂單不存在: " + id);
        }
        
        // 更新訂單資訊
        existingOrder.setPaymentMethod(order.getPaymentMethod());
        existingOrder.setNotes(order.getNotes());
        
        Order updatedOrder = orderDAO.save(existingOrder);
        logger.info("訂單更新成功 - orderId: {}", updatedOrder.getId());
        return updatedOrder;
    }

    /**
     * 刪除訂單
     * @param id 要刪除的訂單ID
     */
    @Override
    public void deleteOrder(Long id) {
        logger.info("刪除訂單 - orderId: {}", id);
        
        Order order = orderDAO.findById(id);
        if (order == null) {
            logger.warn("要刪除的訂單不存在 - orderId: {}", id);
            throw new IllegalArgumentException("訂單不存在: " + id);
        }
        
        orderDAO.delete(id);
        logger.info("訂單刪除成功 - orderId: {}", id);
    }

    /**
     * 更新訂單狀態
     * @param orderId 訂單ID
     * @param status 新狀態
     * @return 更新後的訂單
     */
    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        logger.info("更新訂單狀態 - orderId: {}, newStatus: {}", orderId, status);
        
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            logger.warn("訂單不存在 - orderId: {}", orderId);
            throw new IllegalArgumentException("訂單不存在: " + orderId);
        }
        
        OrderStatus oldStatus = order.getStatus();
        order.setStatus(status);
        Order updatedOrder = orderDAO.save(order);
        
        logger.info("訂單狀態更新成功 - orderId: {}, oldStatus: {}, newStatus: {}", 
                   orderId, oldStatus, status);
        return updatedOrder;
    }

    /**
     * 更新付款狀態
     * @param orderId 訂單ID
     * @param paymentStatus 新付款狀態
     * @return 更新後的訂單
     */
    @Override
    public Order updatePaymentStatus(Long orderId, PaymentStatus paymentStatus) {
        logger.info("更新付款狀態 - orderId: {}, newPaymentStatus: {}", orderId, paymentStatus);
        
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            logger.warn("訂單不存在 - orderId: {}", orderId);
            throw new IllegalArgumentException("訂單不存在: " + orderId);
        }
        
        PaymentStatus oldStatus = order.getPaymentStatus();
        order.setPaymentStatus(paymentStatus);
        Order updatedOrder = orderDAO.save(order);
        
        logger.info("付款狀態更新成功 - orderId: {}, oldStatus: {}, newStatus: {}", 
                   orderId, oldStatus, paymentStatus);
        return updatedOrder;
    }

    /**
     * 設定訂單運費
     * @param orderId 訂單ID
     * @param shippingFee 運費
     * @return 更新後的訂單
     */
    @Override
    public Order setShippingFee(Long orderId, BigDecimal shippingFee) {
        logger.info("設定訂單運費 - orderId: {}, shippingFee: {}", orderId, shippingFee);
        
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            logger.warn("訂單不存在 - orderId: {}", orderId);
            throw new IllegalArgumentException("訂單不存在: " + orderId);
        }
        
        order.setShippingFee(shippingFee);
        Order updatedOrder = orderDAO.save(order);
        
        logger.info("運費設定成功 - orderId: {}, shippingFee: {}", orderId, shippingFee);
        return updatedOrder;
    }

    /**
     * 設定訂單折扣
     * @param orderId 訂單ID
     * @param discountAmount 折扣金額
     * @return 更新後的訂單
     */
    @Override
    public Order setDiscountAmount(Long orderId, BigDecimal discountAmount) {
        logger.info("設定訂單折扣 - orderId: {}, discountAmount: {}", orderId, discountAmount);
        
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            logger.warn("訂單不存在 - orderId: {}", orderId);
            throw new IllegalArgumentException("訂單不存在: " + orderId);
        }
        
        order.setDiscountAmount(discountAmount);
        Order updatedOrder = orderDAO.save(order);
        
        logger.info("折扣設定成功 - orderId: {}, discountAmount: {}", orderId, discountAmount);
        return updatedOrder;
    }

    /**
     * 重新計算訂單金額
     * @param orderId 訂單ID
     * @return 更新後的訂單
     */
    @Override
    public Order recalculateOrderAmount(Long orderId) {
        logger.info("重新計算訂單金額 - orderId: {}", orderId);
        
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            logger.warn("訂單不存在 - orderId: {}", orderId);
            throw new IllegalArgumentException("訂單不存在: " + orderId);
        }
        
        order.calculateTotalAmount();
        Order updatedOrder = orderDAO.save(order);
        
        logger.info("訂單金額重新計算完成 - orderId: {}, totalAmount: {}, finalAmount: {}", 
                   orderId, updatedOrder.getTotalAmount(), updatedOrder.getFinalAmount());
        return updatedOrder;
    }

    /**
     * 取消訂單
     * @param orderId 訂單ID
     * @param reason 取消原因
     * @return 更新後的訂單
     */
    @Override
    public Order cancelOrder(Long orderId, String reason) {
        logger.info("取消訂單 - orderId: {}, reason: {}", orderId, reason);
        
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            logger.warn("訂單不存在 - orderId: {}", orderId);
            throw new IllegalArgumentException("訂單不存在: " + orderId);
        }
        
        if (!canCancelOrder(orderId)) {
            logger.warn("訂單無法取消 - orderId: {}, currentStatus: {}", orderId, order.getStatus());
            throw new IllegalStateException("訂單無法取消，當前狀態: " + order.getStatus());
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        if (reason != null && !reason.trim().isEmpty()) {
            String currentNotes = order.getNotes();
            String newNotes = currentNotes == null ? reason : currentNotes + "\n取消原因: " + reason;
            order.setNotes(newNotes);
        }
        
        Order updatedOrder = orderDAO.save(order);
        logger.info("訂單取消成功 - orderId: {}", orderId);
        return updatedOrder;
    }

    /**
     * 確認訂單
     * @param orderId 訂單ID
     * @return 更新後的訂單
     */
    @Override
    public Order confirmOrder(Long orderId) {
        logger.info("確認訂單 - orderId: {}", orderId);
        return updateOrderStatus(orderId, OrderStatus.CONFIRMED);
    }

    /**
     * 處理訂單（開始處理）
     * @param orderId 訂單ID
     * @return 更新後的訂單
     */
    @Override
    public Order processOrder(Long orderId) {
        logger.info("開始處理訂單 - orderId: {}", orderId);
        return updateOrderStatus(orderId, OrderStatus.PROCESSING);
    }

    /**
     * 出貨訂單
     * @param orderId 訂單ID
     * @param trackingNumber 追蹤號碼
     * @return 更新後的訂單
     */
    @Override
    public Order shipOrder(Long orderId, String trackingNumber) {
        logger.info("訂單出貨 - orderId: {}, trackingNumber: {}", orderId, trackingNumber);
        
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            logger.warn("訂單不存在 - orderId: {}", orderId);
            throw new IllegalArgumentException("訂單不存在: " + orderId);
        }
        
        order.setStatus(OrderStatus.SHIPPED);
        if (trackingNumber != null && !trackingNumber.trim().isEmpty()) {
            String currentNotes = order.getNotes();
            String newNotes = currentNotes == null ? "追蹤號碼: " + trackingNumber : 
                             currentNotes + "\n追蹤號碼: " + trackingNumber;
            order.setNotes(newNotes);
        }
        
        Order updatedOrder = orderDAO.save(order);
        logger.info("訂單出貨成功 - orderId: {}, trackingNumber: {}", orderId, trackingNumber);
        return updatedOrder;
    }

    /**
     * 完成訂單（送達）
     * @param orderId 訂單ID
     * @return 更新後的訂單
     */
    @Override
    public Order completeOrder(Long orderId) {
        logger.info("完成訂單 - orderId: {}", orderId);
        return updateOrderStatus(orderId, OrderStatus.DELIVERED);
    }

    /**
     * 檢查訂單是否可以取消
     * @param orderId 訂單ID
     * @return 如果可以取消返回true，否則返回false
     */
    @Override
    @Transactional(readOnly = true)
    public boolean canCancelOrder(Long orderId) {
        logger.debug("檢查訂單是否可以取消 - orderId: {}", orderId);
        
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            logger.warn("訂單不存在 - orderId: {}", orderId);
            return false;
        }
        
        OrderStatus status = order.getStatus();
        boolean canCancel = status == OrderStatus.PENDING || status == OrderStatus.CONFIRMED;
        
        logger.debug("訂單取消檢查結果 - orderId: {}, status: {}, canCancel: {}", 
                    orderId, status, canCancel);
        return canCancel;
    }

    /**
     * 檢查訂單是否可以修改
     * @param orderId 訂單ID
     * @return 如果可以修改返回true，否則返回false
     */
    @Override
    @Transactional(readOnly = true)
    public boolean canModifyOrder(Long orderId) {
        logger.debug("檢查訂單是否可以修改 - orderId: {}", orderId);
        
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            logger.warn("訂單不存在 - orderId: {}", orderId);
            return false;
        }
        
        OrderStatus status = order.getStatus();
        boolean canModify = status == OrderStatus.PENDING;
        
        logger.debug("訂單修改檢查結果 - orderId: {}, status: {}, canModify: {}", 
                    orderId, status, canModify);
        return canModify;
    }

    /**
     * 取得客戶的訂單統計
     * @param customerId 客戶ID
     * @return 訂單統計資訊（總訂單數、總金額等）
     */
    @Override
    @Transactional(readOnly = true)
    public OrderStatistics getCustomerOrderStatistics(Long customerId) {
        logger.info("取得客戶訂單統計 - customerId: {}", customerId);
        
        List<Order> orders = orderDAO.findByCustomerId(customerId);
        
        long totalOrders = orders.size();
        BigDecimal totalAmount = orders.stream()
                .map(Order::getFinalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        long pendingOrders = orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.PENDING)
                .count();
        
        long completedOrders = orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .count();
        
        long cancelledOrders = orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.CANCELLED)
                .count();
        
        OrderStatistics statistics = new OrderStatistics(
                totalOrders, totalAmount, pendingOrders, completedOrders, cancelledOrders);
        
        logger.info("客戶訂單統計完成 - customerId: {}, totalOrders: {}, totalAmount: {}", 
                   customerId, totalOrders, totalAmount);
        return statistics;
    }
}
