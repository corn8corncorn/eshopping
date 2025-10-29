package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Customer;
import com.example.demo.model.User;
import com.example.demo.service.CustomerService;
import com.example.demo.service.UserService;

/**
 * 客戶管理控制器
 * 負責處理客戶資料相關的請求，包括客戶列表、編輯和查看資料
 */
@Controller
@RequestMapping("/customers")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    /**
     * 顯示客戶列表頁面（管理員功能）
     * 獲取所有客戶資料並顯示在列表中
     * 
     * @param model 用於傳遞客戶資料到前端頁面
     * @return 客戶列表頁面模板名稱
     */
    @GetMapping
    public String listCustomers(Model model) {
        logger.info("進入客戶列表頁面");
        model.addAttribute("customers", customerService.getAll());
        logger.info("客戶列表載入完成");
        return "customers";
    }

    /**
     * 顯示編輯客戶資料頁面
     * 獲取目前登入用戶的客戶資料，如果不存在則創建一個新的
     * 
     * @param model 用於傳遞客戶資料到前端頁面
     * @return 編輯客戶資料頁面模板名稱
     */
    @GetMapping("/edit")
    public String showEditForm(Model model) {
        logger.info("進入編輯客戶資料頁面");
        
        // 取得目前登入的用戶
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getByUsername(auth.getName());
        logger.debug("目前登入用戶 - username: {}, userId: {}", currentUser.getUsername(), currentUser.getId());

        // 取得客戶資料
        Customer customer = customerService.getByUser(currentUser);
        if (customer == null) {
            // 如果沒有客戶資料，建立一個
            logger.info("客戶資料不存在，建立新的客戶資料 - username: {}", currentUser.getUsername());
            customer = customerService.createCustomerForUser(currentUser, currentUser.getUsername());
        }

        model.addAttribute("customer", customer);
        logger.info("顯示編輯客戶資料頁面完成 - customerId: {}", customer.getId());
        return "edit-customer";
    }

    /**
     * 更新客戶資料
     * 保存修改後的客戶資訊
     * 
     * @param customer 包含更新後客戶資料的物件
     * @return 更新成功後重定向到首頁
     */
    @PostMapping("/update")
    public String updateCustomer(@ModelAttribute("customer") Customer customer) {
        logger.info("更新客戶資料 - customerId: {}", customer.getId());
        customerService.update(customer.getId(), customer);
        logger.info("客戶資料更新成功 - customerId: {}", customer.getId());
        return "redirect:/home";
    }

    /**
     * 顯示客戶詳細資料頁面
     * 顯示目前登入用戶的客戶資料
     * 
     * @param model 用於傳遞客戶資料到前端頁面
     * @return 客戶詳細資料頁面模板名稱
     */
    @GetMapping("/profile")
    public String showProfile(Model model) {
        logger.info("進入客戶詳細資料頁面");
        
        // 取得目前登入的用戶
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getByUsername(auth.getName());
        logger.debug("目前登入用戶 - username: {}, userId: {}", currentUser.getUsername(), currentUser.getId());

        // 取得客戶資料
        Customer customer = customerService.getByUser(currentUser);
        model.addAttribute("customer", customer);
        logger.info("客戶詳細資料載入完成 - customerId: {}", customer.getId());
        return "customer-profile";
    }
}
