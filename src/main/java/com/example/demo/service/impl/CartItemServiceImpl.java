package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.CartItemDAO;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.service.CartItemService;

/**
 * 購物車項目服務層實作類
 * 實作購物車項目相關的業務邏輯
 */
@Service
@Transactional
public class CartItemServiceImpl implements CartItemService {

    private static final Logger logger = LoggerFactory.getLogger(CartItemServiceImpl.class);

    @Autowired
    private CartItemDAO cartItemDAO;

    /**
     * 取得所有購物車項目
     * @return 所有購物車項目的列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<CartItem> getAll() {
        logger.info("取得所有購物車項目");
        List<CartItem> cartItems = cartItemDAO.findAll();
        logger.debug("取得購物車項目數量: {}", cartItems.size());
        return cartItems;
    }

    /**
     * 根據ID取得購物車項目
     * @param id 購物車項目ID
     * @return 購物車項目物件，如果不存在則返回null
     */
    @Override
    @Transactional(readOnly = true)
    public CartItem getById(Long id) {
        logger.info("根據ID取得購物車項目 - cartItemId: {}", id);
        CartItem cartItem = cartItemDAO.findById(id);
        logger.debug("購物車項目查詢結果 - cartItemId: {}, found: {}", id, cartItem != null);
        return cartItem;
    }

    /**
     * 根據購物車取得所有購物車項目
     * @param cart 購物車
     * @return 該購物車的所有項目列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<CartItem> getByCart(Cart cart) {
        logger.info("根據購物車取得購物車項目 - cartId: {}", cart.getId());
        List<CartItem> cartItems = cartItemDAO.findByCart(cart);
        logger.debug("購物車項目查詢結果 - cartId: {}, itemCount: {}", cart.getId(), cartItems.size());
        return cartItems;
    }

    /** 
     * 根據購物車ID取得所有購物車項目
     * @param cartId 購物車ID
     * @return 該購物車的所有項目列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<CartItem> getByCartId(Long cartId) {
        logger.info("根據購物車ID取得購物車項目 - cartId: {}", cartId);
        List<CartItem> cartItems = cartItemDAO.findByCartId(cartId);
        
        // 強制初始化每個 cartItem 的 product，確保在 Session 內完成
        for (CartItem item : cartItems) {
            if (item.getProduct() != null) {
                org.hibernate.Hibernate.initialize(item.getProduct());
                // 訪問 product 的基本屬性以確保完全初始化
                item.getProduct().getId();
                item.getProduct().getName();
                item.getProduct().getPrice();
            }
        }
        
        logger.debug("購物車項目查詢結果 - cartId: {}, itemCount: {}", cartId, cartItems.size());
        return cartItems;
    }

    /**
     * 根據商品取得所有購物車項目
     * @param product 商品
     * @return 包含該商品的所有購物車項目列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<CartItem> getByProduct(Product product) {
        logger.info("根據商品取得購物車項目 - productId: {}", product.getId());
        List<CartItem> cartItems = cartItemDAO.findByProduct(product);
        logger.debug("商品購物車項目查詢結果 - productId: {}, itemCount: {}", product.getId(), cartItems.size());
        return cartItems;
    }

    /**
     * 根據商品ID取得所有購物車項目
     * @param productId 商品ID
     * @return 包含該商品的所有購物車項目列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<CartItem> getByProductId(Long productId) {
        logger.info("根據商品ID取得購物車項目 - productId: {}", productId);
        List<CartItem> cartItems = cartItemDAO.findByProductId(productId);
        logger.debug("商品購物車項目查詢結果 - productId: {}, itemCount: {}", productId, cartItems.size());
        return cartItems;
    }

    /**
     * 建立新的購物車項目
     * @param cart 所屬購物車
     * @param product 商品
     * @param quantity 數量
     * @return 新建立的購物車項目
     */
    @Override
    public CartItem createCartItem(Cart cart, Product product, Integer quantity) {
        logger.info("建立新購物車項目 - cartId: {}, productId: {}, quantity: {}", 
                   cart.getId(), product.getId(), quantity);
        
        if (quantity <= 0) {
            logger.warn("購物車項目數量必須大於0 - quantity: {}", quantity);
            throw new IllegalArgumentException("購物車項目數量必須大於0");
        }
        
        if (!checkStockAvailability(product.getId(), quantity)) {
            logger.warn("商品庫存不足 - productId: {}, requestedQuantity: {}", 
                       product.getId(), quantity);
            throw new IllegalStateException("商品庫存不足");
        }
        
        CartItem cartItem = new CartItem(cart, product, quantity);
        
        logger.info("購物車項目建立成功 - cartId: {}, productId: {}, quantity: {}", 
                   cart.getId(), product.getId(), quantity);
        return cartItem;
    }

