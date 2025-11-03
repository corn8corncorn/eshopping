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

import com.example.demo.dao.CartItemDAO;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;

/**
 * 購物車項目資料存取層實作類別
 * 負責與資料庫進行購物車項目相關的資料操作
 */
@Repository
@Transactional
public class CartItemDAOImpl implements CartItemDAO {

    private static final Logger logger = LoggerFactory.getLogger(CartItemDAOImpl.class);

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
     * 儲存購物車項目到資料庫
     * @param cartItem 要儲存的購物車項目物件
     * @return 儲存後的購物車項目物件（包含自動生成的 ID）
     */
    @Override
    public CartItem save(CartItem cartItem) {
        logger.info("開始儲存購物車項目到資料庫 - cartId: {}, productId: {}", 
                   cartItem.getCart().getId(), cartItem.getProduct().getId());
        getCurrentSession().saveOrUpdate(cartItem);
        logger.info("購物車項目儲存成功 - cartItemId: {}, cartId: {}, productId: {}", 
                   cartItem.getId(), cartItem.getCart().getId(), cartItem.getProduct().getId());
        return cartItem;
    }

    /**
     * 批量儲存購物車項目到資料庫
     * @param cartItems 要儲存的購物車項目列表
     * @return 儲存後的購物車項目列表
     */
    @Override
    public List<CartItem> saveAll(List<CartItem> cartItems) {
        logger.info("開始批量儲存購物車項目到資料庫 - count: {}", cartItems.size());
        for (CartItem cartItem : cartItems) {
            getCurrentSession().saveOrUpdate(cartItem);
        }
        logger.info("批量儲存購物車項目成功 - count: {}", cartItems.size());
        return cartItems;
    }

    /**
     * 根據ID查找購物車項目
     * @param id 購物車項目ID
     * @return 購物車項目物件，如果不存在則返回null
     */
    @Override
    @Transactional(readOnly = true)
    public CartItem findById(Long id) {
        logger.info("根據ID查找購物車項目 - cartItemId: {}", id);
        Query<CartItem> query = getCurrentSession().createQuery(
                "SELECT ci FROM CartItem ci LEFT JOIN FETCH ci.product WHERE ci.id = :id", CartItem.class);
        query.setParameter("id", id);
        CartItem cartItem = query.uniqueResult();
        logger.debug("購物車項目查詢結果 - cartItemId: {}, found: {}", id, cartItem != null);
        return cartItem;
    }

