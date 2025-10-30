package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * 購物車實體類
 * 代表一個客戶的購物車，包含購物車項目、總金額等資訊
 */
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    /**
     * 客戶資訊 - 多對一關聯
     * 一個客戶只有一個購物車（或透過 session 管理）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /**
     * 購物車建立時間
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 購物車更新時間
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 購物車項目列表 - 一對多關聯
     */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    /**
     * 預設建構子
     */
    public Cart() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 建構子
     * @param customer 客戶
     */
    public Cart(Customer customer) {
        this();
        this.customer = customer;
    }

    /**
     * 動態計算購物車總金額
     */
    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    /**
     * 動態計算購物車總商品數量
     */
    public Integer getTotalItems() {
        int itemCount = 0;
        for (CartItem item : cartItems) {
            itemCount += item.getQuantity();
        }
        return itemCount;
    }

    /**
     * 添加購物車項目
     * @param cartItem 購物車項目
     */
    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setCart(this);
    }

    /**
     * 移除購物車項目
     * @param cartItem 購物車項目
     */
    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        cartItem.setCart(null);
    }

    /**
     * 清空購物車
     */
    public void clearCart() {
        cartItems.clear();
    }

    /**
     * 檢查購物車是否為空
     * @return 如果購物車為空返回true，否則返回false
     */
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    /**
     * 取得購物車項目數量
     * @return 購物車項目數量（不同商品的種類數）
     */
    public int getItemCount() {
        return cartItems.size();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", totalAmount=" + getTotalAmount() +
                ", totalItems=" + getTotalItems() +
                ", itemCount=" + getItemCount() +
                ", createdAt=" + createdAt +
                '}';
    }
}
