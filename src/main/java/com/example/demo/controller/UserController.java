package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

/**
 * 用戶管理控制器
 * 負責處理用戶資料相關的 CRUD 操作
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 顯示用戶列表頁面
     * 獲取所有用戶資料並顯示在列表中
     * 
     * @param model 用於傳遞用戶資料到前端頁面
     * @return 用戶列表頁面模板名稱
     */
    @GetMapping
    public String listUsers(Model model) {
        logger.info("進入用戶列表頁面");
        model.addAttribute("users", userService.getAll());
        logger.info("用戶列表載入完成");
        return "users";
    }

    /**
     * 顯示新增用戶頁面
     * 準備空白的用戶資料供填寫
     * 
     * @param model 用於傳遞空白用戶資料到前端頁面
     * @return 新增用戶頁面模板名稱
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        logger.info("進入新增用戶頁面");
        model.addAttribute("user", new User());
        return "add-user";
    }

    /**
     * 顯示編輯用戶頁面
     * 根據用戶 ID 獲取用戶資料供編輯
     * 
     * @param id 用戶 ID
     * @param model 用於傳遞用戶資料到前端頁面
     * @return 編輯用戶頁面模板名稱
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        logger.info("進入編輯用戶頁面 - userId: {}", id);
        User user = userService.getById(id);
        model.addAttribute("user", user);
        logger.debug("用戶資料載入完成 - userId: {}, username: {}", id, user.getUsername());
        return "edit-user";
    }

    /**
     * 更新用戶資料
     * 保存修改後的用戶資訊，如果密碼不為空則進行加密
     * 
     * @param id 用戶 ID
     * @param user 包含更新後用戶資料的物件
     * @return 更新成功後重定向到用戶列表頁面
     */
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") User user) {
        logger.info("開始更新用戶 - userId: {}, username: {}", id, user.getUsername());
        
        // 編輯用戶時，如果密碼不為空才加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            logger.debug("密碼已提供，進行加密");
            String encodedPassword = userService.encodePassword(user.getPassword());
            user.setPassword(encodedPassword);
        }
        userService.updateUser(id, user);
        logger.info("用戶更新成功 - userId: {}", id);
        return "redirect:/users";
    }

    /**
     * 儲存新用戶
     * 將新創建的用戶資料保存到資料庫，密碼會自動加密
     * 
     * @param user 包含新用戶資料的物件
     * @return 保存成功後重定向到用戶列表頁面
     */
    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user) {
        logger.info("開始儲存新用戶 - username: {}", user.getUsername());
        
        // 新增用戶時加密密碼
        logger.debug("對新用戶密碼進行加密");
        String encodedPassword = userService.encodePassword(user.getPassword());
        user.setPassword(encodedPassword);
        userService.saveUser(user);
        logger.info("用戶儲存成功 - userId: {}, username: {}", user.getId(), user.getUsername());
        return "redirect:/users";
    }

    /**
     * 刪除用戶
     * 根據用戶 ID 從資料庫中刪除用戶
     * 
     * @param id 用戶 ID
     * @return 刪除成功後重定向到用戶列表頁面
     */
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        logger.info("開始刪除用戶 - userId: {}", id);
        userService.deleteUser(id);
        logger.info("用戶刪除成功 - userId: {}", id);
        return "redirect:/users";
    }
}
