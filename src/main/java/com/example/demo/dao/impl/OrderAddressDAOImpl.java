package com.example.demo.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.OrderAddressDAO;
import com.example.demo.model.Order;
import com.example.demo.model.OrderAddress;

/**
 * 訂單地址資料存取層實作類別
 * 負責與資料庫進行訂單地址相關的資料操作
 */
@Repository
@Transactional
public class OrderAddressDAOImpl implements OrderAddressDAO {

    private static final Logger logger = LoggerFactory.getLogger(OrderAddressDAOImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * 取得目前的 Hibernate Session
     * @return 目前的 Hibernate Session
     */
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 儲存訂單地址到資料庫
     * @param orderAddress 要儲存的訂單地址物件
     * @return 儲存後的訂單地址物件（包含自動生成的 ID）
     */
    @Override
    public OrderAddress save(OrderAddress orderAddress) {
        logger.info("開始儲存訂單地址到資料庫 - orderId: {}", 
                   orderAddress.getOrder() != null ? orderAddress.getOrder().getId() : "null");
        getCurrentSession().saveOrUpdate(orderAddress);
        logger.info("訂單地址儲存成功 - orderAddressId: {}, orderId: {}", 
                   orderAddress.getId(), orderAddress.getOrder().getId());
        return orderAddress;
    }

    /**
     * 根據ID查找訂單地址
     * @param id 訂單地址ID
     * @return 訂單地址物件，如果不存在則返回null
     */
    @Override
    @Transactional(readOnly = true)
    public OrderAddress findById(Long id) {
        logger.debug("從資料庫取得訂單地址 - orderAddressId: {}", id);
        OrderAddress orderAddress = getCurrentSession().get(OrderAddress.class, id);
        logger.debug("訂單地址查詢結果 - orderAddressId: {}, found: {}", id, orderAddress != null);
        return orderAddress;
    }

    /**
     * 根據訂單查找訂單地址
     * @param order 訂單
     * @return 訂單地址物件，如果不存在則返回null
     */
    @Override
    @Transactional(readOnly = true)
    public OrderAddress findByOrder(Order order) {
        logger.debug("根據訂單查找訂單地址 - orderId: {}", order.getId());
        
        Query<OrderAddress> query = getCurrentSession().createQuery(
                "FROM OrderAddress WHERE order = :order", OrderAddress.class);
        query.setParameter("order", order);
        OrderAddress orderAddress = query.uniqueResult();
        
        logger.debug("訂單地址查詢結果 - orderId: {}, found: {}", order.getId(), orderAddress != null);
        return orderAddress;
    }

    /**
     * 根據訂單ID查找訂單地址
     * @param orderId 訂單ID
     * @return 訂單地址物件，如果不存在則返回null
     */
    @Override
    @Transactional(readOnly = true)
    public OrderAddress findByOrderId(Long orderId) {
        logger.debug("根據訂單ID查找訂單地址 - orderId: {}", orderId);
        
        Query<OrderAddress> query = getCurrentSession().createQuery(
                "FROM OrderAddress WHERE order.id = :orderId", OrderAddress.class);
        query.setParameter("orderId", orderId);
        OrderAddress orderAddress = query.uniqueResult();
        
        logger.debug("訂單地址查詢結果 - orderId: {}, found: {}", orderId, orderAddress != null);
        return orderAddress;
    }

    /**
     * 刪除訂單地址
     * @param id 要刪除的訂單地址ID
     */
    @Override
    public void delete(Long id) {
        logger.info("開始刪除訂單地址 - orderAddressId: {}", id);
        
        OrderAddress orderAddress = getCurrentSession().get(OrderAddress.class, id);
        if (orderAddress != null) {
            getCurrentSession().delete(orderAddress);
            logger.info("訂單地址刪除成功 - orderAddressId: {}", id);
        } else {
            logger.warn("要刪除的訂單地址不存在 - orderAddressId: {}", id);
        }
    }
}