    /**
     * 查找所有購物車項目
     * @return 所有購物車項目的列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<CartItem> findAll() {
        logger.info("查找所有購物車項目");
        Query<CartItem> query = getCurrentSession().createQuery(
                "SELECT DISTINCT ci FROM CartItem ci LEFT JOIN FETCH ci.product ORDER BY ci.id", CartItem.class);
        List<CartItem> cartItems = query.getResultList();
        logger.debug("查詢到購物車項目數量: {}", cartItems.size());
        return cartItems;
    }

    /**
     * 根據購物車查找購物車項目
     * @param cart 購物車
     * @return 該購物車的所有項目列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<CartItem> findByCart(Cart cart) {
        logger.info("根據購物車查找購物車項目 - cartId: {}", cart.getId());
        Query<CartItem> query = getCurrentSession().createQuery(
                "SELECT DISTINCT ci FROM CartItem ci LEFT JOIN FETCH ci.product WHERE ci.cart = :cart ORDER BY ci.id", CartItem.class);
        query.setParameter("cart", cart);
        List<CartItem> cartItems = query.getResultList();
        logger.debug("購物車項目查詢結果 - cartId: {}, itemCount: {}", cart.getId(), cartItems.size());
        return cartItems;
    }

    /**
     * 根據購物車ID查找購物車項目
     * @param cartId 購物車ID
     * @return 該購物車的所有項目列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<CartItem> findByCartId(Long cartId) {
        logger.info("根據購物車ID查找購物車項目 - cartId: {}", cartId);
        Query<CartItem> query = getCurrentSession().createQuery(
                "SELECT DISTINCT ci FROM CartItem ci LEFT JOIN FETCH ci.product WHERE ci.cart.id = :cartId ORDER BY ci.id", CartItem.class);
        query.setParameter("cartId", cartId);
        List<CartItem> cartItems = query.getResultList();
        logger.debug("購物車項目查詢結果 - cartId: {}, itemCount: {}", cartId, cartItems.size());
        return cartItems;
    }

    /**
     * 根據商品查找購物車項目
     * @param product 商品
     * @return 包含該商品的所有購物車項目列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<CartItem> findByProduct(Product product) {
        logger.info("根據商品查找購物車項目 - productId: {}", product.getId());
        Query<CartItem> query = getCurrentSession().createQuery(
                "SELECT DISTINCT ci FROM CartItem ci LEFT JOIN FETCH ci.product WHERE ci.product = :product ORDER BY ci.id", CartItem.class);
        query.setParameter("product", product);
        List<CartItem> cartItems = query.getResultList();
        logger.debug("商品購物車項目查詢結果 - productId: {}, itemCount: {}", product.getId(), cartItems.size());
        return cartItems;
    }

    /**
     * 根據商品ID查找購物車項目
     * @param productId 商品ID
     * @return 包含該商品的所有購物車項目列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<CartItem> findByProductId(Long productId) {
        logger.info("根據商品ID查找購物車項目 - productId: {}", productId);
        Query<CartItem> query = getCurrentSession().createQuery(
                "SELECT DISTINCT ci FROM CartItem ci LEFT JOIN FETCH ci.product WHERE ci.product.id = :productId ORDER BY ci.id", CartItem.class);
        query.setParameter("productId", productId);
        List<CartItem> cartItems = query.getResultList();
        logger.debug("商品購物車項目查詢結果 - productId: {}, itemCount: {}", productId, cartItems.size());
        return cartItems;
    }

    /**
     * 根據購物車和商品查找購物車項目
     * @param cart 購物車
     * @param product 商品
     * @return 購物車項目物件，如果不存在則返回null
     */
    @Override
    @Transactional(readOnly = true)
    public CartItem findByCartAndProduct(Cart cart, Product product) {
        logger.info("根據購物車和商品查找購物車項目 - cartId: {}, productId: {}", 
                   cart.getId(), product.getId());
        Query<CartItem> query = getCurrentSession().createQuery(
                "SELECT ci FROM CartItem ci LEFT JOIN FETCH ci.product WHERE ci.cart = :cart AND ci.product = :product", CartItem.class);
        query.setParameter("cart", cart);
        query.setParameter("product", product);
        CartItem cartItem = query.uniqueResult();
        logger.debug("購物車項目查詢結果 - cartId: {}, productId: {}, found: {}", 
                    cart.getId(), product.getId(), cartItem != null);
        return cartItem;
    }

    /**
     * 根據購物車ID和商品ID查找購物車項目
     * @param cartId 購物車ID
     * @param productId 商品ID
     * @return 購物車項目物件，如果不存在則返回null
     */
    @Override
    @Transactional(readOnly = true)
    public CartItem findByCartIdAndProductId(Long cartId, Long productId) {
        logger.info("根據購物車ID和商品ID查找購物車項目 - cartId: {}, productId: {}", cartId, productId);
        Query<CartItem> query = getCurrentSession().createQuery(
                "SELECT ci FROM CartItem ci LEFT JOIN FETCH ci.product WHERE ci.cart.id = :cartId AND ci.product.id = :productId", CartItem.class);
        query.setParameter("cartId", cartId);
        query.setParameter("productId", productId);
        CartItem cartItem = query.uniqueResult();
        logger.debug("購物車項目查詢結果 - cartId: {}, productId: {}, found: {}", 
                    cartId, productId, cartItem != null);
        return cartItem;
    }

    /**
     * 刪除購物車項目
     * @param id 要刪除的購物車項目ID
     */
    @Override
    public void delete(Long id) {
        logger.info("開始刪除購物車項目 - cartItemId: {}", id);
        CartItem cartItem = getCurrentSession().get(CartItem.class, id);
        if (cartItem != null) {
            getCurrentSession().delete(cartItem);
            logger.info("購物車項目刪除成功 - cartItemId: {}", id);
        } else {
            logger.warn("要刪除的購物車項目不存在 - cartItemId: {}", id);
        }
    }

