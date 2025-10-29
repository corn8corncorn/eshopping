package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;

/**
 * 購物車項目資料存取物件介面
 * 定義購物車項目相關的資料庫操作
 */
public interface CartItemDAO {

    /**
     * 儲存購物車項目
     * @param cartItem 要儲存的購物車項目
     * @return 儲存後的購物車項目
     */
    CartItem save(CartItem cartItem);

    /**
     * 批量儲存購物車項目
     * @param cartItems 要儲存的購物車項目列表
     * @return 儲存後的購物車項目列表
     */
    List<CartItem> saveAll(List<CartItem> cartItems);

    /**
     * 根據ID查找購物車項目
     * @param id 購物車項目ID
     * @return 購物車項目物件，如果不存在則返回null
     */
    CartItem findById(Long id);

    /**
     * 查找所有購物車項目
     * @return 所有購物車項目的列表
     */
    List<CartItem> findAll();

    /**
     * 根據購物車查找購物車項目
     * @param cart 購物車
     * @return 該購物車的所有項目列表
     */
    List<CartItem> findByCart(Cart cart);

    /**
     * 根據購物車ID查找購物車項目
     * @param cartId 購物車ID
     * @return 該購物車的所有項目列表
     */
    List<CartItem> findByCartId(Long cartId);

    /**
     * 根據商品查找購物車項目
     * @param product 商品
     * @return 包含該商品的所有購物車項目列表
     */
    List<CartItem> findByProduct(Product product);

    /**
     * 根據商品ID查找購物車項目
     * @param productId 商品ID
     * @return 包含該商品的所有購物車項目列表
     */
    List<CartItem> findByProductId(Long productId);

    /**
     * 根據購物車和商品查找購物車項目
     * @param cart 購物車
     * @param product 商品
     * @return 購物車項目物件，如果不存在則返回null
     */
    CartItem findByCartAndProduct(Cart cart, Product product);

    /**
     * 根據購物車ID和商品ID查找購物車項目
     * @param cartId 購物車ID
     * @param productId 商品ID
     * @return 購物車項目物件，如果不存在則返回null
     */
    CartItem findByCartIdAndProductId(Long cartId, Long productId);

    /**
     * 刪除購物車項目
     * @param id 要刪除的購物車項目ID
     */
    void delete(Long id);

    /**
     * 刪除購物車項目
     * @param cartItem 要刪除的購物車項目物件
     */
    void delete(CartItem cartItem);

    /**
     * 批量刪除購物車項目
     * @param ids 要刪除的購物車項目ID列表
     */
    void deleteAllById(List<Long> ids);

    /**
     * 根據購物車刪除所有購物車項目
     * @param cartId 購物車ID
     */
    void deleteAllByCartId(Long cartId);

    /**
     * 檢查購物車項目是否存在
     * @param id 購物車項目ID
     * @return 如果存在返回true，否則返回false
     */
    boolean existsById(Long id);

    /**
     * 計算購物車項目總數
     * @return 購物車項目總數
     */
    long count();

    /**
     * 根據購物車ID計算購物車項目總數
     * @param cartId 購物車ID
     * @return 該購物車的項目總數
     */
    long countByCartId(Long cartId);

    /**
     * 根據購物車ID計算總商品數量
     * @param cartId 購物車ID
     * @return 該購物車的總商品數量
     */
    int sumQuantityByCartId(Long cartId);
}
