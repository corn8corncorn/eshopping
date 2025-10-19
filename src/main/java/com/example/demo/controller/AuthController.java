package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "登入失敗，請檢查您的帳號和密碼");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name, 
                          @RequestParam String email, 
                          @RequestParam String password, 
                          @RequestParam String confirmPassword,
                          Model model) {
        
        // 驗證密碼是否一致
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "密碼不一致");
            return "register";
        }

        // 檢查email是否已存在
        if (userService.getUserByEmail(email) != null) {
            model.addAttribute("error", "此email已被註冊");
            return "register";
        }

        // 創建新用戶
        User newUser = new User(name, email, passwordEncoder.encode(password), "USER");
        userService.saveUser(newUser);

        model.addAttribute("success", "註冊成功！請登入");
        return "login";
    }
}
