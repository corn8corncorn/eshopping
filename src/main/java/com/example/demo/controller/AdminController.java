package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;

import java.util.List;

/**
 * 後台管理控制器
 * 統一管理後台管理相關的路由和功能
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    /**
     * 後台管理主頁面（Dashboard）
     * 顯示系統統計資訊和快速入口
     * 
     * @param model 用於傳遞資料到前端頁面
     * @return 後台管理主頁面模板名稱
     */
    @GetMapping({"", "/dashboard"})
    public String dashboard(Model model) {
        logger.info("進入後台管理主頁面");
        
        try {
            // 獲取統計資料
            List<User> allUsers = userService.getAll();
            List<Product> allProducts = productService.getAll();
            List<Order> allOrders = orderService.getAll();
            
            // 計算統計數據
            long totalUsers = allUsers != null ? allUsers.size() : 0;
            long totalProducts = allProducts != null ? allProducts.size() : 0;
            long totalOrders = allOrders != null ? allOrders.size() : 0;
            
            // 計算訂單統計
            long pendingOrders = allOrders != null ? 
                allOrders.stream().filter(o -> o.getStatus() == Order.OrderStatus.PENDING).count() : 0;
            long paidOrders = allOrders != null ?
                allOrders.stream().filter(o -> o.getPaymentStatus() == Order.PaymentStatus.PAID).count() : 0;
            
            // 計算總銷售額
            double totalSales = allOrders != null ?
                allOrders.stream()
                    .filter(o -> o.getFinalAmount() != null)
                    .mapToDouble(o -> o.getFinalAmount().doubleValue())
                    .sum() : 0.0;
            
            model.addAttribute("totalUsers", totalUsers);
            model.addAttribute("totalProducts", totalProducts);
            model.addAttribute("totalOrders", totalOrders);
            model.addAttribute("pendingOrders", pendingOrders);
            model.addAttribute("paidOrders", paidOrders);
            model.addAttribute("totalSales", totalSales);
            
            logger.info("後台管理主頁面載入完成 - 用戶數: {}, 商品數: {}, 訂單數: {}", 
                       totalUsers, totalProducts, totalOrders);
            
        } catch (Exception e) {
            logger.error("載入後台管理主頁面時發生錯誤", e);
            model.addAttribute("error", "載入統計資料失敗：" + e.getMessage());
        }
        
        return "admin/dashboard";
    }
}

