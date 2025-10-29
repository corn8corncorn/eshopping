package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Customer;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.service.CartService;
import com.example.demo.service.CustomerService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;

/**
 * 購物車管理控制器
 * 負責處理購物車相關的請求，包括顯示購物車、添加商品、更新數量、移除商品等操作
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    /**
     * 顯示購物車頁面
     * 獲取當前登入用戶的購物車並顯示購物車內容
     * 
     * @param model 用於傳遞購物車資料到前端頁面
     * @return 購物車頁面模板名稱
     */
    @GetMapping
    public String viewCart(Model model) {
        logger.info("進入購物車頁面");
        
        try {
            // 取得目前登入的用戶
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
                logger.warn("用戶未登入，無法查看購物車");
                return "redirect:/login";
            }
            
            User currentUser = userService.getByUsername(auth.getName());
            logger.debug("目前登入用戶 - username: {}, userId: {}", currentUser.getUsername(), currentUser.getId());

            // 取得客戶資料
            Customer customer = customerService.getByUser(currentUser);
            if (customer == null) {
                logger.warn("客戶資料不存在 - username: {}", currentUser.getUsername());
                model.addAttribute("error", "請先完善您的客戶資料");
                return "redirect:/customers/edit";
            }

            // 取得或建立購物車
            Cart cart = cartService.getOrCreateCart(customer);
            logger.debug("購物車載入成功 - cartId: {}, totalItems: {}, totalAmount: {}", 
                        cart.getId(), cart.getTotalItems(), cart.getTotalAmount());

            model.addAttribute("cart", cart);
            model.addAttribute("cartItems", cart.getCartItems());
            logger.info("購物車頁面載入完成 - cartId: {}", cart.getId());
            
        } catch (Exception e) {
            logger.error("載入購物車時發生錯誤", e);
            model.addAttribute("error", "載入購物車失敗：" + e.getMessage());
        }
        
        return "cart";
    }

    /**
     * 添加商品到購物車
     * 根據商品ID和數量將商品添加到購物車中
     * 
     * @param productId 商品ID
     * @param quantity 商品數量，預設為1
     * @param redirectAttributes 用於傳遞重定向訊息
     * @return 重定向到購物車頁面
     */
    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId,
                           @RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
                           RedirectAttributes redirectAttributes) {
        logger.info("添加商品到購物車 - productId: {}, quantity: {}", productId, quantity);
        
        try {
            // 取得目前登入的用戶
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
                logger.warn("用戶未登入，無法添加商品到購物車");
                return "redirect:/login";
            }
            
            User currentUser = userService.getByUsername(auth.getName());
            logger.debug("目前登入用戶 - username: {}", currentUser.getUsername());

            // 取得客戶資料
            Customer customer = customerService.getByUser(currentUser);
            if (customer == null) {
                logger.warn("客戶資料不存在 - username: {}", currentUser.getUsername());
                redirectAttributes.addFlashAttribute("error", "請先完善您的客戶資料");
                return "redirect:/customers/edit";
            }

            // 取得商品
            Product product = productService.getById(productId);
            if (product == null) {
                logger.warn("商品不存在 - productId: {}", productId);
                redirectAttributes.addFlashAttribute("error", "商品不存在");
                return "redirect:/products";
            }

            // 檢查庫存
            if (!product.hasEnoughStock(quantity)) {
                logger.warn("庫存不足 - productId: {}, requested: {}, available: {}", 
                           productId, quantity, product.getStockQuantity());
                redirectAttributes.addFlashAttribute("error", "庫存不足，目前庫存：" + product.getStockQuantity());
                return "redirect:/products";
            }

            // 取得或建立購物車
            Cart cart = cartService.getOrCreateCart(customer);
            logger.debug("取得購物車 - cartId: {}", cart.getId());

            // 添加商品到購物車
            cartService.addProductToCart(cart.getId(), product, quantity);
            logger.info("商品已添加到購物車 - cartId: {}, productId: {}, quantity: {}", 
                       cart.getId(), productId, quantity);
            
            redirectAttributes.addFlashAttribute("success", "商品已成功添加到購物車");
            
        } catch (Exception e) {
            logger.error("添加商品到購物車時發生錯誤 - productId: {}", productId, e);
            redirectAttributes.addFlashAttribute("error", "添加商品到購物車失敗：" + e.getMessage());
        }
        
        return "redirect:/cart";
    }

    /**
     * 更新購物車中的商品數量
     * 根據商品ID更新購物車中該商品的數量
     * 
     * @param productId 商品ID
     * @param quantity 新的數量
     * @param redirectAttributes 用於傳遞重定向訊息
     * @return 重定向到購物車頁面
     */
    @PostMapping("/update/{productId}")
    public String updateQuantity(@PathVariable("productId") Long productId,
                                 @RequestParam("quantity") Integer quantity,
                                 RedirectAttributes redirectAttributes) {
        logger.info("更新購物車商品數量 - productId: {}, quantity: {}", productId, quantity);
        
        try {
            // 驗證數量
            if (quantity == null || quantity <= 0) {
                logger.warn("無效的數量 - quantity: {}", quantity);
                redirectAttributes.addFlashAttribute("error", "數量必須大於0");
                return "redirect:/cart";
            }

            // 取得目前登入的用戶
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
                logger.warn("用戶未登入");
                return "redirect:/login";
            }
            
            User currentUser = userService.getByUsername(auth.getName());
            Customer customer = customerService.getByUser(currentUser);
            if (customer == null) {
                logger.warn("客戶資料不存在");
                redirectAttributes.addFlashAttribute("error", "請先完善您的客戶資料");
                return "redirect:/customers/edit";
            }

            // 取得購物車
            Cart cart = cartService.getByCustomer(customer);
            if (cart == null) {
                logger.warn("購物車不存在");
                redirectAttributes.addFlashAttribute("error", "購物車不存在");
                return "redirect:/cart";
            }

            // 檢查商品庫存
            Product product = productService.getById(productId);
            if (product == null) {
                logger.warn("商品不存在 - productId: {}", productId);
                redirectAttributes.addFlashAttribute("error", "商品不存在");
                return "redirect:/cart";
            }

            if (!product.hasEnoughStock(quantity)) {
                logger.warn("庫存不足 - productId: {}, requested: {}, available: {}", 
                           productId, quantity, product.getStockQuantity());
                redirectAttributes.addFlashAttribute("error", "庫存不足，目前庫存：" + product.getStockQuantity());
                return "redirect:/cart";
            }

            // 更新數量
            cartService.updateCartItemQuantity(cart.getId(), productId, quantity);
            logger.info("購物車商品數量已更新 - cartId: {}, productId: {}, quantity: {}", 
                       cart.getId(), productId, quantity);
            
            redirectAttributes.addFlashAttribute("success", "商品數量已更新");
            
        } catch (Exception e) {
            logger.error("更新購物車商品數量時發生錯誤 - productId: {}", productId, e);
            redirectAttributes.addFlashAttribute("error", "更新數量失敗：" + e.getMessage());
        }
        
        return "redirect:/cart";
    }

    /**
     * 從購物車移除商品
     * 根據商品ID從購物車中移除該商品
     * 
     * @param productId 商品ID
     * @param redirectAttributes 用於傳遞重定向訊息
     * @return 重定向到購物車頁面
     */
    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable("productId") Long productId,
                                RedirectAttributes redirectAttributes) {
        logger.info("從購物車移除商品 - productId: {}", productId);
        
        try {
            // 取得目前登入的用戶
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
                logger.warn("用戶未登入");
                return "redirect:/login";
            }
            
            User currentUser = userService.getByUsername(auth.getName());
            Customer customer = customerService.getByUser(currentUser);
            if (customer == null) {
                logger.warn("客戶資料不存在");
                redirectAttributes.addFlashAttribute("error", "請先完善您的客戶資料");
                return "redirect:/customers/edit";
            }

            // 取得購物車
            Cart cart = cartService.getByCustomer(customer);
            if (cart == null) {
                logger.warn("購物車不存在");
                redirectAttributes.addFlashAttribute("error", "購物車不存在");
                return "redirect:/cart";
            }

            // 移除商品
            cartService.removeProductFromCart(cart.getId(), productId);
            logger.info("商品已從購物車移除 - cartId: {}, productId: {}", cart.getId(), productId);
            
            redirectAttributes.addFlashAttribute("success", "商品已從購物車移除");
            
        } catch (Exception e) {
            logger.error("從購物車移除商品時發生錯誤 - productId: {}", productId, e);
            redirectAttributes.addFlashAttribute("error", "移除商品失敗：" + e.getMessage());
        }
        
        return "redirect:/cart";
    }

    /**
     * 清空購物車
     * 清空購物車中的所有商品
     * 
     * @param redirectAttributes 用於傳遞重定向訊息
     * @return 重定向到購物車頁面
     */
    @PostMapping("/clear")
    public String clearCart(RedirectAttributes redirectAttributes) {
        logger.info("清空購物車");
        
        try {
            // 取得目前登入的用戶
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
                logger.warn("用戶未登入");
                return "redirect:/login";
            }
            
            User currentUser = userService.getByUsername(auth.getName());
            Customer customer = customerService.getByUser(currentUser);
            if (customer == null) {
                logger.warn("客戶資料不存在");
                redirectAttributes.addFlashAttribute("error", "請先完善您的客戶資料");
                return "redirect:/customers/edit";
            }

            // 取得購物車
            Cart cart = cartService.getByCustomer(customer);
            if (cart == null) {
                logger.warn("購物車不存在");
                redirectAttributes.addFlashAttribute("error", "購物車不存在");
                return "redirect:/cart";
            }

            // 清空購物車
            cartService.clearCart(cart.getId());
            logger.info("購物車已清空 - cartId: {}", cart.getId());
            
            redirectAttributes.addFlashAttribute("success", "購物車已清空");
            
        } catch (Exception e) {
            logger.error("清空購物車時發生錯誤", e);
            redirectAttributes.addFlashAttribute("error", "清空購物車失敗：" + e.getMessage());
        }
        
        return "redirect:/cart";
    }
}

