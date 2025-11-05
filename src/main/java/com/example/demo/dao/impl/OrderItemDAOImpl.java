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

import com.example.demo.dao.OrderItemDAO;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;

/**
 * 訂單項目資料存取層實作類別
 * 負責與資料庫進行訂單項目相關的資料操作
 */
@Repository
@Transactional
public class OrderItemDAOImpl implements OrderItemDAO {

    private static final Logger logger = LoggerFactory.getLogger(OrderItemDAOImpl.class);

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
     * 儲存訂單項目到資料庫
     * @param orderItem 要儲存的訂單項目物件
     * @return 儲存後的訂單項目物件（包含自動生成的 ID）
     */
    @Override
    public OrderItem save(OrderItem orderItem) {
        logger.info("開始儲存訂單項目到資料庫 - orderId: {}, productId: {}", 
                   orderItem.getOrder() != null ? orderItem.getOrder().getId() : "null",
                   orderItem.getProduct() != null ? orderItem.getProduct().getId() : "null");
        getCurrentSession().save(orderItem);
        // 立即 flush 以檢查約束錯誤
        getCurrentSession().flush();
        logger.info("訂單項目儲存成功 - orderItemId: {}, orderId: {}, productId: {}", 
                   orderItem.getId(), 
                   orderItem.getOrder() != null ? orderItem.getOrder().getId() : "null",
                   orderItem.getProduct() != null ? orderItem.getProduct().getId() : "null");
        return orderItem;
    }

    /**
     * 批量儲存訂單項目到資料庫
     * @param orderItems 要儲存的訂單項目列表
     * @return 儲存後的訂單項目列表
     */
    @Override
    public List<OrderItem> saveAll(List<OrderItem> orderItems) {
        logger.info("開始批量儲存訂單項目到資料庫 - count: {}", orderItems.size());
        for (OrderItem orderItem : orderItems) {
            getCurrentSession().save(orderItem);
        }
        logger.info("批量儲存訂單項目成功 - count: {}", orderItems.size());
        return orderItems;
    }

    /**
     * 根據ID查找訂單項目
     * @param id 訂單項目ID
     * @return 訂單項目物件，如果不存在則返回null
     */
    @Override
    @Transactional(readOnly = true)
    public OrderItem findById(Long id) {
        logger.info("根據ID查找訂單項目 - orderItemId: {}", id);
        OrderItem orderItem = getCurrentSession().get(OrderItem.class, id);
        logger.debug("訂單項目查詢結果 - orderItemId: {}, found: {}", id, orderItem != null);
        return orderItem;
    }

    /**
     * 查找所有訂單項目
     * @return 所有訂單項目的列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderItem> findAll() {
        logger.info("查找所有訂單項目");
        Query<OrderItem> query = getCurrentSession().createQuery("FROM OrderItem", OrderItem.class);
        List<OrderItem> orderItems = query.getResultList();
        logger.debug("查詢到訂單項目數量: {}", orderItems.size());
        return orderItems;
    }

    /**
     * 根據訂單查找訂單項目
     * @param order 訂單
     * @return 該訂單的所有項目列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderItem> findByOrder(Order order) {
        logger.info("根據訂單查找訂單項目 - orderId: {}", order.getId());
        Query<OrderItem> query = getCurrentSession().createQuery(
                "FROM OrderItem WHERE order = :order ORDER BY id", OrderItem.class);
        query.setParameter("order", order);
        List<OrderItem> orderItems = query.getResultList();
        logger.debug("訂單項目查詢結果 - orderId: {}, itemCount: {}", order.getId(), orderItems.size());
        return orderItems;
    }

    /**
     * 根據訂單ID查找訂單項目
     * @param orderId 訂單ID
     * @return 該訂單的所有項目列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderItem> findByOrderId(Long orderId) {
        logger.info("根據訂單ID查找訂單項目 - orderId: {}", orderId);
        Query<OrderItem> query = getCurrentSession().createQuery(
                "FROM OrderItem WHERE order.id = :orderId ORDER BY id", OrderItem.class);
        query.setParameter("orderId", orderId);
        List<OrderItem> orderItems = query.getResultList();
        logger.debug("訂單項目查詢結果 - orderId: {}, itemCount: {}", orderId, orderItems.size());
        return orderItems;
    }

    /**
     * 根據商品查找訂單項目
     * @param product 商品
     * @return 包含該商品的所有訂單項目列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderItem> findByProduct(Product product) {
        logger.info("根據商品查找訂單項目 - productId: {}", product.getId());
        Query<OrderItem> query = getCurrentSession().createQuery(
                "FROM OrderItem WHERE product = :product ORDER BY id", OrderItem.class);
        query.setParameter("product", product);
        List<OrderItem> orderItems = query.getResultList();
        logger.debug("商品訂單項目查詢結果 - productId: {}, itemCount: {}", product.getId(), orderItems.size());
        return orderItems;
    }

    /**
     * 根據商品ID查找訂單項目
     * @param productId 商品ID
     * @return 包含該商品的所有訂單項目列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderItem> findByProductId(Long productId) {
        logger.info("根據商品ID查找訂單項目 - productId: {}", productId);
        Query<OrderItem> query = getCurrentSession().createQuery(
                "FROM OrderItem WHERE product.id = :productId ORDER BY id", OrderItem.class);
        query.setParameter("productId", productId);
        List<OrderItem> orderItems = query.getResultList();
        logger.debug("商品訂單項目查詢結果 - productId: {}, itemCount: {}", productId, orderItems.size());
        return orderItems;
    }

    /**
     * 刪除訂單項目
     * @param id 要刪除的訂單項目ID
     */
    @Override
    public void delete(Long id) {
        logger.info("開始刪除訂單項目 - orderItemId: {}", id);
        OrderItem orderItem = getCurrentSession().get(OrderItem.class, id);
        if (orderItem != null) {
            getCurrentSession().delete(orderItem);
            logger.info("訂單項目刪除成功 - orderItemId: {}", id);
        } else {
            logger.warn("要刪除的訂單項目不存在 - orderItemId: {}", id);
        }
    }

