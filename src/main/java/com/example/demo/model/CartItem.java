package com.example.demo.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
     * 商品單價（加入購物車時的價格，用於快照）
     */
    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    /**
     * 小計金額（數量 × 單價）
     */
    @Column(name = "subtotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    /**
     * 商品名稱快照（加入購物車時的商品名稱）
     */
    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    /**
     * 商品圖片URL快照（加入購物車時的商品圖片）
     */
    @Column(name = "product_image_url", length = 500)
    private String productImageUrl;

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
        this.unitPrice = product.getPrice();
        this.productName = product.getName();
        this.productImageUrl = product.getImageUrl();
        calculateSubtotal();
    }

    /**
     * 計算小計金額
     */
    public void calculateSubtotal() {
        if (quantity != null && unitPrice != null) {
            this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }

    /**
     * 更新數量並重新計算小計
     * @param newQuantity 新數量
     */
    public void updateQuantity(Integer newQuantity) {
        this.quantity = newQuantity;
        calculateSubtotal();
    }

    /**
     * 更新單價並重新計算小計
     * @param newUnitPrice 新單價
     */
    public void updateUnitPrice(BigDecimal newUnitPrice) {
        this.unitPrice = newUnitPrice;
        calculateSubtotal();
    }

    /**
     * 增加數量
     * @param increment 增加的數量
     */
    public void incrementQuantity(Integer increment) {
        this.quantity += increment;
        calculateSubtotal();
    }

    /**
     * 減少數量
     * @param decrement 減少的數量
     */
    public void decrementQuantity(Integer decrement) {
        if (this.quantity >= decrement) {
            this.quantity -= decrement;
            calculateSubtotal();
        }
    }

    /**
     * 同步商品資訊（當商品價格或名稱變動時）
     */
    public void syncProductInfo() {
        if (product != null) {
            this.unitPrice = product.getPrice();
            this.productName = product.getName();
            this.productImageUrl = product.getImageUrl();
            calculateSubtotal();
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
        if (product != null) {
            this.unitPrice = product.getPrice();
            this.productName = product.getName();
            this.productImageUrl = product.getImageUrl();
            calculateSubtotal();
        }
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateSubtotal();
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        calculateSubtotal();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + subtotal +
                '}';
    }
}
