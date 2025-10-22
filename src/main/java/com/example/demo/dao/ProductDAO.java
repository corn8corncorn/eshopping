package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.Product;

/**
 * 商品資料存取層 DAO 介面
 * 定義對商品的基本 CRUD 操作
 */
public interface ProductDAO {

    /**
     * 取得所有商品
     * @return 商品清單
     */
    List<Product> getAll();

    /**
     * 依 ID 取得商品
     * @param id 商品 ID
     * @return 商品，若不存在則為 null
     */
    Product getById(Long id);

    /**
     * 新增或更新商品
     * @param product 商品實體
     */
    void save(Product product);

    /**
     * 依 ID 刪除商品
     * @param id 商品 ID
     */
    void delete(Long id);
}
