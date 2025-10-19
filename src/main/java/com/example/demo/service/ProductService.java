package com.example.demo.service;

import java.util.List;
import com.example.demo.model.Product;

public interface ProductService {
	
	List<Product> getAllProducts();
	
	Product getProductById(Long id);

	void saveProduct(Product product);
	
	void updateProduct(Long id, Product product);
	
	void deletProduct(Long id);

}
