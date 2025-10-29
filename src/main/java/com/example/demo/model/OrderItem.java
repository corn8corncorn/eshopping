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
 * 訂單項目實體類
 * 代表訂單中的單一商品項目，包含商品資訊、數量、價格等
 */
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    /**
     * 所屬訂單 - 多對一關聯
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

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
     * 商品單價（下單時的價格，避免商品價格變動影響歷史訂單）
     */
    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    /**
     * 小計金額（數量 × 單價）
     */
    @Column(name = "subtotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    /**
     * 商品名稱快照（下單時的商品名稱）
     */
    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    /**
     * 商品描述快照（下單時的商品描述）
     */
    @Column(name = "product_description", columnDefinition = "TEXT")
    private String productDescription;

    /**
     * 商品圖片URL快照（下單時的商品圖片）
     */
    @Column(name = "product_image_url", length = 500)
    private String productImageUrl;

    /**
     * 預設建構子
     */
    public OrderItem() {}

    /**
     * 建構子
     * @param order 所屬訂單
     * @param product 商品
     * @param quantity 數量
     */
    public OrderItem(Order order, Product product, Integer quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
        this.productName = product.getName();
        this.productDescription = product.getDescription();
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

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            this.unitPrice = product.getPrice();
            this.productName = product.getName();
            this.productDescription = product.getDescription();
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

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + subtotal +
                '}';
    }
}