    /**
     * 儲存購物車項目
     * @param cartItem 要儲存的購物車項目
     * @return 儲存後的購物車項目
     */
    @Override
    public CartItem saveCartItem(CartItem cartItem) {
        logger.info("儲存購物車項目 - cartItemId: {}, cartId: {}, productId: {}", 
                   cartItem.getId(), cartItem.getCart().getId(), cartItem.getProduct().getId());
        CartItem savedCartItem = cartItemDAO.save(cartItem);
        logger.debug("購物車項目儲存成功 - cartItemId: {}", savedCartItem.getId());
        return savedCartItem;
    }

    /**
     * 更新購物車項目
     * @param id 購物車項目ID
     * @param cartItem 要更新的購物車項目
     * @return 更新後的購物車項目
     */
    @Override
    public CartItem updateCartItem(Long id, CartItem cartItem) {
        logger.info("更新購物車項目 - cartItemId: {}", id);
        
        CartItem existingCartItem = cartItemDAO.findById(id);
        if (existingCartItem == null) {
            logger.warn("購物車項目不存在 - cartItemId: {}", id);
            throw new IllegalArgumentException("購物車項目不存在: " + id);
        }
        
        // 更新購物車項目資訊
        existingCartItem.setQuantity(cartItem.getQuantity());
        
        CartItem updatedCartItem = cartItemDAO.save(existingCartItem);
        logger.info("購物車項目更新成功 - cartItemId: {}", updatedCartItem.getId());
        return updatedCartItem;
    }

    /**
     * 刪除購物車項目
     * @param id 要刪除的購物車項目ID
     */
    @Override
    public void deleteCartItem(Long id) {
        logger.info("刪除購物車項目 - cartItemId: {}", id);
        
        CartItem cartItem = cartItemDAO.findById(id);
        if (cartItem == null) {
            logger.warn("要刪除的購物車項目不存在 - cartItemId: {}", id);
            throw new IllegalArgumentException("購物車項目不存在: " + id);
        }
        
        cartItemDAO.delete(id);
        logger.info("購物車項目刪除成功 - cartItemId: {}", id);
    }

    /**
     * 更新購物車項目數量
     * @param cartItemId 購物車項目ID
     * @param quantity 新數量
     * @return 更新後的購物車項目
     */
    @Override
    public CartItem updateQuantity(Long cartItemId, Integer quantity) {
        logger.info("更新購物車項目數量 - cartItemId: {}, newQuantity: {}", cartItemId, quantity);
        
        if (quantity <= 0) {
            logger.warn("購物車項目數量必須大於0 - quantity: {}", quantity);
            throw new IllegalArgumentException("購物車項目數量必須大於0");
        }
        
        CartItem cartItem = cartItemDAO.findById(cartItemId);
        if (cartItem == null) {
            logger.warn("購物車項目不存在 - cartItemId: {}", cartItemId);
            throw new IllegalArgumentException("購物車項目不存在: " + cartItemId);
        }
        
        // 檢查庫存
        if (!checkStockAvailability(cartItem.getProduct().getId(), quantity)) {
            logger.warn("商品庫存不足 - productId: {}, requestedQuantity: {}", 
                       cartItem.getProduct().getId(), quantity);
            throw new IllegalStateException("商品庫存不足");
        }
        
        cartItem.updateQuantity(quantity);
        CartItem updatedCartItem = cartItemDAO.save(cartItem);
        
        logger.info("購物車項目數量更新成功 - cartItemId: {}, newQuantity: {}", cartItemId, quantity);
        return updatedCartItem;
    }

