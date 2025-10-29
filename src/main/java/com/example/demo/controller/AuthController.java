package com.example.demo.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.UserLoginDTO;
import com.example.demo.dto.UserRegistrationDTO;
import com.example.demo.model.User;
import com.example.demo.service.CustomerService;
import com.example.demo.service.UserService;

/**
 * 認證控制器
 * 負責處理使用者登入和註冊相關的請求
 */
@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    // 密碼加密與驗證交由 UserService 處理，Controller 不直接處理

    /**
     * 顯示登入頁面
     * @param error 錯誤參數，如果登入失敗會帶有此參數
     * @param success 成功參數，如果註冊成功會帶有此參數
     * @param model 用於傳遞資料到前端頁面
     * @return 登入頁面模板名稱
     */
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "success", required = false) String success, Model model) {
        logger.info("進入登入頁面 - error: {}, success: {}", error, success);
        
        // 如果有錯誤參數，表示登入失敗，顯示錯誤訊息
        if (error != null) {
            logger.warn("登入失敗 - 用戶嘗試登入失敗");
            model.addAttribute("error", "登入失敗，請檢查您的帳號和密碼");
        }

        // 如果有成功參數，表示註冊成功，顯示成功訊息
        if (success != null) {
            logger.info("註冊成功 - 導向登入頁面");
            model.addAttribute("success", "註冊成功！請使用您的帳號和密碼登入");
        }

        // 加入登入 DTO 到 model
        if (!model.containsAttribute("userLoginDTO")) {
            model.addAttribute("userLoginDTO", new UserLoginDTO());
        }

        logger.info("顯示登入頁面完成");
        return "login";
    }

    /**
     * 顯示註冊頁面
     * @param model 用於傳遞資料到前端頁面
     * @return 註冊頁面模板名稱
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        logger.info("進入註冊頁面");
        
        // 加入註冊 DTO 到 model
        if (!model.containsAttribute("userRegistrationDTO")) {
            model.addAttribute("userRegistrationDTO", new UserRegistrationDTO());
        }
        
        logger.info("顯示註冊頁面完成");
        return "register";
    }

    /**
     * 處理使用者註冊請求
     * 驗證表單資料、檢查用戶名和郵箱是否已存在、創建新用戶並建立客戶資料
     * 
     * @param userRegistrationDTO 註冊資料傳輸物件，包含用戶名、郵箱、密碼等資訊
     * @param bindingResult 表單驗證結果，包含驗證錯誤
     * @param model 用於傳遞資料到前端頁面的模型物件
     * @return 註冊成功導向登入頁面，失敗則返回註冊頁面
     */
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userRegistrationDTO") UserRegistrationDTO userRegistrationDTO,
                           BindingResult bindingResult,
                           Model model) {
        logger.info("處理使用者註冊請求 - username: {}", userRegistrationDTO.getUsername());

        // 檢查表單驗證錯誤
        if (bindingResult.hasErrors()) {
            logger.warn("註冊失敗 - 表單驗證錯誤: {}", bindingResult.getAllErrors());
            return "register";
        }

        // 驗證密碼是否一致
        if (!userRegistrationDTO.isPasswordMatch()) {
            logger.warn("註冊失敗 - 密碼不一致");
            model.addAttribute("error", "密碼不一致");
            return "register";
        }

        // 檢查 username 與 email 是否已存在
        if (userService.isUsernameExists(userRegistrationDTO.getUsername())) {
            logger.warn("註冊失敗 - 使用者名稱已存在: {}", userRegistrationDTO.getUsername());
            model.addAttribute("error", "此使用者名稱已被註冊");
            return "register";
        }
        if (userService.isEmailExists(userRegistrationDTO.getEmail())) {
            logger.warn("註冊失敗 - Email 已存在: {}", userRegistrationDTO.getEmail());
            model.addAttribute("error", "此 Email 已被註冊");
            return "register";
        }

        // 建立新使用者（密碼加密交由 Service 處理）
        logger.debug("建立新使用者 - username: {}, email: {}", userRegistrationDTO.getUsername(), userRegistrationDTO.getEmail());
        User newUser = new User();
        newUser.setUsername(userRegistrationDTO.getUsername());
        newUser.setEmail(userRegistrationDTO.getEmail());
        newUser.setPassword(userRegistrationDTO.getPassword());

        try {
            // 呼叫 Service 層進行註冊，包含密碼加密和資料驗證
            logger.info("開始註冊使用者 - username: {}", userRegistrationDTO.getUsername());
            User savedUser = userService.registerUser(newUser);
            logger.info("使用者註冊成功 - userId: {}, username: {}", savedUser.getId(), savedUser.getUsername());

            // 為新用戶建立客戶資料
            logger.debug("為新用戶建立客戶資料 - userId: {}", savedUser.getId());
            customerService.createCustomerForUser(savedUser, userRegistrationDTO.getUsername());
            logger.info("客戶資料建立成功");

            // 註冊成功，重定向到登入頁面並顯示成功訊息
            logger.info("註冊流程完成 - 導向登入頁面");
            return "redirect:/login?success=true";

        } catch (IllegalArgumentException ex) {
            // 如果註冊失敗，顯示錯誤訊息並返回註冊頁面
            logger.error("註冊失敗 - IllegalArgumentException: {}", ex.getMessage());
            model.addAttribute("error", ex.getMessage());
            return "register";
        } catch (Exception ex) {
            // 處理其他可能的異常
            logger.error("註冊過程發生未預期的錯誤: {}", ex.getMessage(), ex);
            model.addAttribute("error", "註冊過程中發生錯誤：" + ex.getMessage());
            return "register";
        }
    }

    /**
     * 顯示忘記密碼頁面
     * 提供用戶輸入 email 和 username 用於驗證身份
     * 
     * @param model 用於傳遞資料到前端頁面
     * @return 忘記密碼頁面模板名稱
     */
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        logger.info("進入忘記密碼頁面");
        return "forgot-password";
    }

    /**
     * 處理忘記密碼請求 - 驗證 email 和 username
     * @param email 電子郵件
     * @param username 使用者名稱
     * @param model 用於傳遞資料到前端頁面
     * @return 驗證成功導向重設密碼頁面，失敗則返回忘記密碼頁面
     */
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email,
                                      @RequestParam("username") String username,
                                      Model model) {
        logger.info("處理忘記密碼請求 - email: {}, username: {}", email, username);
        
        // 根據 email 查找使用者
        User user = userService.getByEmail(email);
        logger.debug("根據 email 查找使用者結果: {}", user != null ? "找到" : "未找到");

        // 驗證 email 和 username 是否匹配
        if (user == null || !user.getUsername().equals(username)) {
            logger.warn("忘記密碼驗證失敗 - email: {}, username: {}", email, username);
            model.addAttribute("error", "電子郵件或使用者名稱不正確");
            return "forgot-password";
        }

        // 驗證成功，將使用者 ID 傳遞到重設密碼頁面
        logger.info("忘記密碼驗證成功 - userId: {}, username: {}", user.getId(), user.getUsername());
        model.addAttribute("userId", user.getId());
        model.addAttribute("username", user.getUsername());
        return "reset-password";
    }

    /**
     * 處理重設密碼請求
     * @param userId 使用者 ID
     * @param newPassword 新密碼
     * @param confirmPassword 確認密碼
     * @param model 用於傳遞資料到前端頁面
     * @return 重設成功導向登入頁面，失敗則返回重設密碼頁面
     */
    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("userId") Long userId,
                                     @RequestParam("newPassword") String newPassword,
                                     @RequestParam("confirmPassword") String confirmPassword,
                                     Model model) {
        logger.info("處理重設密碼請求 - userId: {}", userId);
        
        // 驗證密碼是否一致
        if (!newPassword.equals(confirmPassword)) {
            logger.warn("重設密碼失敗 - 密碼不一致");
            model.addAttribute("error", "密碼不一致");
            model.addAttribute("userId", userId);
            return "reset-password";
        }

        // 取得使用者並更新密碼
        logger.debug("查找使用者 - userId: {}", userId);
        User user = userService.getById(userId);
        if (user == null) {
            logger.error("重設密碼失敗 - 使用者不存在 - userId: {}", userId);
            model.addAttribute("error", "使用者不存在");
            return "forgot-password";
        }

        // 加密新密碼並更新
        logger.info("開始重設密碼 - userId: {}, username: {}", userId, user.getUsername());
        String encodedPassword = userService.encodePassword(newPassword);
        user.setPassword(encodedPassword);
        userService.updateUser(user.getId(), user);
        logger.info("密碼重設成功 - userId: {}", userId);

        // 重設成功，導向登入頁面並顯示成功訊息
        model.addAttribute("success", "密碼重設成功！請使用新密碼登入");
        return "login";
    }
}