    /**
     * 批量刪除訂單項目
     * @param ids 要刪除的訂單項目ID列表
     */
    @Override
    public void deleteAllById(List<Long> ids) {
        logger.info("開始批量刪除訂單項目 - count: {}", ids.size());
        Query<?> query = getCurrentSession().createQuery(
                "DELETE FROM OrderItem WHERE id IN :ids");
        query.setParameter("ids", ids);
        int deletedCount = query.executeUpdate();
        logger.info("批量刪除訂單項目成功 - deletedCount: {}", deletedCount);
    }

    /**
     * 檢查訂單項目是否存在
     * @param id 訂單項目ID
     * @return 如果存在返回true，否則返回false
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        logger.debug("檢查訂單項目是否存在 - orderItemId: {}", id);
        Query<Long> query = getCurrentSession().createQuery(
                "SELECT COUNT(*) FROM OrderItem WHERE id = :id", Long.class);
        query.setParameter("id", id);
        Long count = query.uniqueResult();
        boolean exists = count > 0;
        logger.debug("訂單項目存在檢查結果 - orderItemId: {}, exists: {}", id, exists);
        return exists;
    }

    /**
     * 計算訂單項目總數
     * @return 訂單項目總數
     */
    @Override
    @Transactional(readOnly = true)
    public long count() {
        logger.debug("計算訂單項目總數");
        Query<Long> query = getCurrentSession().createQuery(
                "SELECT COUNT(*) FROM OrderItem", Long.class);
        Long count = query.uniqueResult();
        logger.debug("訂單項目總數: {}", count);
        return count;
    }

    /**
     * 根據訂單ID計算訂單項目總數
     * @param orderId 訂單ID
     * @return 該訂單的項目總數
     */
    @Override
    @Transactional(readOnly = true)
    public long countByOrderId(Long orderId) {
        logger.debug("根據訂單ID計算訂單項目總數 - orderId: {}", orderId);
        Query<Long> query = getCurrentSession().createQuery(
                "SELECT COUNT(*) FROM OrderItem WHERE order.id = :orderId", Long.class);
        query.setParameter("orderId", orderId);
        Long count = query.uniqueResult();
        logger.debug("訂單項目總數 - orderId: {}, count: {}", orderId, count);
        return count;
    }

    /**
     * 根據商品ID計算訂單項目總數
     * @param productId 商品ID
     * @return 包含該商品的訂單項目總數
     */
    @Override
    @Transactional(readOnly = true)
    public long countByProductId(Long productId) {
        logger.debug("根據商品ID計算訂單項目總數 - productId: {}", productId);
        Query<Long> query = getCurrentSession().createQuery(
                "SELECT COUNT(*) FROM OrderItem WHERE product.id = :productId", Long.class);
        query.setParameter("productId", productId);
        Long count = query.uniqueResult();
        logger.debug("商品訂單項目總數 - productId: {}, count: {}", productId, count);
        return count;
    }

    /**
     * 根據訂單ID計算總商品數量
     * @param orderId 訂單ID
     * @return 該訂單的總商品數量
     */
    @Override
    @Transactional(readOnly = true)
    public int sumQuantityByOrderId(Long orderId) {
        logger.debug("根據訂單ID計算總商品數量 - orderId: {}", orderId);
        Query<Integer> query = getCurrentSession().createQuery(
                "SELECT COALESCE(SUM(quantity), 0) FROM OrderItem WHERE order.id = :orderId", Integer.class);
        query.setParameter("orderId", orderId);
        Integer totalQuantity = query.uniqueResult();
        logger.debug("訂單總商品數量 - orderId: {}, totalQuantity: {}", orderId, totalQuantity);
        return totalQuantity != null ? totalQuantity : 0;
    }

    /**
     * 根據商品ID計算總銷售數量
     * @param productId 商品ID
     * @return 該商品的總銷售數量
     */
    @Override
    @Transactional(readOnly = true)
    public int sumQuantityByProductId(Long productId) {
        logger.debug("根據商品ID計算總銷售數量 - productId: {}", productId);
        Query<Integer> query = getCurrentSession().createQuery(
                "SELECT COALESCE(SUM(quantity), 0) FROM OrderItem WHERE product.id = :productId", Integer.class);
        query.setParameter("productId", productId);
        Integer totalQuantity = query.uniqueResult();
        logger.debug("商品總銷售數量 - productId: {}, totalQuantity: {}", productId, totalQuantity);
        return totalQuantity != null ? totalQuantity : 0;
    }
}
