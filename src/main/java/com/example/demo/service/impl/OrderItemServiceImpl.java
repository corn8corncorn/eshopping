package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.OrderItemDAO;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;
import com.example.demo.service.OrderItemService;

/**
 * 訂單項目服務層實作類
 * 實作訂單項目相關的業務邏輯
 */
@Service
@Transactional
public class OrderItemServiceImpl implements OrderItemService {

    private static final Logger logger = LoggerFactory.getLogger(OrderItemServiceImpl.class);

    @Autowired
    private OrderItemDAO orderItemDAO;

    /**
     * 取得所有訂單項目
     * @return 所有訂單項目的列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderItem> getAll() {
        logger.info("取得所有訂單項目");
        List<OrderItem> orderItems = orderItemDAO.findAll();
        logger.debug("取得訂單項目數量: {}", orderItems.size());
        return orderItems;
    }

    /**
     * 根據ID取得訂單項目
     * @param id 訂單項目ID
     * @return 訂單項目物件，如果不存在則返回null
     */
    @Override
    @Transactional(readOnly = true)
    public OrderItem getById(Long id) {
        logger.info("根據ID取得訂單項目 - orderItemId: {}", id);
        OrderItem orderItem = orderItemDAO.findById(id);
        logger.debug("訂單項目查詢結果 - orderItemId: {}, found: {}", id, orderItem != null);
        return orderItem;
    }

    /**
     * 根據訂單取得所有訂單項目
     * @param order 訂單
     * @return 該訂單的所有項目列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderItem> getByOrder(Order order) {
        logger.info("根據訂單取得訂單項目 - orderId: {}", order.getId());
        List<OrderItem> orderItems = orderItemDAO.findByOrder(order);
        logger.debug("訂單項目查詢結果 - orderId: {}, itemCount: {}", order.getId(), orderItems.size());
        return orderItems;
    }

    /**
     * 根據訂單ID取得所有訂單項目
     * @param orderId 訂單ID
     * @return 該訂單的所有項目列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderItem> getByOrderId(Long orderId) {
        logger.info("根據訂單ID取得訂單項目 - orderId: {}", orderId);
        List<OrderItem> orderItems = orderItemDAO.findByOrderId(orderId);
        logger.debug("訂單項目查詢結果 - orderId: {}, itemCount: {}", orderId, orderItems.size());
        return orderItems;
    }

    /**
     * 根據商品取得所有訂單項目
     * @param product 商品
     * @return 包含該商品的所有訂單項目列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderItem> getByProduct(Product product) {
        logger.info("根據商品取得訂單項目 - productId: {}", product.getId());
        List<OrderItem> orderItems = orderItemDAO.findByProduct(product);
        logger.debug("商品訂單項目查詢結果 - productId: {}, itemCount: {}", product.getId(), orderItems.size());
        return orderItems;
    }

    /**
     * 根據商品ID取得所有訂單項目
     * @param productId 商品ID
     * @return 包含該商品的所有訂單項目列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderItem> getByProductId(Long productId) {
        logger.info("根據商品ID取得訂單項目 - productId: {}", productId);
        List<OrderItem> orderItems = orderItemDAO.findByProductId(productId);
        logger.debug("商品訂單項目查詢結果 - productId: {}, itemCount: {}", productId, orderItems.size());
        return orderItems;
    }

    /**
     * 建立新的訂單項目
     * @param order 所屬訂單
     * @param product 商品
     * @param quantity 數量
     * @return 新建立的訂單項目
     */
    @Override
    public OrderItem createOrderItem(Order order, Product product, Integer quantity) {
        logger.info("建立新訂單項目 - orderId: {}, productId: {}, quantity: {}", 
                   order.getId(), product.getId(), quantity);
        
        if (quantity <= 0) {
            logger.warn("訂單項目數量必須大於0 - quantity: {}", quantity);
            throw new IllegalArgumentException("訂單項目數量必須大於0");
        }
        
        if (!checkStockAvailability(product.getId(), quantity)) {
            logger.warn("商品庫存不足 - productId: {}, requestedQuantity: {}, availableStock: {}", 
                       product.getId(), quantity, product.getStockQuantity());
            throw new IllegalStateException("商品庫存不足");
        }
        
        OrderItem orderItem = new OrderItem(order, product, quantity);
        OrderItem savedOrderItem = orderItemDAO.save(orderItem);
        
        logger.info("訂單項目建立成功 - orderItemId: {}, orderId: {}, productId: {}", 
                   savedOrderItem.getId(), order.getId(), product.getId());
        return savedOrderItem;
    }

