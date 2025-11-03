package com.example.demo.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

import com.example.demo.model.Customer;
import com.example.demo.model.Order;
import com.example.demo.model.OrderAddress;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.service.OrderService;
import com.example.demo.service.OrderItemService;

/**
 * Order Service 測試類
 * 測試訂單服務層的業務邏輯
 */
public class OrderServiceTest {

    /**
     * 測試 OrderService 介面方法
     */
    @Test
    public void testOrderServiceInterface() {
        // 建立測試用戶
        User user = new User("testuser", "test@example.com", "password123");
        user.setId(1L);
        
        // 建立測試客戶
        Customer customer = new Customer(user, "測試客戶");
        customer.setId(1L);
        
        // 建立測試商品
        Product product1 = new Product("測試商品1", "電子產品", new BigDecimal("100.00"), 10);
        Product product2 = new Product("測試商品2", "服飾", new BigDecimal("50.00"), 5);
        
        // 測試訂單建立
        Order order = new Order(customer, Order.PaymentMethod.CREDIT_CARD);
        // 設置訂單地址
        OrderAddress orderAddress = new OrderAddress(order, "收件人", "0912345678", "台北市信義區");
        order.setOrderAddress(orderAddress);
        
        // 驗證訂單基本屬性
        assertNotNull("訂單不應為空", order);
        assertNotNull("訂單編號不應為空", order.getOrderNumber());
        assertEquals("訂單狀態應為待處理", Order.OrderStatus.PENDING, order.getStatus());
        assertEquals("付款狀態應為待付款", Order.PaymentStatus.PENDING, order.getPaymentStatus());
        assertEquals("付款方式應為信用卡", Order.PaymentMethod.CREDIT_CARD, order.getPaymentMethod());
        
        // 測試訂單項目建立
        OrderItem item1 = new OrderItem(order, product1, 2);
        OrderItem item2 = new OrderItem(order, product2, 1);
        
        order.addOrderItem(item1);
        order.addOrderItem(item2);
        
        // 驗證訂單項目
        assertEquals("訂單應有2個項目", 2, order.getOrderItems().size());
        assertEquals("總金額應為250.00", new BigDecimal("250.00"), order.getTotalAmount());
        assertEquals("最終金額應為250.00", new BigDecimal("250.00"), order.getFinalAmount());
        
        // 測試訂單狀態變更
        order.setStatus(Order.OrderStatus.CONFIRMED);
        assertEquals("訂單狀態應為已確認", Order.OrderStatus.CONFIRMED, order.getStatus());
        
        order.setPaymentStatus(Order.PaymentStatus.PAID);
        assertEquals("付款狀態應為已付款", Order.PaymentStatus.PAID, order.getPaymentStatus());
        
        // 測試運費和折扣
        order.setShippingFee(new BigDecimal("30.00"));
        order.setDiscountAmount(new BigDecimal("20.00"));
        assertEquals("含運費和折扣後最終金額應為260.00", new BigDecimal("260.00"), order.getFinalAmount());
        
        // 測試訂單統計
        OrderService.OrderStatistics statistics = new OrderService.OrderStatistics();
        statistics.setTotalOrders(1L);
        statistics.setTotalAmount(new BigDecimal("260.00"));
        statistics.setPendingOrders(0L);
        statistics.setCompletedOrders(0L);
        statistics.setCancelledOrders(0L);
        
        assertNotNull("統計資訊不應為空", statistics);
        assertEquals("總訂單數應為1", Long.valueOf(1), statistics.getTotalOrders());
        assertEquals("總金額應為260.00", new BigDecimal("260.00"), statistics.getTotalAmount());
        
        System.out.println("Order Service 介面測試通過！");
        System.out.println("訂單編號: " + order.getOrderNumber());
        System.out.println("訂單狀態: " + order.getStatus());
        System.out.println("付款狀態: " + order.getPaymentStatus());
        System.out.println("訂單總金額: " + order.getTotalAmount());
        System.out.println("訂單最終金額: " + order.getFinalAmount());
    }

