package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

/**
 * 產品管理控制器
 * 負責處理商品相關的 CRUD 操作
 */
@Controller
@RequestMapping("/products")
public class ProductController {

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;

	/**
	 * 顯示商品列表頁面
	 * 獲取所有商品資料並顯示在列表中，支持分頁功能
	 * 
	 * @param page 頁碼（從 0 開始，預設為 0）
	 * @param size 每頁顯示數量（預設為 10）
	 * @param model 用於傳遞商品資料到前端頁面
	 * @return 商品列表頁面模板名稱
	 */
	@GetMapping
	public String listProducts(@RequestParam(value = "page", defaultValue = "0") int page,
	                          @RequestParam(value = "size", defaultValue = "10") int size,
	                          Model model) {
		logger.info("進入商品列表頁面 - page: {}, size: {}", page, size);
		
		List<Product> allProducts = productService.getAll();
		int totalProducts = allProducts.size();
		int totalPages = totalProducts > 0 ? (int) Math.ceil((double) totalProducts / size) : 0;
		
		// 確保頁碼不超出範圍
		if (page < 0) {
			page = 0;
		}
		if (totalPages > 0 && page >= totalPages) {
			page = totalPages - 1;
		}
		
		// 計算當前頁的商品範圍
		List<Product> paginatedProducts;
		if (totalProducts > 0) {
			int start = page * size;
			int end = Math.min(start + size, totalProducts);
			paginatedProducts = allProducts.subList(start, end);
		} else {
			paginatedProducts = java.util.Collections.emptyList();
		}
		
		model.addAttribute("products", paginatedProducts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("totalProducts", totalProducts);
		model.addAttribute("pageSize", size);
		
		logger.info("商品列表載入完成 - 總數: {}, 當前頁: {}/{}, 顯示: {} 筆", 
		            totalProducts, page + 1, totalPages, paginatedProducts.size());
		return "products";
	}

	/**
	 * 顯示新增商品頁面
	 * 準備空白的商品資料供用戶填寫
	 * 
	 * @param model 用於傳遞空白商品資料到前端頁面
	 * @return 新增商品頁面模板名稱
	 */
	@GetMapping("/add")
	public String showAddForm(Model model) {
		logger.info("進入新增商品頁面");
		model.addAttribute("product", new Product());
		return "add-product";
	}

	/**
	 * 顯示編輯商品頁面
	 * 根據商品 ID 獲取商品資料供編輯
	 * 
	 * @param id 商品 ID
	 * @param model 用於傳遞商品資料到前端頁面
	 * @return 編輯商品頁面模板名稱
	 */
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable("id") Long id, Model model) {
		logger.info("進入編輯商品頁面 - productId: {}", id);
		Product product = productService.getById(id);
		model.addAttribute("product", product);
		logger.debug("商品資料載入完成 - productId: {}, productName: {}", id, product.getName());
		return "edit-product";
	}

	/**
	 * 更新商品資料
	 * 保存修改後的商品資訊
	 * 
	 * @param id 商品 ID
	 * @param product 包含更新後商品資料的物件
	 * @return 更新成功後重定向到商品列表頁面
	 */
	@PostMapping("/update/{id}")
	public String updateProduct(@PathVariable("id") Long id, @ModelAttribute("product") Product product) {
		logger.info("開始更新商品 - productId: {}, productName: {}", id, product.getName());
		productService.updateProduct(id, product);
		logger.info("商品更新成功 - productId: {}", id);
		return "redirect:/products";
	}

	/**
	 * 儲存新商品
	 * 將新創建的商品資料保存到資料庫
	 * 
	 * @param product 包含新商品資料的物件
	 * @return 保存成功後重定向到商品列表頁面
	 */
	@PostMapping("/save")
	public String saveUser(@ModelAttribute Product product) {
		logger.info("開始儲存新商品 - productName: {}", product.getName());
		productService.saveProduct(product);
		logger.info("商品儲存成功 - productId: {}", product.getId());
		return "redirect:/products";
	}

	/**
	 * 刪除商品
	 * 根據商品 ID 從資料庫中刪除商品
	 * 
	 * @param id 商品 ID
	 * @return 刪除成功後重定向到商品列表頁面
	 */
	@PostMapping("/delete/{id}")
	public String deleteProduct(@PathVariable("id") Long id) {
		logger.info("開始刪除商品 - productId: {}", id);
		productService.deleteProduct(id);
		logger.info("商品刪除成功 - productId: {}", id);
		return "redirect:/products";
	}
}
