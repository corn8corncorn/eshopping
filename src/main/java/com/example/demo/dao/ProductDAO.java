package com.example.demo.dao;

import java.util.List;
import com.example.demo.model.Product;

public interface ProductDAO {

	List<Product> findAll();
	
	Product findById(Long id);
	
	void save(Product product);
	
	void delete(Long id);
}