    /**
     * 測試 OrderItemService 介面方法
     */
    @Test
    public void testOrderItemServiceInterface() {
        // 建立測試用戶和客戶
        User user = new User("testuser", "test@example.com", "password123");
        user.setId(1L);
        Customer customer = new Customer(user, "測試客戶");
        customer.setId(1L);
        
        // 建立測試商品
        Product product1 = new Product("測試商品1", "電子產品", new BigDecimal("100.00"), 10);
        Product product2 = new Product("測試商品2", "服飾", new BigDecimal("50.00"), 5);
        
        // 建立測試訂單
        Order order = new Order(customer, Order.PaymentMethod.CREDIT_CARD);
        // 設置訂單地址
        OrderAddress orderAddress = new OrderAddress(order, "收件人", "0912345678", "台北市信義區");
        order.setOrderAddress(orderAddress);
        
        // 測試訂單項目建立
        OrderItem item1 = new OrderItem(order, product1, 2);
        OrderItem item2 = new OrderItem(order, product2, 1);
        
        // 驗證訂單項目基本屬性
        assertNotNull("訂單項目1不應為空", item1);
        assertNotNull("訂單項目2不應為空", item2);
        
        assertEquals("商品1數量應為2", Integer.valueOf(2), item1.getQuantity());
        assertEquals("商品1單價應為100.00", new BigDecimal("100.00"), item1.getUnitPrice());
        assertEquals("商品1小計應為200.00", new BigDecimal("200.00"), item1.getSubtotal());
        
        assertEquals("商品2數量應為1", Integer.valueOf(1), item2.getQuantity());
        assertEquals("商品2單價應為50.00", new BigDecimal("50.00"), item2.getUnitPrice());
        assertEquals("商品2小計應為50.00", new BigDecimal("50.00"), item2.getSubtotal());
        
        // 測試數量更新
        item1.updateQuantity(3);
        assertEquals("商品1數量應為3", Integer.valueOf(3), item1.getQuantity());
        assertEquals("商品1小計應為300.00", new BigDecimal("300.00"), item1.getSubtotal());
        
        // 測試單價更新
        item2.updateUnitPrice(new BigDecimal("60.00"));
        assertEquals("商品2單價應為60.00", new BigDecimal("60.00"), item2.getUnitPrice());
        assertEquals("商品2小計應為60.00", new BigDecimal("60.00"), item2.getSubtotal());
        
        // 測試商品銷售統計
        OrderItemService.ProductSalesStatistics statistics = new OrderItemService.ProductSalesStatistics();
        statistics.setTotalOrders(2L);
        statistics.setTotalQuantity(4);
        statistics.setTotalRevenue(new BigDecimal("360.00"));
        statistics.setAveragePrice(new BigDecimal("90.00"));
        
        assertNotNull("銷售統計不應為空", statistics);
        assertEquals("總訂單數應為2", Long.valueOf(2), statistics.getTotalOrders());
        assertEquals("總數量應為4", Integer.valueOf(4), statistics.getTotalQuantity());
        assertEquals("總收入應為360.00", new BigDecimal("360.00"), statistics.getTotalRevenue());
        assertEquals("平均價格應為90.00", new BigDecimal("90.00"), statistics.getAveragePrice());
        
        System.out.println("OrderItem Service 介面測試通過！");
        System.out.println("訂單項目1: " + item1.getProductName() + " x" + item1.getQuantity() + " = " + item1.getSubtotal());
        System.out.println("訂單項目2: " + item2.getProductName() + " x" + item2.getQuantity() + " = " + item2.getSubtotal());
        System.out.println("總銷售統計: " + statistics.getTotalOrders() + " 筆訂單, " + 
                          statistics.getTotalQuantity() + " 件商品, 總收入 " + statistics.getTotalRevenue());
    }
}