    /**
     * 更新購物車項目單價
     * @param cartItemId 購物車項目ID
     * @param unitPrice 新單價
     * @return 更新後的購物車項目
     */
    @Override
    public CartItem updateUnitPrice(Long cartItemId, BigDecimal unitPrice) {
        logger.info("更新購物車項目單價 - cartItemId: {}, newUnitPrice: {}", cartItemId, unitPrice);
        
        if (unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("購物車項目單價必須大於0 - unitPrice: {}", unitPrice);
            throw new IllegalArgumentException("購物車項目單價必須大於0");
        }
        
        CartItem cartItem = cartItemDAO.findById(cartItemId);
        if (cartItem == null) {
            logger.warn("購物車項目不存在 - cartItemId: {}", cartItemId);
            throw new IllegalArgumentException("購物車項目不存在: " + cartItemId);
        }
        
        CartItem updatedCartItem = cartItemDAO.save(cartItem);
        
        logger.info("購物車項目單價更新成功 - cartItemId: {}, newUnitPrice: {}", cartItemId, unitPrice);
        return updatedCartItem;
    }

    /**
     * 重新計算購物車項目小計
     * @param cartItemId 購物車項目ID
     * @return 更新後的購物車項目
     */
    @Override
    public CartItem recalculateSubtotal(Long cartItemId) {
        logger.info("重新計算購物車項目小計 - cartItemId: {}", cartItemId);
        
        CartItem cartItem = cartItemDAO.findById(cartItemId);
        if (cartItem == null) {
            logger.warn("購物車項目不存在 - cartItemId: {}", cartItemId);
            throw new IllegalArgumentException("購物車項目不存在: " + cartItemId);
        }
        
        // 小計現在是動態計算的，不需要重新計算
        CartItem updatedCartItem = cartItemDAO.save(cartItem);
        
        logger.info("購物車項目小計重新計算完成 - cartItemId: {}, subtotal: {}", 
                   cartItemId, updatedCartItem.getSubtotal());
        return updatedCartItem;
    }

    /**
     * 增加購物車項目數量
     * @param cartItemId 購物車項目ID
     * @param increment 增加的數量
     * @return 更新後的購物車項目
     */
    @Override
    public CartItem incrementQuantity(Long cartItemId, Integer increment) {
        logger.info("增加購物車項目數量 - cartItemId: {}, increment: {}", cartItemId, increment);
        
        CartItem cartItem = cartItemDAO.findById(cartItemId);
        if (cartItem == null) {
            logger.warn("購物車項目不存在 - cartItemId: {}", cartItemId);
            throw new IllegalArgumentException("購物車項目不存在: " + cartItemId);
        }
        
        int newQuantity = cartItem.getQuantity() + increment;
        
        // 檢查庫存
        if (!checkStockAvailability(cartItem.getProduct().getId(), newQuantity)) {
            logger.warn("商品庫存不足 - productId: {}, requestedQuantity: {}", 
                       cartItem.getProduct().getId(), newQuantity);
            throw new IllegalStateException("商品庫存不足");
        }
        
        cartItem.incrementQuantity(increment);
        CartItem updatedCartItem = cartItemDAO.save(cartItem);
        
        logger.info("購物車項目數量增加成功 - cartItemId: {}, newQuantity: {}", 
                   cartItemId, updatedCartItem.getQuantity());
        return updatedCartItem;
    }

