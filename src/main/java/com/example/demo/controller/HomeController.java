package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.Cart;
import com.example.demo.model.Customer;
import com.example.demo.model.Product;
import com.example.demo.model.Product.ProductStatus;
import com.example.demo.model.User;
import com.example.demo.service.CartService;
import com.example.demo.service.CustomerService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 首頁控制器
 * 負責處理首頁相關的請求
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    /**
     * 根路徑重定向
     * 將根路徑 "/" 重定向到 "/home"
     * 
     * @return 重定向到首頁
     */
    @GetMapping("/")
    public String home() {
        logger.debug("根路徑重定向到首頁");
        return "redirect:/home";
    }

    /**
     * 顯示首頁
     * 根據用戶登入狀態顯示不同的內容，並展示熱門商品、推薦商品等
     * 
     * @param model 用於傳遞用戶登入狀態和商品資料到前端頁面
     * @return 首頁模板名稱
     */
    @GetMapping("/home")
    public String homePage(Model model) {
        logger.info("進入首頁");
        
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
                logger.debug("用戶已登入 - username: {}", auth.getName());
                model.addAttribute("isLoggedIn", true);
                model.addAttribute("username", auth.getName());
                
                // 取得購物車商品數量
                try {
                    User currentUser = userService.getByUsername(auth.getName());
                    Customer customer = customerService.getByUser(currentUser);
                    if (customer != null) {
                        Cart cart = cartService.getByCustomer(customer);
                        if (cart != null) {
                            model.addAttribute("cartItemCount", cart.getTotalItems());
                        } else {
                            model.addAttribute("cartItemCount", 0);
                        }
                    } else {
                        model.addAttribute("cartItemCount", 0);
                    }
                } catch (Exception e) {
                    logger.debug("無法取得購物車資訊", e);
                    model.addAttribute("cartItemCount", 0);
                }
            } else {
                logger.debug("用戶未登入");
                model.addAttribute("isLoggedIn", false);
                model.addAttribute("cartItemCount", 0);
            }
            
            // 取得所有上架中的商品
            List<Product> allProducts = productService.getAll();
            List<Product> activeProducts = allProducts.stream()
                    .filter(p -> p.getStatus() == ProductStatus.ACTIVE)
                    .collect(Collectors.toList());
            
            // 熱門商品（庫存充足的前 6 個商品）
            List<Product> hotProducts = activeProducts.stream()
                    .filter(p -> p.getStockQuantity() > p.getMinStockThreshold())
                    .limit(6)
                    .collect(Collectors.toList());
            
            // 最新商品（最近創建的 6 個商品，按創建時間倒序）
            List<Product> latestProducts = activeProducts.stream()
                    .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                    .limit(6)
                    .collect(Collectors.toList());
            
            // 推薦商品（庫存充足的隨機商品，取前 4 個）
            List<Product> recommendedProducts = activeProducts.stream()
                    .filter(p -> p.getStockQuantity() > p.getMinStockThreshold())
                    .limit(4)
                    .collect(Collectors.toList());
            
            // 取得所有商品分類
            List<String> categories = activeProducts.stream()
                    .map(Product::getType)
                    .distinct()
                    .sorted()
                    .limit(8) // 最多顯示 8 個分類
                    .collect(Collectors.toList());
            
            model.addAttribute("hotProducts", hotProducts);
            model.addAttribute("latestProducts", latestProducts);
            model.addAttribute("recommendedProducts", recommendedProducts);
            model.addAttribute("categories", categories);
            
            logger.debug("首頁商品資料載入完成 - 熱門: {}, 最新: {}, 推薦: {}, 分類: {}", 
                        hotProducts.size(), latestProducts.size(), recommendedProducts.size(), categories.size());
            
        } catch (Exception e) {
            logger.error("載入首頁資料時發生錯誤", e);
            model.addAttribute("error", "載入首頁資料失敗：" + e.getMessage());
        }
        
        logger.info("首頁載入完成");
        return "home";
    }
}
