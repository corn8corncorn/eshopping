package com.example.demo.service;

import com.example.demo.model.Order;
import com.example.demo.model.OrderAddress;

/**
 * 訂單地址服務介面
 * 定義訂單地址相關的業務邏輯
 */
public interface OrderAddressService {

    /**
     * 建立訂單地址
     * @param order 訂單
     * @param recipientName 收件人姓名
     * @param phone 收件人電話
     * @param streetAddress 街道地址
     * @param country 國家
     * @param city 城市
     * @param district 區/鄉鎮
     * @param postCode 郵遞區號
     * @return 新建立的訂單地址
     */
    OrderAddress createOrderAddress(Order order, String recipientName, String phone, String streetAddress,
                                   String country, String city, String district, String postCode);

    /**
     * 儲存訂單地址
     * @param orderAddress 要儲存的訂單地址
     * @return 儲存後的訂單地址
     */
    OrderAddress saveOrderAddress(OrderAddress orderAddress);

    /**
     * 根據ID查找訂單地址
     * @param id 訂單地址ID
     * @return 訂單地址物件，如果不存在則返回null
     */
    OrderAddress getById(Long id);

    /**
     * 根據訂單查找訂單地址
     * @param order 訂單
     * @return 訂單地址物件，如果不存在則返回null
     */
    OrderAddress getByOrder(Order order);
}