    /**
     * 儲存訂單項目
     * @param orderItem 要儲存的訂單項目
     * @return 儲存後的訂單項目
     */
    @Override
    public OrderItem saveOrderItem(OrderItem orderItem) {
        logger.info("儲存訂單項目 - orderItemId: {}, orderId: {}, productId: {}", 
                   orderItem.getId(), orderItem.getOrder().getId(), orderItem.getProduct().getId());
        OrderItem savedOrderItem = orderItemDAO.save(orderItem);
        logger.debug("訂單項目儲存成功 - orderItemId: {}", savedOrderItem.getId());
        return savedOrderItem;
    }

    /**
     * 更新訂單項目
     * @param id 訂單項目ID
     * @param orderItem 要更新的訂單項目
     * @return 更新後的訂單項目
     */
    @Override
    public OrderItem updateOrderItem(Long id, OrderItem orderItem) {
        logger.info("更新訂單項目 - orderItemId: {}", id);
        
        OrderItem existingOrderItem = orderItemDAO.findById(id);
        if (existingOrderItem == null) {
            logger.warn("訂單項目不存在 - orderItemId: {}", id);
            throw new IllegalArgumentException("訂單項目不存在: " + id);
        }
        
        if (!canModifyOrderItem(id)) {
            logger.warn("訂單項目無法修改 - orderItemId: {}", id);
            throw new IllegalStateException("訂單項目無法修改");
        }
        
        // 更新訂單項目資訊
        existingOrderItem.setQuantity(orderItem.getQuantity());
        existingOrderItem.setUnitPrice(orderItem.getUnitPrice());
        existingOrderItem.setProductName(orderItem.getProductName());
        existingOrderItem.setProductDescription(orderItem.getProductDescription());
        existingOrderItem.setProductImageUrl(orderItem.getProductImageUrl());
        
        OrderItem updatedOrderItem = orderItemDAO.save(existingOrderItem);
        logger.info("訂單項目更新成功 - orderItemId: {}", updatedOrderItem.getId());
        return updatedOrderItem;
    }

    /**
     * 刪除訂單項目
     * @param id 要刪除的訂單項目ID
     */
    @Override
    public void deleteOrderItem(Long id) {
        logger.info("刪除訂單項目 - orderItemId: {}", id);
        
        OrderItem orderItem = orderItemDAO.findById(id);
        if (orderItem == null) {
            logger.warn("要刪除的訂單項目不存在 - orderItemId: {}", id);
            throw new IllegalArgumentException("訂單項目不存在: " + id);
        }
        
        orderItemDAO.delete(id);
        logger.info("訂單項目刪除成功 - orderItemId: {}", id);
    }

