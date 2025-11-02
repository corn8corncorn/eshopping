package com.example.demo.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.ProductDAO;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

/**
 * 商品服務層實作類別
 * 負責處理商品相關的業務邏輯
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	private ProductDAO productRepository;

	/**
	 * 取得所有商品
	 * 
	 * @return 所有商品的列表
	 */
	@Override
	public List<Product> getAll() {
		logger.debug("開始取得所有商品");
		List<Product> products = productRepository.getAll();
		logger.info("成功取得 {} 個商品", products.size());
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
		logger.debug("開始取得商品 - productId: {}", id);
		Product product = productRepository.getById(id);
		logger.debug("商品取得結果 - productId: {}, found: {}, name: {}", id, product != null, 
                    product != null ? product.getName() : "N/A");
		return product;
	}

	/**
	 * 新增商品
	 * 
	 * @param product 商品物件
	 */
	@Override
	public void saveProduct(Product product) {
		logger.info("開始儲存新商品 - name: {}, type: {}, price: {}", 
                   product.getName(), product.getType(), product.getPrice());
		productRepository.save(product);
		logger.info("商品儲存成功 - productId: {}, name: {}", product.getId(), product.getName());
	}

	/**
	 * 更新商品
	 * 根據 ID 查找現有商品並更新資料
	 * 
	 * @param id 商品 ID
	 * @param updateProduct 包含新商品資料的物件
	 */
	@Override
	public void updateProduct(Long id, Product updateProduct) {
		logger.info("開始更新商品 - productId: {}", id);
		logger.debug("更新資料 - name: {}, stockQuantity: {}, status: {}", 
		             updateProduct.getName(), updateProduct.getStockQuantity(), updateProduct.getStatus());
		
		Product existingProduct = productRepository.getById(id);
		if (existingProduct != null) {
			logger.debug("找到現有商品 - name: {}, stockQuantity: {}, status: {}", 
			             existingProduct.getName(), existingProduct.getStockQuantity(), existingProduct.getStatus());
			
			// 直接更新所有欄位，確保 stockQuantity 被正確設置
			existingProduct.setName(updateProduct.getName());
			existingProduct.setType(updateProduct.getType());
			existingProduct.setPrice(updateProduct.getPrice());
			
			// 更新庫存數量（這會自動更新狀態）
			Integer newStockQuantity = updateProduct.getStockQuantity();
			logger.debug("準備更新庫存數量 - 從 {} 改為 {}", existingProduct.getStockQuantity(), newStockQuantity);
			
			if (newStockQuantity != null) {
				existingProduct.setStockQuantity(newStockQuantity);
				logger.debug("庫存數量已設置為: {}", existingProduct.getStockQuantity());
			} else {
				logger.warn("庫存數量為 null，保持原值: {}", existingProduct.getStockQuantity());
			}
			
			existingProduct.setDescription(updateProduct.getDescription());
			existingProduct.setImageUrl(updateProduct.getImageUrl());
			
			// 最後更新狀態（如果用戶手動選擇了狀態，則使用用戶選擇的）
			existingProduct.setStatus(updateProduct.getStatus());
			
			// 驗證更新後的值
			logger.debug("更新完成，準備保存 - stockQuantity: {}", existingProduct.getStockQuantity());
			
			// 保存到資料庫
			productRepository.save(existingProduct);
			
			logger.info("商品更新成功 - productId: {}, name: {}, stockQuantity: {}, status: {}", 
			            id, existingProduct.getName(), existingProduct.getStockQuantity(), existingProduct.getStatus());
		} else {
			logger.warn("商品不存在 - productId: {}", id);
		}
	}

	/**
	 * 刪除商品
	 * 
	 * @param id 商品 ID
	 */
	@Override
	public void deleteProduct(Long id) {
		logger.info("開始刪除商品 - productId: {}", id);
		productRepository.delete(id);
		logger.info("商品刪除成功 - productId: {}", id);
	}
}
