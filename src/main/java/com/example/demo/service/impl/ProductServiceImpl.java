package com.example.demo.service.impl;

import com.example.demo.dao.ProductDAO;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDAO productReposity;

	@Override
	public List<Product> getAllProducts() {
		return productReposity.findAll();
	}

	@Override
	public Product getProductById(Long id) {
		return productReposity.findById(id);
	}

	@Override
	public void saveProduct(Product product) {
		productReposity.save(product);
	}

	@Override
	public void updateProduct(Long id, Product updateProduct) {
		Product existingProduct = productReposity.findById(id);
		if (existingProduct != null) {
			existingProduct.setName(updateProduct.getName());
			existingProduct.setType(updateProduct.getType());
			existingProduct.setPrice(updateProduct.getPrice());
			productReposity.save(existingProduct);
		}
	}

	@Override
	public void deletProduct(Long id) {
		productReposity.delete(id);
	}
}