    /**
     * 刪除購物車項目
     * @param cartItem 要刪除的購物車項目物件
     */
    @Override
    public void delete(CartItem cartItem) {
        logger.info("開始刪除購物車項目 - cartItemId: {}", cartItem.getId());
        getCurrentSession().delete(cartItem);
        logger.info("購物車項目刪除成功 - cartItemId: {}", cartItem.getId());
    }

    /**
     * 批量刪除購物車項目
     * @param ids 要刪除的購物車項目ID列表
     */
    @Override
    public void deleteAllById(List<Long> ids) {
        logger.info("開始批量刪除購物車項目 - count: {}", ids.size());
        Query<?> query = getCurrentSession().createQuery(
                "DELETE FROM CartItem WHERE id IN :ids");
        query.setParameter("ids", ids);
        int deletedCount = query.executeUpdate();
        logger.info("批量刪除購物車項目成功 - deletedCount: {}", deletedCount);
    }

    /**
     * 根據購物車刪除所有購物車項目
     * @param cartId 購物車ID
     */
    @Override
    public void deleteAllByCartId(Long cartId) {
        logger.info("根據購物車刪除所有購物車項目 - cartId: {}", cartId);
        Query<?> query = getCurrentSession().createQuery(
                "DELETE FROM CartItem WHERE cart.id = :cartId");
        query.setParameter("cartId", cartId);
        int deletedCount = query.executeUpdate();
        logger.info("刪除購物車所有項目成功 - cartId: {}, deletedCount: {}", cartId, deletedCount);
    }

    /**
     * 檢查購物車項目是否存在
     * @param id 購物車項目ID
     * @return 如果存在返回true，否則返回false
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        logger.debug("檢查購物車項目是否存在 - cartItemId: {}", id);
        Query<Long> query = getCurrentSession().createQuery(
                "SELECT COUNT(*) FROM CartItem WHERE id = :id", Long.class);
        query.setParameter("id", id);
        Long count = query.uniqueResult();
        boolean exists = count > 0;
        logger.debug("購物車項目存在檢查結果 - cartItemId: {}, exists: {}", id, exists);
        return exists;
    }

    /**
     * 計算購物車項目總數
     * @return 購物車項目總數
     */
    @Override
    @Transactional(readOnly = true)
    public long count() {
        logger.debug("計算購物車項目總數");
        Query<Long> query = getCurrentSession().createQuery(
                "SELECT COUNT(*) FROM CartItem", Long.class);
        Long count = query.uniqueResult();
        logger.debug("購物車項目總數: {}", count);
        return count;
    }

    /**
     * 根據購物車ID計算購物車項目總數
     * @param cartId 購物車ID
     * @return 該購物車的項目總數
     */
    @Override
    @Transactional(readOnly = true)
    public long countByCartId(Long cartId) {
        logger.debug("根據購物車ID計算購物車項目總數 - cartId: {}", cartId);
        Query<Long> query = getCurrentSession().createQuery(
                "SELECT COUNT(*) FROM CartItem WHERE cart.id = :cartId", Long.class);
        query.setParameter("cartId", cartId);
        Long count = query.uniqueResult();
        logger.debug("購物車項目總數 - cartId: {}, count: {}", cartId, count);
        return count;
    }

    /**
     * 根據購物車ID計算總商品數量
     * @param cartId 購物車ID
     * @return 該購物車的總商品數量
     */
    @Override
    @Transactional(readOnly = true)
    public int sumQuantityByCartId(Long cartId) {
        logger.debug("根據購物車ID計算總商品數量 - cartId: {}", cartId);
        Query<Integer> query = getCurrentSession().createQuery(
                "SELECT COALESCE(SUM(quantity), 0) FROM CartItem WHERE cart.id = :cartId", Integer.class);
        query.setParameter("cartId", cartId);
        Integer totalQuantity = query.uniqueResult();
        logger.debug("購物車總商品數量 - cartId: {}, totalQuantity: {}", cartId, totalQuantity);
        return totalQuantity != null ? totalQuantity : 0;
    }
}