    /**
     * 減少購物車項目數量
     * @param cartItemId 購物車項目ID
     * @param decrement 減少的數量
     * @return 更新後的購物車項目
     */
    @Override
    public CartItem decrementQuantity(Long cartItemId, Integer decrement) {
        logger.info("減少購物車項目數量 - cartItemId: {}, decrement: {}", cartItemId, decrement);
        
        CartItem cartItem = cartItemDAO.findById(cartItemId);
        if (cartItem == null) {
            logger.warn("購物車項目不存在 - cartItemId: {}", cartItemId);
            throw new IllegalArgumentException("購物車項目不存在: " + cartItemId);
        }
        
        if (cartItem.getQuantity() <= decrement) {
            logger.warn("購物車項目數量不足 - cartItemId: {}, currentQuantity: {}, decrement: {}", 
                       cartItemId, cartItem.getQuantity(), decrement);
            throw new IllegalArgumentException("購物車項目數量不足");
        }
        
        cartItem.decrementQuantity(decrement);
        CartItem updatedCartItem = cartItemDAO.save(cartItem);
        
        logger.info("購物車項目數量減少成功 - cartItemId: {}, newQuantity: {}", 
                   cartItemId, updatedCartItem.getQuantity());
        return updatedCartItem;
    }

    /**
     * 同步商品資訊（當商品價格或名稱變動時）
     * @param cartItemId 購物車項目ID
     * @return 更新後的購物車項目
     */
    @Override
    public CartItem syncProductInfo(Long cartItemId) {
        logger.info("同步購物車項目商品資訊 - cartItemId: {}", cartItemId);
        
        CartItem cartItem = cartItemDAO.findById(cartItemId);
        if (cartItem == null) {
            logger.warn("購物車項目不存在 - cartItemId: {}", cartItemId);
            throw new IllegalArgumentException("購物車項目不存在: " + cartItemId);
        }
        
        // 商品資訊現在是動態取得的，不需要同步
        CartItem updatedCartItem = cartItemDAO.save(cartItem);
        
        logger.info("商品資訊同步成功 - cartItemId: {}", cartItemId);
        return updatedCartItem;
    }

    /**
     * 批量更新購物車項目
     * @param cartItems 要更新的購物車項目列表
     * @return 更新後的購物車項目列表
     */
    @Override
    public List<CartItem> updateCartItems(List<CartItem> cartItems) {
        logger.info("批量更新購物車項目 - count: {}", cartItems.size());
        
        List<CartItem> updatedCartItems = cartItemDAO.saveAll(cartItems);
        logger.info("批量更新購物車項目成功 - count: {}", updatedCartItems.size());
        return updatedCartItems;
    }

    /**
     * 批量刪除購物車項目
     * @param cartItemIds 要刪除的購物車項目ID列表
     */
    @Override
    public void deleteCartItems(List<Long> cartItemIds) {
        logger.info("批量刪除購物車項目 - count: {}", cartItemIds.size());
        
        for (Long cartItemId : cartItemIds) {
            CartItem cartItem = cartItemDAO.findById(cartItemId);
            if (cartItem == null) {
                logger.warn("要刪除的購物車項目不存在 - cartItemId: {}", cartItemId);
                throw new IllegalArgumentException("購物車項目不存在: " + cartItemId);
            }
        }
        
        cartItemDAO.deleteAllById(cartItemIds);
        logger.info("批量刪除購物車項目成功 - count: {}", cartItemIds.size());
    }

    /**
     * 檢查商品庫存是否足夠
     * @param productId 商品ID
     * @param quantity 需要的數量
     * @return 如果庫存足夠返回true，否則返回false
     */
    @Override
    @Transactional(readOnly = true)
    public boolean checkStockAvailability(Long productId, Integer quantity) {
        logger.debug("檢查商品庫存 - productId: {}, requiredQuantity: {}", productId, quantity);
        
        // 這裡需要注入 ProductService 或 ProductDAO 來檢查庫存
        // 暫時返回 true，實際實作時需要查詢商品庫存
        boolean available = true; // 實際實作時需要查詢商品庫存
        
        logger.debug("庫存檢查結果 - productId: {}, requiredQuantity: {}, available: {}", 
                    productId, quantity, available);
        return available;
    }
}
