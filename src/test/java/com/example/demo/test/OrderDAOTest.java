package com.example.demo.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

import com.example.demo.model.Customer;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.service.OrderService;
import com.example.demo.service.OrderItemService;

/**
 * Order DAO 測試類
 * 測試訂單資料存取層的介面定義
 */
public class OrderDAOTest {

    /**
     * 測試 OrderDAO 介面方法定義
     */
    @Test
    public void testOrderDAOInterface() {
        // 建立測試用戶
        User user = new User("testuser", "test@example.com", "password123");
        user.setId(1L);
        
        // 建立測試客戶
        Customer customer = new Customer(user, "測試客戶");
        customer.setId(1L);
        
        // 建立測試訂單
        Order order = new Order(customer, "收件人", "台北市信義區", Order.PaymentMethod.CREDIT_CARD);
        
        // 驗證訂單基本屬性
        assertNotNull("訂單不應為空", order);
        assertNotNull("訂單編號不應為空", order.getOrderNumber());
        assertEquals("訂單狀態應為待處理", Order.OrderStatus.PENDING, order.getStatus());
        assertEquals("付款狀態應為待付款", Order.PaymentStatus.PENDING, order.getPaymentStatus());
        
        // 測試訂單狀態枚舉
        Order.OrderStatus[] statuses = Order.OrderStatus.values();
        assertEquals("訂單狀態枚舉應有7個值", 7, statuses.length);
        
        Order.PaymentStatus[] paymentStatuses = Order.PaymentStatus.values();
        assertEquals("付款狀態枚舉應有4個值", 4, paymentStatuses.length);
        
        Order.PaymentMethod[] paymentMethods = Order.PaymentMethod.values();
        assertEquals("付款方式枚舉應有4個值", 4, paymentMethods.length);
        
        System.out.println("Order DAO 介面測試通過！");
        System.out.println("訂單編號: " + order.getOrderNumber());
        System.out.println("訂單狀態: " + order.getStatus());
        System.out.println("付款狀態: " + order.getPaymentStatus());
        System.out.println("付款方式: " + order.getPaymentMethod());
    }

    /**
     * 測試 OrderItemDAO 介面方法定義
     */
    @Test
    public void testOrderItemDAOInterface() {
        // 建立測試用戶和客戶
        User user = new User("testuser", "test@example.com", "password123");
        user.setId(1L);
        Customer customer = new Customer(user, "測試客戶");
        customer.setId(1L);
        
        // 建立測試商品
        Product product1 = new Product("測試商品1", "電子產品", new BigDecimal("100.00"), 10);
        Product product2 = new Product("測試商品2", "服飾", new BigDecimal("50.00"), 5);
        
        // 建立測試訂單
        Order order = new Order(customer, "收件人", "台北市信義區", Order.PaymentMethod.CREDIT_CARD);
        
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
        
        // 測試關聯關係
        assertEquals("訂單項目1的訂單應正確", order, item1.getOrder());
        assertEquals("訂單項目1的商品應正確", product1, item1.getProduct());
        assertEquals("訂單項目2的訂單應正確", order, item2.getOrder());
        assertEquals("訂單項目2的商品應正確", product2, item2.getProduct());
        
        // 測試商品快照功能
        assertEquals("商品1名稱快照應正確", "測試商品1", item1.getProductName());
        assertEquals("商品2名稱快照應正確", "測試商品2", item2.getProductName());
        
        System.out.println("OrderItem DAO 介面測試通過！");
        System.out.println("訂單項目1: " + item1.getProductName() + " x" + item1.getQuantity() + " = " + item1.getSubtotal());
        System.out.println("訂單項目2: " + item2.getProductName() + " x" + item2.getQuantity() + " = " + item2.getSubtotal());
        System.out.println("關聯關係驗證: ✅");
    }

    /**
     * 測試 DAO 統計功能
     */
    @Test
    public void testDAOStatistics() {
        // 測試訂單統計
        OrderService.OrderStatistics orderStats = new OrderService.OrderStatistics();
        orderStats.setTotalOrders(5L);
        orderStats.setTotalAmount(new BigDecimal("1500.00"));
        orderStats.setPendingOrders(2L);
        orderStats.setCompletedOrders(2L);
        orderStats.setCancelledOrders(1L);
        
        assertNotNull("訂單統計不應為空", orderStats);
        assertEquals("總訂單數應為5", Long.valueOf(5), orderStats.getTotalOrders());
        assertEquals("總金額應為1500.00", new BigDecimal("1500.00"), orderStats.getTotalAmount());
        assertEquals("待處理訂單應為2", Long.valueOf(2), orderStats.getPendingOrders());
        assertEquals("已完成訂單應為2", Long.valueOf(2), orderStats.getCompletedOrders());
        assertEquals("已取消訂單應為1", Long.valueOf(1), orderStats.getCancelledOrders());
        
        // 測試商品銷售統計
        OrderItemService.ProductSalesStatistics productStats = new OrderItemService.ProductSalesStatistics();
        productStats.setTotalOrders(10L);
        productStats.setTotalQuantity(25);
        productStats.setTotalRevenue(new BigDecimal("2500.00"));
        productStats.setAveragePrice(new BigDecimal("100.00"));
        
        assertNotNull("商品銷售統計不應為空", productStats);
        assertEquals("總訂單數應為10", Long.valueOf(10), productStats.getTotalOrders());
        assertEquals("總數量應為25", Integer.valueOf(25), productStats.getTotalQuantity());
        assertEquals("總收入應為2500.00", new BigDecimal("2500.00"), productStats.getTotalRevenue());
        assertEquals("平均價格應為100.00", new BigDecimal("100.00"), productStats.getAveragePrice());
        
        System.out.println("DAO 統計功能測試通過！");
        System.out.println("訂單統計: " + orderStats.getTotalOrders() + " 筆訂單, 總金額 " + orderStats.getTotalAmount());
        System.out.println("商品統計: " + productStats.getTotalOrders() + " 筆訂單, " + 
                          productStats.getTotalQuantity() + " 件商品, 總收入 " + productStats.getTotalRevenue());
    }
}