    /**
     * 更新訂單項目數量
     * @param orderItemId 訂單項目ID
     * @param quantity 新數量
     * @return 更新後的訂單項目
     */
    @Override
    public OrderItem updateQuantity(Long orderItemId, Integer quantity) {
        logger.info("更新訂單項目數量 - orderItemId: {}, newQuantity: {}", orderItemId, quantity);
        
        if (quantity <= 0) {
            logger.warn("訂單項目數量必須大於0 - quantity: {}", quantity);
            throw new IllegalArgumentException("訂單項目數量必須大於0");
        }
        
        OrderItem orderItem = orderItemDAO.findById(orderItemId);
        if (orderItem == null) {
            logger.warn("訂單項目不存在 - orderItemId: {}", orderItemId);
            throw new IllegalArgumentException("訂單項目不存在: " + orderItemId);
        }
        
        if (!canModifyOrderItem(orderItemId)) {
            logger.warn("訂單項目無法修改 - orderItemId: {}", orderItemId);
            throw new IllegalStateException("訂單項目無法修改");
        }
        
        // 檢查庫存
        if (!checkStockAvailability(orderItem.getProduct().getId(), quantity)) {
            logger.warn("商品庫存不足 - productId: {}, requestedQuantity: {}", 
                       orderItem.getProduct().getId(), quantity);
            throw new IllegalStateException("商品庫存不足");
        }
        
        orderItem.updateQuantity(quantity);
        OrderItem updatedOrderItem = orderItemDAO.save(orderItem);
        
        logger.info("訂單項目數量更新成功 - orderItemId: {}, newQuantity: {}", orderItemId, quantity);
        return updatedOrderItem;
    }

    /**
     * 更新訂單項目單價
     * @param orderItemId 訂單項目ID
     * @param unitPrice 新單價
     * @return 更新後的訂單項目
     */
    @Override
    public OrderItem updateUnitPrice(Long orderItemId, BigDecimal unitPrice) {
        logger.info("更新訂單項目單價 - orderItemId: {}, newUnitPrice: {}", orderItemId, unitPrice);
        
        if (unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("訂單項目單價必須大於0 - unitPrice: {}", unitPrice);
            throw new IllegalArgumentException("訂單項目單價必須大於0");
        }
        
        OrderItem orderItem = orderItemDAO.findById(orderItemId);
        if (orderItem == null) {
            logger.warn("訂單項目不存在 - orderItemId: {}", orderItemId);
            throw new IllegalArgumentException("訂單項目不存在: " + orderItemId);
        }
        
        if (!canModifyOrderItem(orderItemId)) {
            logger.warn("訂單項目無法修改 - orderItemId: {}", orderItemId);
            throw new IllegalStateException("訂單項目無法修改");
        }
        
        orderItem.updateUnitPrice(unitPrice);
        OrderItem updatedOrderItem = orderItemDAO.save(orderItem);
        
        logger.info("訂單項目單價更新成功 - orderItemId: {}, newUnitPrice: {}", orderItemId, unitPrice);
        return updatedOrderItem;
    }

    /**
     * 重新計算訂單項目小計
     * @param orderItemId 訂單項目ID
     * @return 更新後的訂單項目
     */
    @Override
    public OrderItem recalculateSubtotal(Long orderItemId) {
        logger.info("重新計算訂單項目小計 - orderItemId: {}", orderItemId);
        
        OrderItem orderItem = orderItemDAO.findById(orderItemId);
        if (orderItem == null) {
            logger.warn("訂單項目不存在 - orderItemId: {}", orderItemId);
            throw new IllegalArgumentException("訂單項目不存在: " + orderItemId);
        }
        
        orderItem.calculateSubtotal();
        OrderItem updatedOrderItem = orderItemDAO.save(orderItem);
        
        logger.info("訂單項目小計重新計算完成 - orderItemId: {}, subtotal: {}", 
                   orderItemId, updatedOrderItem.getSubtotal());
        return updatedOrderItem;
    }

    /**
     * 批量更新訂單項目
     * @param orderItems 要更新的訂單項目列表
     * @return 更新後的訂單項目列表
     */
    @Override
    public List<OrderItem> updateOrderItems(List<OrderItem> orderItems) {
        logger.info("批量更新訂單項目 - count: {}", orderItems.size());
        
        for (OrderItem orderItem : orderItems) {
            if (!canModifyOrderItem(orderItem.getId())) {
                logger.warn("訂單項目無法修改 - orderItemId: {}", orderItem.getId());
                throw new IllegalStateException("訂單項目無法修改: " + orderItem.getId());
            }
        }
        
        List<OrderItem> updatedOrderItems = orderItemDAO.saveAll(orderItems);
        logger.info("批量更新訂單項目成功 - count: {}", updatedOrderItems.size());
        return updatedOrderItems;
    }

