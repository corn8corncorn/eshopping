package com.example.demo.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

import com.example.demo.model.Customer;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;
import com.example.demo.model.User;

/**
 * Order 模型測試類
 * 測試訂單相關的業務邏輯
 */
public class OrderModelTest {

    /**
     * 測試 Order 模型的基本功能
     */
    @Test
    public void testOrderModel() {
        // 建立測試用戶
        User user = new User("testuser", "test@example.com", "password123");
        user.setId(1L);
        
        // 建立測試客戶
        Customer customer = new Customer(user, "測試客戶");
        customer.setId(1L);
        
        // 建立測試商品
        Product product1 = new Product("測試商品1", "電子產品", new BigDecimal("100.00"), 10);
        Product product2 = new Product("測試商品2", "服飾", new BigDecimal("50.00"), 5);
        
        // 建立訂單
        Order order = new Order(customer, "收件人", "台北市信義區", Order.PaymentMethod.CREDIT_CARD);
        
        // 驗證訂單基本屬性
        assertNotNull("訂單編號不應為空", order.getOrderNumber());
        assertEquals("訂單狀態應為待處理", Order.OrderStatus.PENDING, order.getStatus());
        assertEquals("付款狀態應為待付款", Order.PaymentStatus.PENDING, order.getPaymentStatus());
        assertEquals("付款方式應為信用卡", Order.PaymentMethod.CREDIT_CARD, order.getPaymentMethod());
        
        // 添加訂單項目
        OrderItem item1 = new OrderItem(order, product1, 2);
        OrderItem item2 = new OrderItem(order, product2, 1);
        
        order.addOrderItem(item1);
        order.addOrderItem(item2);
        
        // 驗證訂單項目
        assertEquals("訂單應有2個項目", 2, order.getOrderItems().size());
        assertEquals("總金額應為250.00", new BigDecimal("250.00"), order.getTotalAmount());
        assertEquals("最終金額應為250.00", new BigDecimal("250.00"), order.getFinalAmount());
        
        // 測試運費計算
        order.setShippingFee(new BigDecimal("30.00"));
        assertEquals("含運費後最終金額應為280.00", new BigDecimal("280.00"), order.getFinalAmount());
        
        // 測試折扣計算
        order.setDiscountAmount(new BigDecimal("20.00"));
        assertEquals("含折扣後最終金額應為260.00", new BigDecimal("260.00"), order.getFinalAmount());
        
        // 測試庫存減少
        assertTrue("商品1庫存減少應成功", product1.reduceStock(2));
        assertEquals("商品1庫存應為8", Integer.valueOf(8), product1.getStockQuantity());
        
        assertTrue("商品2庫存減少應成功", product2.reduceStock(1));
        assertEquals("商品2庫存應為4", Integer.valueOf(4), product2.getStockQuantity());
        
        // 測試庫存不足的情況
        assertFalse("商品2庫存不足時應失敗", product2.reduceStock(10));
        assertEquals("商品2庫存應仍為4", Integer.valueOf(4), product2.getStockQuantity());
        
        // 測試庫存警告
        product2.setMinStockThreshold(5);
        assertTrue("商品2庫存低於警告閾值", product2.isLowStock());
        
        product2.setMinStockThreshold(3);
        assertFalse("商品2庫存不低於警告閾值", product2.isLowStock());
        
        System.out.println("Order 模型測試通過！");
        System.out.println("訂單編號: " + order.getOrderNumber());
        System.out.println("訂單總金額: " + order.getTotalAmount());
        System.out.println("訂單最終金額: " + order.getFinalAmount());
        System.out.println("訂單項目數量: " + order.getOrderItems().size());
    }
}