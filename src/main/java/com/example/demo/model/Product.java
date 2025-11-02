package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "product")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id")
	private Long id;

	@Column(name = "name", nullable = false, length = 50)
	private String name;

	@Column(name = "type", nullable = false, length = 100)
	private String type;

	@Column(name = "price", precision = 10, scale = 2)
	@NotNull(message = "價格不能為空")
	@DecimalMin(value = "0.00", message = "價格不能為負數，最小值為 0 元")
	private BigDecimal price;

	/**
	 * 商品庫存數量
	 */
	@Column(name = "stock_quantity", nullable = false)
	private Integer stockQuantity = 0;

	/**
	 * 最小庫存警告數量
	 */
	@Column(name = "min_stock_threshold")
	private Integer minStockThreshold = 10;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "image_url", length = 500)
	private String imageUrl;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private ProductStatus status = ProductStatus.ACTIVE;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	// Constructors
	public Product() {
	}

	public Product(String name, String type, BigDecimal price) {
		super();
		this.name = name;
		this.type = type;
		this.price = price;
	}

	/**
	 * 建構子（包含庫存）
	 * @param name 商品名稱
	 * @param type 商品類型
	 * @param price 商品價格
	 * @param stockQuantity 庫存數量
	 */
	public Product(String name, String type, BigDecimal price, Integer stockQuantity) {
		this(name, type, price);
		this.stockQuantity = stockQuantity;
	}

	// 枚舉類(商品狀態)
	public enum ProductStatus {
		ACTIVE("上架中"), INACTIVE("已下架"), OUT_OF_STOCK("缺貨中");

		private final String description;

		ProductStatus(String description) {
			this.description = description;
		}

		public String getDescription() {
			return description;
		}
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
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

	public ProductStatus getStatus() {
		return status;
	}

	public void setStatus(ProductStatus status) {
		this.status = status;
	}

	public Integer getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
		// 自動更新商品狀態
		updateStatusBasedOnStock();
	}

	public Integer getMinStockThreshold() {
		return minStockThreshold;
	}

	public void setMinStockThreshold(Integer minStockThreshold) {
		this.minStockThreshold = minStockThreshold;
	}

	/**
	 * 增加庫存
	 * @param quantity 增加的數量
	 */
	public void addStock(Integer quantity) {
		if (quantity > 0) {
			this.stockQuantity += quantity;
			updateStatusBasedOnStock();
		}
	}

	/**
	 * 減少庫存
	 * @param quantity 減少的數量
	 * @return 是否成功減少庫存
	 */
	public boolean reduceStock(Integer quantity) {
		if (quantity > 0 && this.stockQuantity >= quantity) {
			this.stockQuantity -= quantity;
			updateStatusBasedOnStock();
			return true;
		}
		return false;
	}

	/**
	 * 檢查是否有足夠庫存
	 * @param quantity 需要的數量
	 * @return 是否有足夠庫存
	 */
	public boolean hasEnoughStock(Integer quantity) {
		return this.stockQuantity >= quantity;
	}

	/**
	 * 檢查庫存是否低於警告閾值
	 * @return 是否庫存不足
	 */
	public boolean isLowStock() {
		return this.stockQuantity <= this.minStockThreshold;
	}

	/**
	 * 根據庫存數量自動更新商品狀態
	 */
	private void updateStatusBasedOnStock() {
		if (this.stockQuantity <= 0) {
			this.status = ProductStatus.OUT_OF_STOCK;
		} else if (this.status == ProductStatus.OUT_OF_STOCK) {
			this.status = ProductStatus.ACTIVE;
		}
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", description=" + description + ", imageUrl=" + imageUrl
				+ ", type=" + type + ", price=" + price + ", stockQuantity=" + stockQuantity + ", status=" + status 
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}

}
