package com.example.demo.dao.impl;

import com.example.demo.dao.ProductDAO;
import com.example.demo.model.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDAOImpl implements ProductDAO{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getCurrrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public List<Product> findAll() {
		return getCurrrentSession().createQuery("FROM Product", Product.class).list();
	}
	
	@Override
	public Product findById(Long id) {
		return getCurrrentSession().get(Product.class, id);
	}
	
	@Override
	public void save(Product product) {
		getCurrrentSession().saveOrUpdate(product);
	}
	
	@Override
	public void delete(Long id) {
		Product product = getCurrrentSession().get(Product.class, id);
		if (product != null) {
			getCurrrentSession().delete(product);
		}
	}
}
