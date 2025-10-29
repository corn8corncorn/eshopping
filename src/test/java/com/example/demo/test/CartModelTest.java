package com.example.demo.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Customer;
import com.example.demo.model.Product;
import com.example.demo.model.User;

/**
 * Cart 模型測試類
 * 測試購物車相關的業務邏輯
 */
public class CartModelTest {

    /**
     * 測試 Cart 模型的基本功能
     */
    @Test
    public void testCartModel() {
        // 建立測試用戶
        User user = new User("testuser", "test@example.com", "password123");
        user.setId(1L);
        
        // 建立測試客戶
        Customer customer = new Customer(user, "測試客戶");
        customer.setId(1L);
        
        // 建立測試商品
        Product product1 = new Product("測試商品1", "電子產品", new BigDecimal("100.00"), 10);
        Product product2 = new Product("測試商品2", "服飾", new BigDecimal("50.00"), 5);
        
        // 建立購物車
        Cart cart = new Cart(customer);
        
        // 驗證購物車基本屬性
        assertNotNull("購物車不應為空", cart);
        assertEquals("購物車總金額應為0", BigDecimal.ZERO, cart.getTotalAmount());
        assertEquals("購物車總商品數量應為0", Integer.valueOf(0), cart.getTotalItems());
        assertTrue("購物車應為空", cart.isEmpty());
        assertEquals("購物車項目數量應為0", 0, cart.getItemCount());
        
        // 添加購物車項目
        CartItem item1 = new CartItem(cart, product1, 2);
        CartItem item2 = new CartItem(cart, product2, 1);
        
        cart.addCartItem(item1);
        cart.addCartItem(item2);
        
        // 驗證購物車項目
        assertEquals("購物車應有2個項目", 2, cart.getItemCount());
        assertEquals("購物車總金額應為250.00", new BigDecimal("250.00"), cart.getTotalAmount());
        assertEquals("購物車總商品數量應為3", Integer.valueOf(3), cart.getTotalItems());
        assertFalse("購物車不應為空", cart.isEmpty());
        
        // 測試移除項目
        cart.removeCartItem(item1);
        assertEquals("購物車應有1個項目", 1, cart.getItemCount());
        assertEquals("購物車總金額應為50.00", new BigDecimal("50.00"), cart.getTotalAmount());
        assertEquals("購物車總商品數量應為1", Integer.valueOf(1), cart.getTotalItems());
        
        // 測試清空購物車
        cart.clearCart();
        assertTrue("購物車應為空", cart.isEmpty());
        assertEquals("購物車總金額應為0", BigDecimal.ZERO, cart.getTotalAmount());
        
        System.out.println("Cart 模型測試通過！");
        System.out.println("購物車客戶: " + cart.getCustomer().getFullName());
        System.out.println("購物車總金額: " + cart.getTotalAmount());
        System.out.println("購物車項目數量: " + cart.getItemCount());
    }

