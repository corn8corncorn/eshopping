package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Product;

/**
 * 商品服務層介面
 * 封裝商品相關的業務邏輯
 */
public interface ProductService {

	/**
	 * 取得所有商品
	 */
	List<Product> getAll();

	/**
	 * 依 ID 取得商品
	 */
	Product getById(Long id);

	/**
	 * 新增商品
	 */
	void saveProduct(Product product);

	/**
	 * 更新商品
	 */
	void updateProduct(Long id, Product product);

	/**
	 * 刪除商品
	 */
	void deleteProduct(Long id);

}
