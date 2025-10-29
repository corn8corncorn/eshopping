package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首頁控制器
 * 負責處理首頁相關的請求
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

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
     * 根據用戶登入狀態顯示不同的內容
     * 
     * @param model 用於傳遞用戶登入狀態到前端頁面
     * @return 首頁模板名稱
     */
    @GetMapping("/home")
    public String homePage(Model model) {
        logger.info("進入首頁");
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            logger.debug("用戶已登入 - username: {}", auth.getName());
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("username", auth.getName());
        } else {
            logger.debug("用戶未登入");
            model.addAttribute("isLoggedIn", false);
        }
        
        logger.info("首頁載入完成");
        return "home";
    }
}
