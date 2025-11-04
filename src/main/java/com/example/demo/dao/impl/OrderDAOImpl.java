package com.example.demo.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.OrderDAO;
import com.example.demo.model.Customer;
import com.example.demo.model.Order;
import com.example.demo.model.Order.OrderStatus;
import com.example.demo.model.Order.PaymentStatus;

/**
 * 訂單資料存取層實作類別
 * 負責與資料庫進行訂單相關的資料操作
 */
@Repository
@Transactional
public class OrderDAOImpl implements OrderDAO {

    private static final Logger logger = LoggerFactory.getLogger(OrderDAOImpl.class);

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
     * 儲存訂單到資料庫
     * @param order 要儲存的訂單物件
     * @return 儲存後的訂單物件（包含自動生成的 ID）
     */
    @Override
    public Order save(Order order) {
        logger.info("開始儲存訂單到資料庫 - orderNumber: {}", order.getOrderNumber());
        getCurrentSession().save(order);
        logger.info("訂單儲存成功 - orderId: {}, orderNumber: {}", order.getId(), order.getOrderNumber());
        return order;
    }

    /**
     * 根據ID查找訂單
     * @param id 訂單ID
     * @return 訂單物件，如果不存在則返回null
     */
    @Override
    @Transactional(readOnly = true)
    public Order findById(Long id) {
        logger.info("根據ID查找訂單 - orderId: {}", id);
        // 使用 JOIN FETCH 預加載關聯數據，確保在 session 內完成所有數據加載
        Query<Order> query = getCurrentSession().createQuery(
                "SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.customer LEFT JOIN FETCH o.orderAddress WHERE o.id = :id", Order.class);
        query.setParameter("id", id);
        Order order = query.uniqueResult();
        logger.debug("訂單查詢結果 - orderId: {}, found: {}, orderNumber: {}", 
                    id, order != null, order != null ? order.getOrderNumber() : "N/A");
        return order;
    }

    /**
     * 查找所有訂單
     * @return 所有訂單的列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        logger.info("查找所有訂單");
        Query<Order> query = getCurrentSession().createQuery("FROM Order ORDER BY createdAt DESC", Order.class);
        List<Order> orders = query.getResultList();
        logger.debug("查詢到訂單數量: {}", orders.size());
        return orders;
    }

    /**
     * 根據訂單編號查找訂單
     * @param orderNumber 訂單編號
     * @return 訂單物件，如果不存在則返回null
     */
    @Override
    @Transactional(readOnly = true)
    public Order findByOrderNumber(String orderNumber) {
        logger.info("根據訂單編號查找訂單 - orderNumber: {}", orderNumber);
        Query<Order> query = getCurrentSession().createQuery(
                "FROM Order WHERE orderNumber = :orderNumber", Order.class);
        query.setParameter("orderNumber", orderNumber);
        Order order = query.uniqueResult();
        logger.debug("訂單查詢結果 - orderNumber: {}, found: {}", orderNumber, order != null);
        return order;
    }

    /**
     * 根據客戶查找訂單
     * @param customer 客戶
     * @return 該客戶的所有訂單列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<Order> findByCustomer(Customer customer) {
        logger.info("根據客戶查找訂單 - customerId: {}", customer.getId());
        // 使用 customer.id 來查詢，避免對象比較問題，並使用 JOIN FETCH 預加載關聯數據
        Query<Order> query = getCurrentSession().createQuery(
                "SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.customer WHERE o.customer.id = :customerId ORDER BY o.createdAt DESC", Order.class);
        query.setParameter("customerId", customer.getId());
        List<Order> orders = query.getResultList();
        logger.info("客戶訂單查詢結果 - customerId: {}, orderCount: {}", customer.getId(), orders.size());
        // 調試：記錄查詢到的訂單編號
        if (!orders.isEmpty()) {
            for (Order order : orders) {
                logger.debug("查詢到的訂單 - orderId: {}, orderNumber: {}", order.getId(), order.getOrderNumber());
            }
        }
        return orders;
    }

    /**
     * 根據客戶ID查找訂單
     * @param customerId 客戶ID
     * @return 該客戶的所有訂單列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<Order> findByCustomerId(Long customerId) {
        logger.info("根據客戶ID查找訂單 - customerId: {}", customerId);
        Query<Order> query = getCurrentSession().createQuery(
                "FROM Order WHERE customer.id = :customerId ORDER BY createdAt DESC", Order.class);
        query.setParameter("customerId", customerId);
        List<Order> orders = query.getResultList();
        logger.debug("客戶訂單查詢結果 - customerId: {}, orderCount: {}", customerId, orders.size());
        return orders;
    }

    /**
     * 根據訂單狀態查找訂單
     * @param status 訂單狀態
     * @return 指定狀態的訂單列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<Order> findByStatus(OrderStatus status) {
        logger.info("根據訂單狀態查找訂單 - status: {}", status);
        Query<Order> query = getCurrentSession().createQuery(
                "FROM Order WHERE status = :status ORDER BY createdAt DESC", Order.class);
        query.setParameter("status", status);
        List<Order> orders = query.getResultList();
        logger.debug("狀態訂單查詢結果 - status: {}, orderCount: {}", status, orders.size());
        return orders;
    }

    /**
     * 根據付款狀態查找訂單
     * @param paymentStatus 付款狀態
     * @return 指定付款狀態的訂單列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<Order> findByPaymentStatus(PaymentStatus paymentStatus) {
        logger.info("根據付款狀態查找訂單 - paymentStatus: {}", paymentStatus);
        Query<Order> query = getCurrentSession().createQuery(
                "FROM Order WHERE paymentStatus = :paymentStatus ORDER BY createdAt DESC", Order.class);
        query.setParameter("paymentStatus", paymentStatus);
        List<Order> orders = query.getResultList();
        logger.debug("付款狀態訂單查詢結果 - paymentStatus: {}, orderCount: {}", paymentStatus, orders.size());
        return orders;
    }

    /**
     * 刪除訂單
     * @param id 要刪除的訂單ID
     */
    @Override
    public void delete(Long id) {
        logger.info("開始刪除訂單 - orderId: {}", id);
        Order order = getCurrentSession().get(Order.class, id);
        if (order != null) {
            getCurrentSession().delete(order);
            logger.info("訂單刪除成功 - orderId: {}", id);
        } else {
            logger.warn("要刪除的訂單不存在 - orderId: {}", id);
        }
    }

