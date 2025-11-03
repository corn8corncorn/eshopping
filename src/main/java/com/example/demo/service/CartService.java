package com.example.demo.service;

import java.math.BigDecimal;
import java.util.List;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Customer;
import com.example.demo.model.Product;

/**
 * 購物車服務層介面
 * 封裝購物車相關的業務邏輯
 */
public interface CartService {

    /**
     * 取得所有購物車
     * @return 所有購物車的列表
     */
    List<Cart> getAll();

    /**
     * 根據ID取得購物車
     * @param id 購物車ID
     * @return 購物車物件，如果不存在則返回null
     */
    Cart getById(Long id);

    /**
     * 根據客戶取得購物車
     * @param customer 客戶
     * @return 該客戶的購物車，如果不存在則返回null
     */
    Cart getByCustomer(Customer customer);

    /**
     * 根據客戶ID取得購物車
     * @param customerId 客戶ID
     * @return 該客戶的購物車，如果不存在則返回null
     */
    Cart getByCustomerId(Long customerId);

    /**
     * 為客戶建立或取得購物車
     * @param customer 客戶
     * @return 購物車物件
     */
    Cart getOrCreateCart(Customer customer);

    /**
     * 為客戶建立或取得購物車
     * @param customerId 客戶ID
     * @return 購物車物件
     */
    Cart getOrCreateCart(Long customerId);

    /**
     * 建立新購物車
     * @param customer 客戶
     * @return 新建立的購物車
     */
    Cart createCart(Customer customer);

    /**
     * 儲存購物車
     * @param cart 要儲存的購物車
     * @return 儲存後的購物車
     */
    Cart saveCart(Cart cart);

    /**
     * 更新購物車
     * @param id 購物車ID
     * @param cart 要更新的購物車
     * @return 更新後的購物車
     */
    Cart updateCart(Long id, Cart cart);

    /**
     * 刪除購物車
     * @param id 要刪除的購物車ID
     */
    void deleteCart(Long id);

    /**
     * 清空購物車
     * @param cartId 購物車ID
     * @return 清空後的購物車
     */
    Cart clearCart(Long cartId);

    /**
     * 添加商品到購物車
     * @param cartId 購物車ID
     * @param product 商品
     * @param quantity 數量
     * @return 更新後的購物車項目
     */
    CartItem addProductToCart(Long cartId, Product product, Integer quantity);

    /**
     * 從購物車移除商品
     * @param cartId 購物車ID
     * @param productId 商品ID
     * @return 更新後的購物車
     */
    Cart removeProductFromCart(Long cartId, Long productId);

    /**
     * 更新購物車中的商品數量
     * @param cartId 購物車ID
     * @param productId 商品ID
     * @param quantity 新數量
     * @return 更新後的購物車項目
     */
    CartItem updateCartItemQuantity(Long cartId, Long productId, Integer quantity);

    /**
     * 取得購物車的總金額
     * @param cartId 購物車ID
     * @return 購物車總金額
     */
    BigDecimal getCartTotalAmount(Long cartId);

    /**
     * 取得購物車的總商品數量
     * @param cartId 購物車ID
     * @return 購物車總商品數量
     */
    Integer getCartTotalItems(Long cartId);
    

    /**
     * 檢查購物車是否為空
     * @param cartId 購物車ID
     * @return 如果購物車為空返回true，否則返回false
     */
    boolean isCartEmpty(Long cartId);
}
