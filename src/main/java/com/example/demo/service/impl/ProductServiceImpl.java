package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.ProductDAO;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDAO productRepository;

	@Override
	public List<Product> getAll() {
		// 取得所有商品
		return productRepository.getAll();
	}

	@Override
	public Product getById(Long id) {
		// 依 ID 取得商品
		return productRepository.getById(id);
	}

	@Override
	public void saveProduct(Product product) {
		// 新增商品
		productRepository.save(product);
	}

	@Override
	public void updateProduct(Long id, Product updateProduct) {
		Product existingProduct = productRepository.getById(id);
		if (existingProduct != null) {
			existingProduct.setName(updateProduct.getName());
			existingProduct.setType(updateProduct.getType());
			existingProduct.setPrice(updateProduct.getPrice());
			existingProduct.setDescription(updateProduct.getDescription());
			existingProduct.setImageUrl(updateProduct.getImageUrl());
			existingProduct.setStatus(updateProduct.getStatus());
			productRepository.save(existingProduct);
		}
	}

	@Override
	public void deleteProduct(Long id) {
		// 刪除商品
		productRepository.delete(id);
	}
}
