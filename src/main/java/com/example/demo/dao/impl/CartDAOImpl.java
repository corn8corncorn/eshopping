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

import com.example.demo.dao.CartDAO;
import com.example.demo.model.Cart;
import com.example.demo.model.Customer;

/**
 * 購物車資料存取層實作類別
 * 負責與資料庫進行購物車相關的資料操作
 */
@Repository
@Transactional
public class CartDAOImpl implements CartDAO {

    private static final Logger logger = LoggerFactory.getLogger(CartDAOImpl.class);

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
     * 儲存購物車到資料庫
     * @param cart 要儲存的購物車物件
     * @return 儲存後的購物車物件（包含自動生成的 ID）
     */
    @Override
    public Cart save(Cart cart) {
        logger.info("開始儲存購物車到資料庫 - customerId: {}", cart.getCustomer().getId());
        cart.calculateTotalAmount();
        getCurrentSession().saveOrUpdate(cart);
        logger.info("購物車儲存成功 - cartId: {}, customerId: {}, totalAmount: {}", 
                   cart.getId(), cart.getCustomer().getId(), cart.getTotalAmount());
        return cart;
    }

    /**
     * 根據ID查找購物車
     * @param id 購物車ID
     * @return 購物車物件，如果不存在則返回null
     */
    @Override
    @Transactional(readOnly = true)
    public Cart findById(Long id) {
        logger.info("根據ID查找購物車 - cartId: {}", id);
        Cart cart = getCurrentSession().get(Cart.class, id);
        logger.debug("購物車查詢結果 - cartId: {}, found: {}", id, cart != null);
        return cart;
    }

    /**
     * 查找所有購物車
     * @return 所有購物車的列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<Cart> findAll() {
        logger.info("查找所有購物車");
        Query<Cart> query = getCurrentSession().createQuery("FROM Cart ORDER BY updatedAt DESC", Cart.class);
        List<Cart> carts = query.getResultList();
        logger.debug("查詢到購物車數量: {}", carts.size());
        return carts;
    }

    /**
     * 根據客戶查找購物車
     * @param customer 客戶
     * @return 該客戶的購物車，如果不存在則返回null
     */
    @Override
    @Transactional(readOnly = true)
    public Cart findByCustomer(Customer customer) {
        logger.info("根據客戶查找購物車 - customerId: {}", customer.getId());
        Query<Cart> query = getCurrentSession().createQuery(
                "FROM Cart WHERE customer = :customer", Cart.class);
        query.setParameter("customer", customer);
        Cart cart = query.uniqueResult();
        logger.debug("購物車查詢結果 - customerId: {}, found: {}", customer.getId(), cart != null);
        return cart;
    }

    /**
     * 根據客戶ID查找購物車
     * @param customerId 客戶ID
     * @return 該客戶的購物車，如果不存在則返回null
     */
    @Override
    @Transactional(readOnly = true)
    public Cart findByCustomerId(Long customerId) {
        logger.info("根據客戶ID查找購物車 - customerId: {}", customerId);
        Query<Cart> query = getCurrentSession().createQuery(
                "FROM Cart WHERE customer.id = :customerId", Cart.class);
        query.setParameter("customerId", customerId);
        Cart cart = query.uniqueResult();
        logger.debug("購物車查詢結果 - customerId: {}, found: {}", customerId, cart != null);
        return cart;
    }

    /**
     * 刪除購物車
     * @param id 要刪除的購物車ID
     */
    @Override
    public void delete(Long id) {
        logger.info("開始刪除購物車 - cartId: {}", id);
        Cart cart = getCurrentSession().get(Cart.class, id);
        if (cart != null) {
            getCurrentSession().delete(cart);
            logger.info("購物車刪除成功 - cartId: {}", id);
        } else {
            logger.warn("要刪除的購物車不存在 - cartId: {}", id);
        }
    }

    /**
     * 刪除購物車
     * @param cart 要刪除的購物車物件
     */
    @Override
    public void delete(Cart cart) {
        logger.info("開始刪除購物車 - cartId: {}", cart.getId());
        getCurrentSession().delete(cart);
        logger.info("購物車刪除成功 - cartId: {}", cart.getId());
    }

    /**
     * 檢查購物車是否存在
     * @param id 購物車ID
     * @return 如果存在返回true，否則返回false
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        logger.debug("檢查購物車是否存在 - cartId: {}", id);
        Query<Long> query = getCurrentSession().createQuery(
                "SELECT COUNT(*) FROM Cart WHERE id = :id", Long.class);
        query.setParameter("id", id);
        Long count = query.uniqueResult();
        boolean exists = count > 0;
        logger.debug("購物車存在檢查結果 - cartId: {}, exists: {}", id, exists);
        return exists;
    }

    /**
     * 檢查客戶是否有購物車
     * @param customerId 客戶ID
     * @return 如果有購物車返回true，否則返回false
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsByCustomerId(Long customerId) {
        logger.debug("檢查客戶是否有購物車 - customerId: {}", customerId);
        Query<Long> query = getCurrentSession().createQuery(
                "SELECT COUNT(*) FROM Cart WHERE customer.id = :customerId", Long.class);
        query.setParameter("customerId", customerId);
        Long count = query.uniqueResult();
        boolean exists = count > 0;
        logger.debug("客戶購物車存在檢查結果 - customerId: {}, exists: {}", customerId, exists);
        return exists;
    }

    /**
     * 計算購物車總數
     * @return 購物車總數
     */
    @Override
    @Transactional(readOnly = true)
    public long count() {
        logger.debug("計算購物車總數");
        Query<Long> query = getCurrentSession().createQuery(
                "SELECT COUNT(*) FROM Cart", Long.class);
        Long count = query.uniqueResult();
        logger.debug("購物車總數: {}", count);
        return count;
    }

    /**
     * 根據客戶ID計算購物車總數
     * @param customerId 客戶ID
     * @return 該客戶的購物車數量（通常為0或1）
     */
    @Override
    @Transactional(readOnly = true)
    public long countByCustomerId(Long customerId) {
        logger.debug("根據客戶ID計算購物車總數 - customerId: {}", customerId);
        Query<Long> query = getCurrentSession().createQuery(
                "SELECT COUNT(*) FROM Cart WHERE customer.id = :customerId", Long.class);
        query.setParameter("customerId", customerId);
        Long count = query.uniqueResult();
        logger.debug("客戶購物車總數 - customerId: {}, count: {}", customerId, count);
        return count;
    }
}
