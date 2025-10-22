package com.example.demo.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.ProductDAO;
import com.example.demo.model.Product;

@Repository
public class ProductDAOImpl implements ProductDAO{

	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * 取得目前的 Hibernate Session
	 */
	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public List<Product> getAll() {
		// 查詢所有商品
		return getCurrentSession().createQuery("FROM Product", Product.class).list();
	}

	@Override
	public Product getById(Long id) {
		// 依 ID 取得商品
		return getCurrentSession().get(Product.class, id);
	}

	@Override
	public void save(Product product) {
		// 新增或更新商品
		getCurrentSession().saveOrUpdate(product);
	}

	@Override
	public void delete(Long id) {
		Product product = getCurrentSession().get(Product.class, id);
		if (product != null) {
			getCurrentSession().delete(product);
		}
	}
}
