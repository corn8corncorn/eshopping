package com.example.demo.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * 訂單地址實體類
 * 代表一個訂單的收件地址資訊
 */
@Entity
@Table(name = "order_addresses")
public class OrderAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_address_id")
    private Long id;

    /**
     * 所屬訂單 - 一對一關聯
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    /**
     * 收件人姓名
     */
    @Column(name = "recipient_name", nullable = false, length = 100)
    private String recipientName;

    /**
     * 收件人電話
     */
    @Column(name = "phone", length = 20)
    private String phone;

    /**
     * 國家
     */
    @Column(name = "country", length = 50)
    private String country;

    /**
     * 城市
     */
    @Column(name = "city", length = 50)
    private String city;

    /**
     * 區/鄉鎮
     */
    @Column(name = "district", length = 50)
    private String district;

    /**
     * 街道地址
     */
    @Column(name = "street_address", columnDefinition = "TEXT")
    private String streetAddress;

    /**
     * 郵遞區號
     */
    @Column(name = "post_code", length = 10)
    private String postCode;

    /**
     * 建立時間
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新時間
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 預設建構子
     */
    public OrderAddress() {}

    /**
     * 建構子
     * @param order 所屬訂單
     * @param recipientName 收件人姓名
     * @param phone 收件人電話
     * @param streetAddress 街道地址
     */
    public OrderAddress(Order order, String recipientName, String phone, String streetAddress) {
        this.order = order;
        this.recipientName = recipientName;
        this.phone = phone;
        this.streetAddress = streetAddress;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
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

    /**
     * 取得完整地址字串
     */
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (country != null && !country.isEmpty()) sb.append(country);
        if (city != null && !city.isEmpty()) sb.append(city);
        if (district != null && !district.isEmpty()) sb.append(district);
        if (streetAddress != null && !streetAddress.isEmpty()) sb.append(streetAddress);
        if (postCode != null && !postCode.isEmpty()) sb.append(" (").append(postCode).append(")");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "OrderAddress{" +
                "id=" + id +
                ", recipientName='" + recipientName + '\'' +
                ", phone='" + phone + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", streetAddress='" + streetAddress + '\'' +
                '}';
    }
}

