package com.example.demo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.OrderAddressDAO;
import com.example.demo.model.Order;
import com.example.demo.model.OrderAddress;
import com.example.demo.service.OrderAddressService;

/**
 * 訂單地址服務實作類別
 * 負責處理訂單地址相關的業務邏輯
 */
@Service
@Transactional
public class OrderAddressServiceImpl implements OrderAddressService {

    private static final Logger logger = LoggerFactory.getLogger(OrderAddressServiceImpl.class);

    @Autowired
    private OrderAddressDAO orderAddressDAO;

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
    @Override
    public OrderAddress createOrderAddress(Order order, String recipientName, String phone, String streetAddress,
                                          String country, String city, String district, String postCode) {
        logger.info("建立訂單地址 - orderId: {}, recipientName: {}", 
                   order.getId(), recipientName);
        
        OrderAddress orderAddress = new OrderAddress(order, recipientName, phone, streetAddress);
        orderAddress.setCountry(country);
        orderAddress.setCity(city);
        orderAddress.setDistrict(district);
        orderAddress.setPostCode(postCode);
        
        OrderAddress savedOrderAddress = orderAddressDAO.save(orderAddress);
        
        logger.info("訂單地址建立成功 - orderAddressId: {}", savedOrderAddress.getId());
        return savedOrderAddress;
    }

    /**
     * 儲存訂單地址
     * @param orderAddress 要儲存的訂單地址
     * @return 儲存後的訂單地址
     */
    @Override
    public OrderAddress saveOrderAddress(OrderAddress orderAddress) {
        logger.info("儲存訂單地址 - orderAddressId: {}", orderAddress.getId());
        OrderAddress savedOrderAddress = orderAddressDAO.save(orderAddress);
        logger.info("訂單地址儲存成功 - orderAddressId: {}", savedOrderAddress.getId());
        return savedOrderAddress;
    }

    /**
     * 根據ID查找訂單地址
     * @param id 訂單地址ID
     * @return 訂單地址物件，如果不存在則返回null
     */
    @Override
    @Transactional(readOnly = true)
    public OrderAddress getById(Long id) {
        logger.debug("查找訂單地址 - orderAddressId: {}", id);
        OrderAddress orderAddress = orderAddressDAO.findById(id);
        logger.debug("訂單地址查找結果 - orderAddressId: {}, found: {}", id, orderAddress != null);
        return orderAddress;
    }

    /**
     * 根據訂單查找訂單地址
     * @param order 訂單
     * @return 訂單地址物件，如果不存在則返回null
     */
    @Override
    @Transactional(readOnly = true)
    public OrderAddress getByOrder(Order order) {
        logger.debug("根據訂單查找訂單地址 - orderId: {}", order.getId());
        OrderAddress orderAddress = orderAddressDAO.findByOrder(order);
        logger.debug("訂單地址查找結果 - orderId: {}, found: {}", order.getId(), orderAddress != null);
        return orderAddress;
    }
}

