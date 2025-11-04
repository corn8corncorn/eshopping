package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.CartDAO;
import com.example.demo.dao.CartItemDAO;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Customer;
import com.example.demo.model.Product;
import com.example.demo.service.CartItemService;
import com.example.demo.service.CartService;

/**
 * 購物車服務層實作類
 * 實作購物車相關的業務邏輯
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    private CartDAO cartDAO;

    @Autowired
    private CartItemDAO cartItemDAO;

    @Autowired
    private CartItemService cartItemService;

    /**
     * 取得所有購物車
     * @return 所有購物車的列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<Cart> getAll() {
        logger.info("取得所有購物車");
        List<Cart> carts = cartDAO.findAll();
        logger.debug("取得購物車數量: {}", carts.size());
        return carts;
    }

    /**
     * 根據ID取得購物車
     * @param id 購物車ID
     * @return 購物車物件，如果不存在則返回null
     */
    @Override
    @Transactional(readOnly = true)
    public Cart getById(Long id) {
        logger.info("根據ID取得購物車 - cartId: {}", id);
        Cart cart = cartDAO.findById(id);
        if (cart != null) {
            // 強制初始化所有 lazy 關聯，確保在 Session 內完成
            Hibernate.initialize(cart.getCartItems());
            if (cart.getCartItems() != null) {
                // 觸發每個 cartItem 的 product 初始化
                for (CartItem item : cart.getCartItems()) {
                    if (item.getProduct() != null) {
                        Hibernate.initialize(item.getProduct());
                        // 訪問 product 的基本屬性以確保完全初始化
                        item.getProduct().getId();
                        item.getProduct().getName();
                        item.getProduct().getPrice();
                    }
                }
            }
        }
        logger.debug("購物車查詢結果 - cartId: {}, found: {}", id, cart != null);
        return cart;
    }

    /**
     * 根據客戶取得購物車
     * @param customer 客戶
     * @return 該客戶的購物車，如果不存在則返回null
     */
    @Override
    @Transactional(readOnly = true)
    public Cart getByCustomer(Customer customer) {
        logger.info("根據客戶取得購物車 - customerId: {}", customer.getId());
        Cart cart = cartDAO.findByCustomer(customer);
        if (cart != null) {
            // 強制初始化所有 lazy 關聯，確保在 Session 內完成
            Hibernate.initialize(cart.getCartItems());
            if (cart.getCartItems() != null) {
                // 觸發每個 cartItem 的 product 初始化
                for (CartItem item : cart.getCartItems()) {
                    if (item.getProduct() != null) {
                        Hibernate.initialize(item.getProduct());
                        // 訪問 product 的基本屬性以確保完全初始化
                        item.getProduct().getId();
                        item.getProduct().getName();
                        item.getProduct().getPrice();
                    }
                }
            }
        }
        logger.debug("購物車查詢結果 - customerId: {}, found: {}", customer.getId(), cart != null);
        return cart;
    }

    /**
     * 根據客戶ID取得購物車
     * @param customerId 客戶ID
     * @return 該客戶的購物車，如果不存在則返回null
     */
    @Override
    @Transactional(readOnly = true)
    public Cart getByCustomerId(Long customerId) {
        logger.info("根據客戶ID取得購物車 - customerId: {}", customerId);
        Cart cart = cartDAO.findByCustomerId(customerId);
        logger.debug("購物車查詢結果 - customerId: {}, found: {}", customerId, cart != null);
        return cart;
    }

    /**
     * 為客戶建立或取得購物車
     * @param customer 客戶
     * @return 購物車物件
     */
    @Override
    public Cart getOrCreateCart(Customer customer) {
        logger.info("取得或建立購物車 - customerId: {}", customer.getId());
        Cart cart = cartDAO.findByCustomer(customer);
        if (cart == null) {
            logger.info("購物車不存在，建立新購物車 - customerId: {}", customer.getId());
            cart = createCart(customer);
        } else {
            // 強制初始化所有 lazy 關聯，確保在 Session 內完成
            Hibernate.initialize(cart.getCartItems());
            if (cart.getCartItems() != null && !cart.getCartItems().isEmpty()) {
                // 觸發每個 cartItem 的 product 初始化
                for (CartItem item : cart.getCartItems()) {
                    if (item.getProduct() != null) {
                        Hibernate.initialize(item.getProduct());
                        // 訪問 product 的基本屬性以確保完全初始化
                        // 必須在訪問 getPrice() 之前確保 product 完全初始化
                        item.getProduct().getId();
                        item.getProduct().getName();
                        BigDecimal price = item.getProduct().getPrice(); // 明確調用以初始化
                        // 確保 price 不為 null
                        if (price == null) {
                            logger.warn("商品價格為 null - productId: {}", item.getProduct().getId());
                        }
                    }
                }
                // 現在可以安全地計算，因為所有數據都已初始化
                // 但不在此處調用，讓 Controller 通過專門的方法調用
            }
        }
        return cart;
    }

    /**
     * 為客戶建立或取得購物車
     * @param customerId 客戶ID
     * @return 購物車物件
     */
    @Override
    public Cart getOrCreateCart(Long customerId) {
        logger.info("取得或建立購物車 - customerId: {}", customerId);
        Cart cart = cartDAO.findByCustomerId(customerId);
        if (cart == null) {
            logger.info("購物車不存在，建立新購物車 - customerId: {}", customerId);
            // 這裡需要 CustomerService 來取得 Customer，暫時簡化處理
            throw new IllegalArgumentException("需要 Customer 物件來建立購物車，請使用 getOrCreateCart(Customer) 方法");
        }
        return cart;
    }

    /**
     * 建立新購物車
     * @param customer 客戶
     * @return 新建立的購物車
     */
    @Override
    public Cart createCart(Customer customer) {
        logger.info("建立新購物車 - customerId: {}", customer.getId());
        
        Cart cart = new Cart(customer);
        Cart savedCart = cartDAO.save(cart);
        
        logger.info("購物車建立成功 - cartId: {}, customerId: {}", savedCart.getId(), customer.getId());
        return savedCart;
    }

    /**
     * 儲存購物車
     * @param cart 要儲存的購物車
     * @return 儲存後的購物車
     */
    @Override
    public Cart saveCart(Cart cart) {
        logger.info("儲存購物車 - cartId: {}, customerId: {}", cart.getId(), cart.getCustomer().getId());
        Cart savedCart = cartDAO.save(cart);
        logger.debug("購物車儲存成功 - cartId: {}", savedCart.getId());
        return savedCart;
    }

    /**
     * 更新購物車
     * @param id 購物車ID
     * @param cart 要更新的購物車
     * @return 更新後的購物車
     */
    @Override
    public Cart updateCart(Long id, Cart cart) {
        logger.info("更新購物車 - cartId: {}", id);
        
        Cart existingCart = cartDAO.findById(id);
        if (existingCart == null) {
            logger.warn("購物車不存在 - cartId: {}", id);
            throw new IllegalArgumentException("購物車不存在: " + id);
        }
        
        // 更新購物車資訊（通常不需要更新，因為購物車資訊由項目自動計算）
        Cart updatedCart = cartDAO.save(existingCart);
        
        logger.info("購物車更新成功 - cartId: {}", updatedCart.getId());
        return updatedCart;
    }

    /**
     * 刪除購物車
     * @param id 要刪除的購物車ID
     */
    @Override
    public void deleteCart(Long id) {
        logger.info("刪除購物車 - cartId: {}", id);
        
        Cart cart = cartDAO.findById(id);
        if (cart == null) {
            logger.warn("要刪除的購物車不存在 - cartId: {}", id);
            throw new IllegalArgumentException("購物車不存在: " + id);
        }
        
        cartDAO.delete(id);
        logger.info("購物車刪除成功 - cartId: {}", id);
    }

    /**
     * 清空購物車
     * @param cartId 購物車ID
     * @return 清空後的購物車
     */
    @Override
    public Cart clearCart(Long cartId) {
        logger.info("清空購物車 - cartId: {}", cartId);
        
        Cart cart = cartDAO.findById(cartId);
        if (cart == null) {
            logger.warn("購物車不存在 - cartId: {}", cartId);
            throw new IllegalArgumentException("購物車不存在: " + cartId);
        }
        
        // 使用 HQL DELETE 直接刪除數據庫中的記錄（繞過 session 中的實體管理）
        cartItemDAO.deleteAllByCartId(cartId);
        
        // 重新加載購物車以確保狀態一致
        cart = cartDAO.findById(cartId);
        if (cart != null) {
            // 清空內存中的列表（此時數據庫記錄已刪除，列表應該為空）
            cart.clearCart();
            // 保存購物車
            Cart updatedCart = cartDAO.save(cart);
            logger.info("購物車清空成功 - cartId: {}", cartId);
            return updatedCart;
        }
        
        logger.warn("清空購物車後重新載入失敗 - cartId: {}", cartId);
        throw new IllegalStateException("清空購物車失敗");
    }

    /**
     * 添加商品到購物車
     * @param cartId 購物車ID
     * @param product 商品
     * @param quantity 數量
     * @return 更新後的購物車項目
     */
    @Override
    public CartItem addProductToCart(Long cartId, Product product, Integer quantity) {
        logger.info("添加商品到購物車 - cartId: {}, productId: {}, quantity: {}", 
                   cartId, product.getId(), quantity);
        
        Cart cart = cartDAO.findById(cartId);
        if (cart == null) {
            logger.warn("購物車不存在 - cartId: {}", cartId);
            throw new IllegalArgumentException("購物車不存在: " + cartId);
        }
        
        if (quantity <= 0) {
            logger.warn("商品數量必須大於0 - quantity: {}", quantity);
            throw new IllegalArgumentException("商品數量必須大於0");
        }
        
        // 檢查庫存
        if (!cartItemService.checkStockAvailability(product.getId(), quantity)) {
            logger.warn("商品庫存不足 - productId: {}, requestedQuantity: {}", 
                       product.getId(), quantity);
            throw new IllegalStateException("商品庫存不足");
        }
        
        // 檢查購物車中是否已存在該商品
        CartItem existingItem = cartItemDAO.findByCartIdAndProductId(cartId, product.getId());
        
        if (existingItem != null) {
            // 如果已存在，增加數量
            logger.info("購物車中已存在該商品，增加數量 - cartItemId: {}, currentQuantity: {}, increment: {}", 
                       existingItem.getId(), existingItem.getQuantity(), quantity);
            existingItem.incrementQuantity(quantity);
            CartItem updatedItem = cartItemDAO.save(existingItem);
            return updatedItem;
        } else {
            // 如果不存在，建立新項目
            logger.info("購物車中不存在該商品，建立新項目");
            CartItem newItem = cartItemService.createCartItem(cart, product, quantity);
            cartItemDAO.save(newItem);
            return newItem;
        }
    }

    /**
     * 從購物車移除商品
     * @param cartId 購物車ID
     * @param productId 商品ID
     * @return 更新後的購物車
     */
    @Override
    public Cart removeProductFromCart(Long cartId, Long productId) {
        logger.info("從購物車移除商品 - cartId: {}, productId: {}", cartId, productId);
        
        CartItem cartItem = cartItemDAO.findByCartIdAndProductId(cartId, productId);
        if (cartItem == null) {
            logger.warn("購物車項目不存在 - cartId: {}, productId: {}", cartId, productId);
            throw new IllegalArgumentException("購物車項目不存在");
        }
        
        cartItemDAO.delete(cartItem);
        
        Cart updatedCart = cartDAO.findById(cartId);
        logger.info("商品移除成功 - cartId: {}, productId: {}", cartId, productId);
        return updatedCart;
    }

    /**
     * 更新購物車中的商品數量
     * @param cartId 購物車ID
     * @param productId 商品ID
     * @param quantity 新數量
     * @return 更新後的購物車項目
     */
    @Override
    public CartItem updateCartItemQuantity(Long cartId, Long productId, Integer quantity) {
        logger.info("更新購物車商品數量 - cartId: {}, productId: {}, newQuantity: {}", 
                   cartId, productId, quantity);
        
        if (quantity <= 0) {
            logger.warn("商品數量必須大於0 - quantity: {}", quantity);
            throw new IllegalArgumentException("商品數量必須大於0");
        }
        
        CartItem cartItem = cartItemDAO.findByCartIdAndProductId(cartId, productId);
        if (cartItem == null) {
            logger.warn("購物車項目不存在 - cartId: {}, productId: {}", cartId, productId);
            throw new IllegalArgumentException("購物車項目不存在");
        }
        
        // 檢查庫存
        if (!cartItemService.checkStockAvailability(productId, quantity)) {
            logger.warn("商品庫存不足 - productId: {}, requestedQuantity: {}", productId, quantity);
            throw new IllegalStateException("商品庫存不足");
        }
        
        cartItem.updateQuantity(quantity);
        CartItem updatedItem = cartItemDAO.save(cartItem);
        
        logger.info("購物車商品數量更新成功 - cartItemId: {}, newQuantity: {}", 
                   updatedItem.getId(), quantity);
        return updatedItem;
    }

    /**
     * 取得購物車的總金額
     * @param cartId 購物車ID
     * @return 購物車總金額
     */
    @Override
    @Transactional(readOnly = true)
    public BigDecimal getCartTotalAmount(Long cartId) {
        logger.debug("取得購物車總金額 - cartId: {}", cartId);
        
        Cart cart = cartDAO.findById(cartId);
        if (cart == null) {
            logger.warn("購物車不存在 - cartId: {}", cartId);
            return BigDecimal.ZERO;
        }
        
        // 強制初始化所有 lazy 關聯
        Hibernate.initialize(cart.getCartItems());
        if (cart.getCartItems() != null) {
            for (CartItem item : cart.getCartItems()) {
                if (item.getProduct() != null) {
                    Hibernate.initialize(item.getProduct());
                    item.getProduct().getPrice(); // 確保 price 已初始化
                }
            }
        }
        
        return cart.getTotalAmount();
    }

    /**
     * 取得購物車的總商品數量
     * @param cartId 購物車ID
     * @return 購物車總商品數量
     */
    @Override
    @Transactional(readOnly = true)
    public Integer getCartTotalItems(Long cartId) {
        logger.debug("取得購物車總商品數量 - cartId: {}", cartId);
        
        Cart cart = cartDAO.findById(cartId);
        if (cart == null) {
            logger.warn("購物車不存在 - cartId: {}", cartId);
            return 0;
        }
        
        // 強制初始化所有 lazy 關聯
        Hibernate.initialize(cart.getCartItems());
        if (cart.getCartItems() != null) {
            for (CartItem item : cart.getCartItems()) {
                if (item.getProduct() != null) {
                    Hibernate.initialize(item.getProduct());
                }
            }
        }
        
        return cart.getTotalItems();
    }

    /**
     * 檢查購物車是否為空
     * @param cartId 購物車ID
     * @return 如果購物車為空返回true，否則返回false
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isCartEmpty(Long cartId) {
        logger.debug("檢查購物車是否為空 - cartId: {}", cartId);
        
        Cart cart = cartDAO.findById(cartId);
        if (cart == null) {
            logger.warn("購物車不存在 - cartId: {}", cartId);
            return true;
        }
        
        boolean isEmpty = cart.isEmpty();
        logger.debug("購物車空值檢查結果 - cartId: {}, isEmpty: {}", cartId, isEmpty);
        return isEmpty;
    }
}
