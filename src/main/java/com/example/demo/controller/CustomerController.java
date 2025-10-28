package com.example.demo.controller;

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
 * 負責處理客戶資料相關的請求
 */
@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    /**
     * 客戶列表頁（管理員功能）
     */
    @GetMapping
    public String listCustomers(Model model) {
        model.addAttribute("customers", customerService.getAll());
        return "customers";
    }

    /**
     * 顯示編輯客戶資料頁面
     */
    @GetMapping("/edit")
    public String showEditForm(Model model) {
        // 取得目前登入的用戶
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getByUsername(auth.getName());

        // 取得客戶資料
        Customer customer = customerService.getByUser(currentUser);
        if (customer == null) {
            // 如果沒有客戶資料，建立一個
            customer = customerService.createCustomerForUser(currentUser, currentUser.getUsername());
        }

        model.addAttribute("customer", customer);
        return "edit-customer";
    }

    /**
     * 更新客戶資料
     */
    @PostMapping("/update")
    public String updateCustomer(@ModelAttribute("customer") Customer customer) {
        customerService.update(customer.getId(), customer);
        return "redirect:/home";
    }

    /**
     * 顯示客戶詳細資料頁面
     */
    @GetMapping("/profile")
    public String showProfile(Model model) {
        // 取得目前登入的用戶
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getByUsername(auth.getName());

        // 取得客戶資料
        Customer customer = customerService.getByUser(currentUser);
        model.addAttribute("customer", customer);
        return "customer-profile";
    }
}