    /**
     * 測試 CartItem 模型的基本功能
     */
    @Test
    public void testCartItemModel() {
        // 建立測試用戶和客戶
        User user = new User("testuser", "test@example.com", "password123");
        user.setId(1L);
        Customer customer = new Customer(user, "測試客戶");
        customer.setId(1L);
        
        // 建立測試商品
        Product product1 = new Product("測試商品1", "電子產品", new BigDecimal("100.00"), 10);
        
        // 建立測試購物車
        Cart cart = new Cart(customer);
        
        // 測試購物車項目建立
        CartItem item1 = new CartItem(cart, product1, 2);
        
        // 驗證購物車項目基本屬性
        assertNotNull("購物車項目1不應為空", item1);
        
        assertEquals("商品1數量應為2", Integer.valueOf(2), item1.getQuantity());
        assertEquals("商品1單價應為100.00", new BigDecimal("100.00"), item1.getUnitPrice());
        assertEquals("商品1小計應為200.00", new BigDecimal("200.00"), item1.getSubtotal());
        
        // 測試數量更新
        item1.updateQuantity(3);
        assertEquals("商品1數量應為3", Integer.valueOf(3), item1.getQuantity());
        assertEquals("商品1小計應為300.00", new BigDecimal("300.00"), item1.getSubtotal());
        
        // 測試增加數量
        item1.incrementQuantity(2);
        assertEquals("商品1數量應為5", Integer.valueOf(5), item1.getQuantity());
        assertEquals("商品1小計應為500.00", new BigDecimal("500.00"), item1.getSubtotal());
        
        // 測試減少數量
        item1.decrementQuantity(2);
        assertEquals("商品1數量應為3", Integer.valueOf(3), item1.getQuantity());
        assertEquals("商品1小計應為300.00", new BigDecimal("300.00"), item1.getSubtotal());
        
        // 測試單價更新
        item1.updateUnitPrice(new BigDecimal("120.00"));
        assertEquals("商品1單價應為120.00", new BigDecimal("120.00"), item1.getUnitPrice());
        assertEquals("商品1小計應為360.00", new BigDecimal("360.00"), item1.getSubtotal());
        
        // 測試商品資訊快照
        assertEquals("商品1名稱快照應正確", "測試商品1", item1.getProductName());
        
        // 測試同步商品資訊
        product1.setPrice(new BigDecimal("150.00"));
        product1.setName("更新後的商品名稱");
        item1.syncProductInfo();
        assertEquals("商品1單價應同步為150.00", new BigDecimal("150.00"), item1.getUnitPrice());
        assertEquals("商品1名稱應同步", "更新後的商品名稱", item1.getProductName());
        
        System.out.println("CartItem 模型測試通過！");
        System.out.println("購物車項目: " + item1.getProductName() + " x" + item1.getQuantity() + " = " + item1.getSubtotal());
        System.out.println("商品資訊同步: ✅");
    }

    /**
     * 測試購物車金額計算
     */
    @Test
    public void testCartAmountCalculation() {
        // 建立測試用戶和客戶
        User user = new User("testuser", "test@example.com", "password123");
        user.setId(1L);
        Customer customer = new Customer(user, "測試客戶");
        customer.setId(1L);
        
        // 建立測試商品
        Product product1 = new Product("測試商品1", "電子產品", new BigDecimal("100.00"), 10);
        Product product2 = new Product("測試商品2", "服飾", new BigDecimal("50.00"), 5);
        Product product3 = new Product("測試商品3", "書籍", new BigDecimal("30.00"), 8);
        
        // 建立測試購物車
        Cart cart = new Cart(customer);
        
        // 添加多個商品到購物車
        CartItem item1 = new CartItem(cart, product1, 2);  // 200.00
        CartItem item2 = new CartItem(cart, product2, 3);  // 150.00
        CartItem item3 = new CartItem(cart, product3, 1);   // 30.00
        
        cart.addCartItem(item1);
        cart.addCartItem(item2);
        cart.addCartItem(item3);
        
        // 驗證總金額計算
        assertEquals("購物車總金額應為380.00", new BigDecimal("380.00"), cart.getTotalAmount());
        assertEquals("購物車總商品數量應為6", Integer.valueOf(6), cart.getTotalItems());
        
        // 更新商品數量
        item1.updateQuantity(5);  // 500.00
        cart.calculateTotalAmount();
        assertEquals("更新後購物車總金額應為680.00", new BigDecimal("680.00"), cart.getTotalAmount());
        assertEquals("更新後購物車總商品數量應為9", Integer.valueOf(9), cart.getTotalItems());
        
        // 移除一個項目
        cart.removeCartItem(item2);
        assertEquals("移除後購物車總金額應為530.00", new BigDecimal("530.00"), cart.getTotalAmount());
        assertEquals("移除後購物車總商品數量應為6", Integer.valueOf(6), cart.getTotalItems());
        
        System.out.println("購物車金額計算測試通過！");
        System.out.println("最終購物車總金額: " + cart.getTotalAmount());
        System.out.println("最終購物車總商品數量: " + cart.getTotalItems());
    }
}
