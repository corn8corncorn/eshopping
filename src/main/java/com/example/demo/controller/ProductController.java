package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	/**
	 * 商品列表頁
	 */
	@GetMapping
	public String listProducts(Model model) {
		model.addAttribute("products", productService.getAll());
		return "products";
	}

	/**
	 * 顯示新增商品頁
	 */
	@GetMapping("/add")
	public String showAddForm(Model model) {
		model.addAttribute("product", new Product());
		return "add-product";
	}

	/**
	 * 顯示編輯商品頁
	 */
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable("id") Long id, Model model) {
		Product product = productService.getById(id);
		model.addAttribute("product", product);
		return "edit-product";
	}

	/**
	 * 更新商品
	 */
	@PostMapping("/update/{id}")
	public String updateProduct(@PathVariable("id") Long id, @ModelAttribute("product") Product product) {
		productService.updateProduct(id, product);
		return "redirect:/products";
	}

	/**
	 * 儲存新商品
	 */
	@PostMapping("/save")
	public String saveUser(@ModelAttribute Product product) {
		productService.saveProduct(product);
		return "redirect:/products";
	}

	/**
	 * 刪除商品
	 */
	@PostMapping("/delete/{id}")
	public String deleteProduct(@PathVariable("id") Long id) {
		productService.deleteProduct(id);
		return "redirect:/products";
	}
}
