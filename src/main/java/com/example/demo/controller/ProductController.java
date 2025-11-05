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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.BindingResult;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import com.example.demo.service.GCPStorageService;

import javax.validation.Valid;

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

	@Autowired
	private GCPStorageService gcpStorageService;

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
	 * @param page 來源頁碼（用於編輯後返回原頁面）
	 * @param model 用於傳遞商品資料到前端頁面
	 * @return 編輯商品頁面模板名稱
	 */
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable("id") Long id,
	                          @RequestParam(value = "page", defaultValue = "0") int page,
	                          Model model) {
		logger.info("進入編輯商品頁面 - productId: {}, page: {}", id, page);
		Product product = productService.getById(id);
		model.addAttribute("product", product);
		model.addAttribute("currentPage", page);
		logger.debug("商品資料載入完成 - productId: {}, productName: {}, page: {}", id, product.getName(), page);
		return "edit-product";
	}

	/**
	 * 更新商品資料
	 * 保存修改後的商品資訊
	 * 
	 * @param id 商品 ID
	 * @param product 包含更新後商品資料的物件
	 * @param redirectAttributes 用於傳遞重定向訊息
	 * @return 更新成功後重定向到商品列表頁面
	 */
	@PostMapping("/update/{id}")
	public String updateProduct(@PathVariable("id") Long id, 
	                           @Valid @ModelAttribute("product") Product product,
	                           BindingResult bindingResult,
	                           @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
	                           @RequestParam(value = "page", defaultValue = "0") int page,
	                           RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			logger.warn("商品更新驗證失敗 - productId: {}, errors: {}", id, bindingResult.getAllErrors());
			redirectAttributes.addFlashAttribute("error", "商品更新失敗：請檢查輸入資料是否正確");
			return "redirect:/products/edit/" + id + "?page=" + page;
		}
		
		try {
			logger.info("開始更新商品 - productId: {}, productName: {}, stockQuantity: {}, page: {}", 
			           id, product.getName(), product.getStockQuantity(), page);
			
			// 處理圖片上傳
			if (imageFile != null && !imageFile.isEmpty()) {
				try {
					// 刪除舊圖片
					Product existingProduct = productService.getById(id);
					if (existingProduct != null && existingProduct.getImageUrl() != null 
					    && !existingProduct.getImageUrl().isEmpty()) {
						gcpStorageService.deleteImage(existingProduct.getImageUrl());
						logger.info("舊圖片已刪除 - productId: {}, oldImageUrl: {}", id, existingProduct.getImageUrl());
					}
					
					// 上傳新圖片
					String imageUrl = gcpStorageService.uploadImage(imageFile, "products");
					product.setImageUrl(imageUrl);
					logger.info("新圖片上傳成功 - productId: {}, imageUrl: {}", id, imageUrl);
				} catch (Exception e) {
					logger.error("圖片上傳失敗", e);
					redirectAttributes.addFlashAttribute("error", "圖片上傳失敗：" + e.getMessage());
					return "redirect:/products/edit/" + id + "?page=" + page;
				}
			}
			
			productService.updateProduct(id, product);
			logger.info("商品更新成功 - productId: {}, page: {}", id, page);
			redirectAttributes.addFlashAttribute("success", "商品「" + product.getName() + "」更新成功");
		} catch (Exception e) {
			logger.error("更新商品失敗 - productId: {}", id, e);
			redirectAttributes.addFlashAttribute("error", "商品更新失敗：" + e.getMessage());
		}
		return "redirect:/products?page=" + page;
	}

	/**
	 * 儲存新商品
	 * 將新創建的商品資料保存到資料庫
	 * 
	 * @param product 包含新商品資料的物件
	 * @param redirectAttributes 用於傳遞重定向訊息
	 * @return 保存成功後重定向到商品列表頁面
	 */
	@PostMapping("/save")
	public String saveUser(@Valid @ModelAttribute Product product, 
	                     BindingResult bindingResult,
	                     @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
	                     RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			logger.warn("商品新增驗證失敗 - errors: {}", bindingResult.getAllErrors());
			redirectAttributes.addFlashAttribute("error", "商品新增失敗：請檢查輸入資料是否正確（價格不能為負數）");
			return "redirect:/products/add";
		}
		
		try {
			logger.info("開始儲存新商品 - productName: {}", product.getName());
			
			// 處理圖片上傳
			if (imageFile != null && !imageFile.isEmpty()) {
				try {
					String imageUrl = gcpStorageService.uploadImage(imageFile, "products");
					product.setImageUrl(imageUrl);
					logger.info("圖片上傳成功 - productName: {}, imageUrl: {}", product.getName(), imageUrl);
				} catch (Exception e) {
					logger.error("圖片上傳失敗", e);
					redirectAttributes.addFlashAttribute("error", "圖片上傳失敗：" + e.getMessage());
					return "redirect:/products/add";
				}
			}
			
			productService.saveProduct(product);
			logger.info("商品儲存成功 - productId: {}", product.getId());
			redirectAttributes.addFlashAttribute("success", "商品「" + product.getName() + "」新增成功");
		} catch (Exception e) {
			logger.error("新增商品失敗 - productName: {}", product.getName(), e);
			redirectAttributes.addFlashAttribute("error", "商品新增失敗：" + e.getMessage());
		}
		return "redirect:/products";
	}

	/**
	 * 刪除商品
	 * 根據商品 ID 從資料庫中刪除商品
	 * 
	 * @param id 商品 ID
	 * @param redirectAttributes 用於傳遞重定向訊息
	 * @return 刪除成功後重定向到商品列表頁面
	 */
	@PostMapping("/delete/{id}")
	public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		try {
			logger.info("開始刪除商品 - productId: {}", id);
			Product product = productService.getById(id);
			String productName = product != null ? product.getName() : "商品";
			productService.deleteProduct(id);
			logger.info("商品刪除成功 - productId: {}", id);
			redirectAttributes.addFlashAttribute("success", "商品「" + productName + "」刪除成功");
		} catch (Exception e) {
			logger.error("刪除商品失敗 - productId: {}", id, e);
			redirectAttributes.addFlashAttribute("error", "商品刪除失敗：" + e.getMessage());
		}
		return "redirect:/products";
	}
}
