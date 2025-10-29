package com.example.demo.service;

import java.math.BigDecimal;
import java.util.List;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;

/**
 * 購物車項目服務層介面
 * 封裝購物車項目相關的業務邏輯
 */
public interface CartItemService {

    /**
     * 取得所有購物車項目
     * @return 所有購物車項目的列表
     */
    List<CartItem> getAll();

    /**
     * 根據ID取得購物車項目
     * @param id 購物車項目ID
     * @return 購物車項目物件，如果不存在則返回null
     */
    CartItem getById(Long id);

    /**
     * 根據購物車取得所有購物車項目
     * @param cart 購物車
     * @return 該購物車的所有項目列表
     */
    List<CartItem> getByCart(Cart cart);

    /**
     * 根據購物車ID取得所有購物車項目
     * @param cartId 購物車ID
     * @return 該購物車的所有項目列表
     */
    List<CartItem> getByCartId(Long cartId);

    /**
     * 根據商品取得所有購物車項目
     * @param product 商品
     * @return 包含該商品的所有購物車項目列表
     */
    List<CartItem> getByProduct(Product product);

    /**
     * 根據商品ID取得所有購物車項目
     * @param productId 商品ID
     * @return 包含該商品的所有購物車項目列表
     */
    List<CartItem> getByProductId(Long productId);

    /**
     * 建立新的購物車項目
     * @param cart 所屬購物車
     * @param product 商品
     * @param quantity 數量
     * @return 新建立的購物車項目
     */
    CartItem createCartItem(Cart cart, Product product, Integer quantity);

    /**
     * 儲存購物車項目
     * @param cartItem 要儲存的購物車項目
     * @return 儲存後的購物車項目
     */
    CartItem saveCartItem(CartItem cartItem);

    /**
     * 更新購物車項目
     * @param id 購物車項目ID
     * @param cartItem 要更新的購物車項目
     * @return 更新後的購物車項目
     */
    CartItem updateCartItem(Long id, CartItem cartItem);

    /**
     * 刪除購物車項目
     * @param id 要刪除的購物車項目ID
     */
    void deleteCartItem(Long id);

    /**
     * 更新購物車項目數量
     * @param cartItemId 購物車項目ID
     * @param quantity 新數量
     * @return 更新後的購物車項目
     */
    CartItem updateQuantity(Long cartItemId, Integer quantity);

    /**
     * 更新購物車項目單價
     * @param cartItemId 購物車項目ID
     * @param unitPrice 新單價
     * @return 更新後的購物車項目
     */
    CartItem updateUnitPrice(Long cartItemId, BigDecimal unitPrice);

    /**
     * 重新計算購物車項目小計
     * @param cartItemId 購物車項目ID
     * @return 更新後的購物車項目
     */
    CartItem recalculateSubtotal(Long cartItemId);

    /**
     * 增加購物車項目數量
     * @param cartItemId 購物車項目ID
     * @param increment 增加的數量
     * @return 更新後的購物車項目
     */
    CartItem incrementQuantity(Long cartItemId, Integer increment);

    /**
     * 減少購物車項目數量
     * @param cartItemId 購物車項目ID
     * @param decrement 減少的數量
     * @return 更新後的購物車項目
     */
    CartItem decrementQuantity(Long cartItemId, Integer decrement);

    /**
     * 同步商品資訊（當商品價格或名稱變動時）
     * @param cartItemId 購物車項目ID
     * @return 更新後的購物車項目
     */
    CartItem syncProductInfo(Long cartItemId);

    /**
     * 批量更新購物車項目
     * @param cartItems 要更新的購物車項目列表
     * @return 更新後的購物車項目列表
     */
    List<CartItem> updateCartItems(List<CartItem> cartItems);

    /**
     * 批量刪除購物車項目
     * @param cartItemIds 要刪除的購物車項目ID列表
     */
    void deleteCartItems(List<Long> cartItemIds);

    /**
     * 檢查商品庫存是否足夠
     * @param productId 商品ID
     * @param quantity 需要的數量
     * @return 如果庫存足夠返回true，否則返回false
     */
    boolean checkStockAvailability(Long productId, Integer quantity);
}