    /**
     * 批量刪除訂單項目
     * @param orderItemIds 要刪除的訂單項目ID列表
     */
    @Override
    public void deleteOrderItems(List<Long> orderItemIds) {
        logger.info("批量刪除訂單項目 - count: {}", orderItemIds.size());
        
        for (Long orderItemId : orderItemIds) {
            OrderItem orderItem = orderItemDAO.findById(orderItemId);
            if (orderItem == null) {
                logger.warn("要刪除的訂單項目不存在 - orderItemId: {}", orderItemId);
                throw new IllegalArgumentException("訂單項目不存在: " + orderItemId);
            }
        }
        
        orderItemDAO.deleteAllById(orderItemIds);
        logger.info("批量刪除訂單項目成功 - count: {}", orderItemIds.size());
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

    /**
     * 檢查訂單項目是否可以修改
     * @param orderItemId 訂單項目ID
     * @return 如果可以修改返回true，否則返回false
     */
    @Override
    @Transactional(readOnly = true)
    public boolean canModifyOrderItem(Long orderItemId) {
        logger.debug("檢查訂單項目是否可以修改 - orderItemId: {}", orderItemId);
        
        OrderItem orderItem = orderItemDAO.findById(orderItemId);
        if (orderItem == null) {
            logger.warn("訂單項目不存在 - orderItemId: {}", orderItemId);
            return false;
        }
        
        Order order = orderItem.getOrder();
        boolean canModify = order.getStatus() == Order.OrderStatus.PENDING;
        
        logger.debug("訂單項目修改檢查結果 - orderItemId: {}, orderStatus: {}, canModify: {}", 
                    orderItemId, order.getStatus(), canModify);
        return canModify;
    }

    /**
     * 取得訂單項目的商品銷售統計
     * @param productId 商品ID
     * @return 商品銷售統計資訊
     */
    @Override
    @Transactional(readOnly = true)
    public ProductSalesStatistics getProductSalesStatistics(Long productId) {
        logger.info("取得商品銷售統計 - productId: {}", productId);
        
        List<OrderItem> orderItems = orderItemDAO.findByProductId(productId);
        
        long totalOrders = orderItems.size();
        int totalQuantity = orderItems.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
        
        BigDecimal totalRevenue = orderItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal averagePrice = totalQuantity > 0 ? 
                totalRevenue.divide(BigDecimal.valueOf(totalQuantity), 2, BigDecimal.ROUND_HALF_UP) : 
                BigDecimal.ZERO;
        
        ProductSalesStatistics statistics = new ProductSalesStatistics(
                totalOrders, totalQuantity, totalRevenue, averagePrice);
        
        logger.info("商品銷售統計完成 - productId: {}, totalOrders: {}, totalQuantity: {}, totalRevenue: {}", 
                   productId, totalOrders, totalQuantity, totalRevenue);
        return statistics;
    }

    /**
     * 取得訂單的總項目數
     * @param orderId 訂單ID
     * @return 訂單的總項目數
     */
    @Override
    @Transactional(readOnly = true)
    public Integer getTotalItemCount(Long orderId) {
        logger.debug("取得訂單總項目數 - orderId: {}", orderId);
        
        List<OrderItem> orderItems = orderItemDAO.findByOrderId(orderId);
        int totalCount = orderItems.size();
        
        logger.debug("訂單總項目數 - orderId: {}, totalCount: {}", orderId, totalCount);
        return totalCount;
    }

    /**
     * 取得訂單的總商品數量
     * @param orderId 訂單ID
     * @return 訂單的總商品數量
     */
    @Override
    @Transactional(readOnly = true)
    public Integer getTotalQuantity(Long orderId) {
        logger.debug("取得訂單總商品數量 - orderId: {}", orderId);
        
        List<OrderItem> orderItems = orderItemDAO.findByOrderId(orderId);
        int totalQuantity = orderItems.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
        
        logger.debug("訂單總商品數量 - orderId: {}, totalQuantity: {}", orderId, totalQuantity);
        return totalQuantity;
    }
}