    /**
     * 檢查訂單是否存在
     * @param id 訂單ID
     * @return 如果存在返回true，否則返回false
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        logger.debug("檢查訂單是否存在 - orderId: {}", id);
        Query<Long> query = getCurrentSession().createQuery(
                "SELECT COUNT(*) FROM Order WHERE id = :id", Long.class);
        query.setParameter("id", id);
        Long count = query.uniqueResult();
        boolean exists = count > 0;
        logger.debug("訂單存在檢查結果 - orderId: {}, exists: {}", id, exists);
        return exists;
    }

    /**
     * 計算訂單總數
     * @return 訂單總數
     */
    @Override
    @Transactional(readOnly = true)
    public long count() {
        logger.debug("計算訂單總數");
        Query<Long> query = getCurrentSession().createQuery(
                "SELECT COUNT(*) FROM Order", Long.class);
        Long count = query.uniqueResult();
        logger.debug("訂單總數: {}", count);
        return count;
    }

    /**
     * 根據客戶ID計算訂單總數
     * @param customerId 客戶ID
     * @return 該客戶的訂單總數
     */
    @Override
    @Transactional(readOnly = true)
    public long countByCustomerId(Long customerId) {
        logger.debug("根據客戶ID計算訂單總數 - customerId: {}", customerId);
        Query<Long> query = getCurrentSession().createQuery(
                "SELECT COUNT(*) FROM Order WHERE customer.id = :customerId", Long.class);
        query.setParameter("customerId", customerId);
        Long count = query.uniqueResult();
        logger.debug("客戶訂單總數 - customerId: {}, count: {}", customerId, count);
        return count;
    }

    /**
     * 根據訂單狀態計算訂單總數
     * @param status 訂單狀態
     * @return 指定狀態的訂單總數
     */
    @Override
    @Transactional(readOnly = true)
    public long countByStatus(OrderStatus status) {
        logger.debug("根據訂單狀態計算訂單總數 - status: {}", status);
        Query<Long> query = getCurrentSession().createQuery(
                "SELECT COUNT(*) FROM Order WHERE status = :status", Long.class);
        query.setParameter("status", status);
        Long count = query.uniqueResult();
        logger.debug("狀態訂單總數 - status: {}, count: {}", status, count);
        return count;
    }

    /**
     * 根據付款狀態計算訂單總數
     * @param paymentStatus 付款狀態
     * @return 指定付款狀態的訂單總數
     */
    @Override
    @Transactional(readOnly = true)
    public long countByPaymentStatus(PaymentStatus paymentStatus) {
        logger.debug("根據付款狀態計算訂單總數 - paymentStatus: {}", paymentStatus);
        Query<Long> query = getCurrentSession().createQuery(
                "SELECT COUNT(*) FROM Order WHERE paymentStatus = :paymentStatus", Long.class);
        query.setParameter("paymentStatus", paymentStatus);
        Long count = query.uniqueResult();
        logger.debug("付款狀態訂單總數 - paymentStatus: {}, count: {}", paymentStatus, count);
        return count;
    }
}
