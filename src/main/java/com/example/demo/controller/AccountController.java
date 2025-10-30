package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Customer;
import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.service.CustomerService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;

/**
 * 會員中心控制器
 * 負責處理會員中心相關的請求，包括個人資料、訂單管理、帳號設定等
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    /**
     * 顯示會員中心主頁面
     * 顯示登入用戶的個人資訊、最近的訂單、帳號設定等
     * 
     * @param model 用於傳遞資料到前端頁面
     * @return 會員中心頁面模板名稱
     */
    @GetMapping
    public String showAccount(Model model) {
        logger.info("進入會員中心頁面");
        
        try {
            // 取得目前登入的用戶
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = userService.getByUsername(auth.getName());
            
            if (currentUser == null) {
                logger.warn("未找到登入用戶");
                return "redirect:/login";
            }
            
            logger.debug("目前登入用戶 - username: {}, userId: {}", currentUser.getUsername(), currentUser.getId());

            // 取得客戶資料
            Customer customer = customerService.getByUser(currentUser);
            
            if (customer == null) {
                // 如果沒有客戶資料，建立一個
                logger.info("客戶資料不存在，建立新的客戶資料 - username: {}", currentUser.getUsername());
                customer = customerService.createCustomerForUser(currentUser, currentUser.getUsername());
            }

            // 取得最近的訂單（最多 5 筆）
            List<Order> recentOrders = orderService.getByCustomer(customer);
            if (recentOrders.size() > 5) {
                recentOrders = recentOrders.subList(0, 5);
            }

            // 計算統計資料
            int totalOrders = recentOrders.size();
            long pendingOrders = recentOrders.stream()
                    .filter(order -> order.getStatus() == Order.OrderStatus.PENDING)
                    .count();

            // 傳遞資料到前端
            model.addAttribute("customer", customer);
            model.addAttribute("user", currentUser);
            model.addAttribute("recentOrders", recentOrders);
            model.addAttribute("totalOrders", totalOrders);
            model.addAttribute("pendingOrders", pendingOrders);
            
            logger.info("會員中心頁面載入完成 - customerId: {}, username: {}", 
                    customer.getId(), currentUser.getUsername());
            
            return "account";
            
        } catch (Exception e) {
            logger.error("載入會員中心頁面時發生錯誤", e);
            model.addAttribute("error", "載入會員中心資料時發生錯誤，請稍後再試");
            return "account";
        }
    }
}

