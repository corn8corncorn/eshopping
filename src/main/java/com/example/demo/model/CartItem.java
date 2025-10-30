package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

/**
 * 購物車項目實體類
 * 代表購物車中的單一商品項目，包含商品資訊、數量、價格等
 */
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    /**
     * 所屬購物車 - 多對一關聯
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    /**
     * 商品資訊 - 多對一關聯
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * 商品數量
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * 加入購物車時間
     */
    @CreationTimestamp
    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;

    /**
     * 預設建構子
     */
    public CartItem() {}

    /**
     * 建構子
     * @param cart 所屬購物車
     * @param product 商品
     * @param quantity 數量
     */
    public CartItem(Cart cart, Product product, Integer quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * 動態取得商品單價
     */
    public BigDecimal getUnitPrice() {
        return product != null ? product.getPrice() : BigDecimal.ZERO;
    }

    /**
     * 動態計算小計金額
     */
    public BigDecimal getSubtotal() {
        if (quantity != null && product != null) {
            return product.getPrice().multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
    }

    /**
     * 動態取得商品名稱
     */
    public String getProductName() {
        return product != null ? product.getName() : "";
    }

    /**
     * 動態取得商品圖片URL
     */
    public String getProductImageUrl() {
        return product != null ? product.getImageUrl() : "";
    }

    /**
     * 更新數量
     * @param newQuantity 新數量
     */
    public void updateQuantity(Integer newQuantity) {
        this.quantity = newQuantity;
    }

    /**
     * 增加數量
     * @param increment 增加的數量
     */
    public void incrementQuantity(Integer increment) {
        this.quantity += increment;
    }

    /**
     * 減少數量
     * @param decrement 減少的數量
     */
    public void decrementQuantity(Integer decrement) {
        if (this.quantity >= decrement) {
            this.quantity -= decrement;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    @PrePersist
    protected void onCreate() {
        if (this.addedAt == null) {
            this.addedAt = LocalDateTime.now();
        }
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", productName='" + getProductName() + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + getUnitPrice() +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}
