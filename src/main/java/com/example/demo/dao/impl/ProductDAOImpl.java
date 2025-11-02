package com.example.demo.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.ProductDAO;
import com.example.demo.model.Product;

/**
 * 商品資料存取層實作類別
 * 負責與資料庫進行商品相關的資料操作
 */
@Repository
public class ProductDAOImpl implements ProductDAO {

	private static final Logger logger = LoggerFactory.getLogger(ProductDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * 取得目前的 Hibernate Session
	 * 
	 * @return 目前的 Hibernate Session
	 */
	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 取得所有商品
	 * 
	 * @return 所有商品的列表
	 */
	@Override
	public List<Product> getAll() {
		logger.debug("從資料庫查詢所有商品");
		List<Product> products = getCurrentSession().createQuery("FROM Product", Product.class).list();
		logger.info("成功查詢到 {} 個商品", products.size());
		return products;
	}

	/**
	 * 依 ID 取得商品
	 * 
	 * @param id 商品 ID
	 * @return 商品物件，如果不存在則返回null
	 */
	@Override
	public Product getById(Long id) {
		logger.debug("從資料庫取得商品 - productId: {}", id);
		Product product = getCurrentSession().get(Product.class, id);
		logger.debug("商品查詢結果 - productId: {}, found: {}, name: {}, stockQuantity: {}", 
		           id, product != null, product != null ? product.getName() : "N/A",
		           product != null ? product.getStockQuantity() : "N/A");
		return product;
	}

	/**
	 * 新增或更新商品
	 * 
	 * @param product 商品物件
	 */
	@Override
	public void save(Product product) {
		logger.info("開始儲存商品到資料庫 - productId: {}, name: {}, stockQuantity: {}, price: {}",
				   product.getId(), product.getName(), product.getStockQuantity(), product.getPrice());
		
		// 如果是更新現有商品（有 ID），使用 merge 確保更新所有欄位
		if (product.getId() != null) {
			logger.debug("更新現有商品，使用 merge - productId: {}", product.getId());
			Product merged = (Product) getCurrentSession().merge(product);
			logger.debug("商品 merge 完成 - productId: {}, stockQuantity: {}", merged.getId(), merged.getStockQuantity());
		} else {
			// 如果是新商品，使用 save
			logger.debug("新增新商品，使用 save");
			getCurrentSession().save(product);
		}
		
		// 強制刷新 session，確保立即寫入資料庫
		getCurrentSession().flush();
		
		logger.info("商品儲存成功 - productId: {}, name: {}, stockQuantity: {}", 
		           product.getId(), product.getName(), product.getStockQuantity());
	}

	/**
	 * 依 ID 刪除商品
	 * 
	 * @param id 商品 ID
	 */
	@Override
	public void delete(Long id) {
		logger.info("開始從資料庫刪除商品 - productId: {}", id);
		Product product = getCurrentSession().get(Product.class, id);
		if (product != null) {
			getCurrentSession().delete(product);
			logger.info("商品刪除成功 - productId: {}", id);
		} else {
			logger.warn("要刪除的商品不存在 - productId: {}", id);
		}
	}
}
