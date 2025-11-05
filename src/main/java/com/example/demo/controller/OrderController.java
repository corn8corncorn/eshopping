package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.example.demo.model.Order;
import com.example.demo.model.Order.OrderStatus;
import com.example.demo.model.Order.PaymentMethod;
import com.example.demo.model.Order.PaymentStatus;
import com.example.demo.model.OrderAddress;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.service.CartService;
import com.example.demo.service.CustomerService;
import com.example.demo.service.OrderAddressService;
import com.example.demo.service.OrderItemService;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;

import java.util.List;

/**
 * 訂單管理控制器
 * 負責處理訂單相關的請求，包括訂單列表、訂單詳情、建立訂單、取消訂單等操作
 */
@Controller
@RequestMapping("/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderAddressService orderAddressService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    /**
     * 顯示訂單列表頁面（管理員功能）
     * 獲取所有訂單並顯示在列表中
     * 
     * @param model 用於傳遞訂單資料到前端頁面
     * @return 訂單列表頁面模板名稱
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String listOrders(Model model) {
        logger.info("進入訂單列表頁面（管理員）");
        
        try {
            List<Order> orders = orderService.getAll();
            logger.debug("載入訂單列表 - 共 {} 筆訂單", orders.size());
            model.addAttribute("orders", orders);
            logger.info("訂單列表頁面載入完成");
        } catch (Exception e) {
            logger.error("載入訂單列表時發生錯誤", e);
            model.addAttribute("error", "載入訂單列表失敗：" + e.getMessage());
        }
        
        return "orders";
    }

    /**
     * 顯示我的訂單列表頁面（用戶功能）
     * 獲取當前登入用戶的所有訂單並顯示，支援分頁功能
     * 
     * @param page 頁碼（從 0 開始，預設為 0）
     * @param size 每頁顯示數量（預設為 10）
     * @param model 用於傳遞訂單資料到前端頁面
     * @return 我的訂單列表頁面模板名稱
     */
    @GetMapping("/my")
    public String listMyOrders(@RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size,
                               Model model) {
        logger.info("進入我的訂單列表頁面 - page: {}, size: {}", page, size);
        
        try {
            // 取得目前登入的用戶
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
                logger.warn("用戶未登入，無法查看訂單");
                return "redirect:/login";
            }
            
            User currentUser = userService.getByUsername(auth.getName());
            logger.debug("目前登入用戶 - username: {}", currentUser.getUsername());

            // 取得客戶資料
            Customer customer = customerService.getByUser(currentUser);
            if (customer == null) {
                logger.warn("客戶資料不存在 - username: {}", currentUser.getUsername());
                model.addAttribute("error", "請先完善您的客戶資料");
                return "redirect:/customers/edit";
            }

            // 取得客戶的所有訂單
            List<Order> allOrders = orderService.getByCustomer(customer);
            logger.info("載入我的訂單列表 - customerId: {}, 共 {} 筆訂單", customer.getId(), allOrders.size());
            
            // 在事務範圍內初始化所有訂單的關聯，確保模板可以訪問
            if (allOrders != null && !allOrders.isEmpty()) {
                for (Order order : allOrders) {
                    // 初始化 customer 關聯，避免 lazy loading 問題
                    if (order.getCustomer() != null) {
                        order.getCustomer().getId(); // 觸發 lazy loading
                    }
                    logger.debug("訂單資訊 - orderId: {}, orderNumber: {}, createdAt: {}, customerId: {}", 
                               order.getId(), order.getOrderNumber(), order.getCreatedAt(),
                               order.getCustomer() != null ? order.getCustomer().getId() : "null");
                }
            } else {
                logger.warn("訂單列表為空 - customerId: {}", customer.getId());
            }
            
            // 分頁計算
            int totalOrders = allOrders != null ? allOrders.size() : 0;
            int totalPages = totalOrders > 0 ? (int) Math.ceil((double) totalOrders / size) : 0;
            
            // 確保頁碼不超出範圍
            if (page < 0) {
                page = 0;
            }
            if (totalPages > 0 && page >= totalPages) {
                page = totalPages - 1;
            }
            
            // 計算當前頁的訂單範圍
            List<Order> paginatedOrders;
            if (totalOrders > 0) {
                int start = page * size;
                int end = Math.min(start + size, totalOrders);
                paginatedOrders = allOrders.subList(start, end);
            } else {
                paginatedOrders = java.util.Collections.emptyList();
            }
            
            model.addAttribute("orders", paginatedOrders);
            model.addAttribute("isMyOrders", true);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalOrders", totalOrders);
            model.addAttribute("pageSize", size);
            
            logger.info("我的訂單列表頁面載入完成 - 總數: {}, 當前頁: {}/{}, 顯示: {} 筆", 
                       totalOrders, page + 1, totalPages, paginatedOrders.size());
            
        } catch (Exception e) {
            logger.error("載入我的訂單列表時發生錯誤", e);
            model.addAttribute("error", "載入訂單列表失敗：" + e.getMessage());
        }
        
        return "orders";
    }

    /**
     * 顯示訂單確認頁面
     * 訂單建立成功後的確認頁面
     * 
     * @param orderId 訂單ID
     * @param model 用於傳遞訂單資料到前端頁面
     * @return 訂單確認頁面模板名稱
     */
    @GetMapping("/confirmation/{orderId}")
    public String orderConfirmation(@PathVariable("orderId") Long orderId, Model model) {
        logger.info("進入訂單確認頁面 - orderId: {}", orderId);
        
        try {
            // 從資料庫查詢訂單（Flash 屬性會在重定向後自動添加到 Model）
            Order order = orderService.getById(orderId);
            
            if (order == null) {
                logger.warn("訂單不存在 - orderId: {}", orderId);
                model.addAttribute("error", "訂單不存在");
                return "redirect:/orders/my";
            }

            // 檢查權限：只能查看自己的訂單，管理員可以查看所有訂單
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
                User currentUser = userService.getByUsername(auth.getName());
                Customer customer = customerService.getByUser(currentUser);
                
                boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
                if (!isAdmin && (customer == null || !order.getCustomer().getId().equals(customer.getId()))) {
                    logger.warn("無權限查看此訂單 - orderId: {}", orderId);
                    model.addAttribute("error", "您沒有權限查看此訂單");
                    return "redirect:/orders/my";
                }
            }

            // 在事務範圍內初始化所有需要的關聯，確保模板可以訪問
            if (order.getCustomer() != null) {
                order.getCustomer().getId(); // 確保 customer 已載入
            }
            if (order.getOrderAddress() != null) {
                order.getOrderAddress().getId(); // 確保 orderAddress 已載入
                order.getOrderAddress().getRecipientName(); // 預載入常用欄位
            }

            // 格式化日期為字串，避免模板中的日期格式化問題
            if (order.getCreatedAt() != null) {
                String formattedDate = order.getCreatedAt().toString().replace('T', ' ').substring(0, 16);
                model.addAttribute("formattedOrderDate", formattedDate);
            }

            model.addAttribute("order", order);
            logger.info("訂單確認頁面載入完成 - orderId: {}, orderNumber: {}, hasOrderAddress: {}", 
                       orderId, order.getOrderNumber(), order.getOrderAddress() != null);
            
        } catch (Exception e) {
            logger.error("載入訂單確認頁面時發生錯誤 - orderId: {}", orderId, e);
            model.addAttribute("error", "載入訂單確認頁面失敗：" + e.getMessage());
            return "redirect:/orders/my";
        }
        
        return "order-confirmation";
    }

    /**
     * 顯示訂單詳情頁面
     * 根據訂單ID獲取訂單詳情並顯示
     * 
     * @param id 訂單ID
     * @param model 用於傳遞訂單資料到前端頁面
     * @return 訂單詳情頁面模板名稱
     */
    @GetMapping("/{id}")
    public String viewOrderDetail(@PathVariable("id") Long id, Model model) {
        logger.info("進入訂單詳情頁面 - orderId: {}", id);
        
        try {
            // 取得訂單
            Order order = orderService.getById(id);
            if (order == null) {
                logger.warn("訂單不存在 - orderId: {}", id);
                model.addAttribute("error", "訂單不存在");
                return "orders";
            }

            // 檢查權限：只能查看自己的訂單，管理員可以查看所有訂單
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
                User currentUser = userService.getByUsername(auth.getName());
                Customer customer = customerService.getByUser(currentUser);
                
                // 檢查是否為管理員或訂單所有者
                boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
                if (!isAdmin && (customer == null || !order.getCustomer().getId().equals(customer.getId()))) {
                    logger.warn("無權限查看此訂單 - orderId: {}, userId: {}", id, currentUser.getId());
                    model.addAttribute("error", "您沒有權限查看此訂單");
                    return "orders";
                }
            }

            // 在事務範圍內初始化所有需要的關聯，確保模板可以訪問
            if (order.getCustomer() != null) {
                order.getCustomer().getId(); // 確保 customer 已載入
            }
            if (order.getOrderAddress() != null) {
                order.getOrderAddress().getId(); // 確保 orderAddress 已載入
                order.getOrderAddress().getRecipientName(); // 預載入常用欄位
            }

            // 取得訂單項目
            List<OrderItem> orderItems = orderItemService.getByOrderId(id);
            logger.debug("載入訂單項目 - orderId: {}, 共 {} 項", id, orderItems.size());

            // 格式化日期為字串，避免模板中的日期格式化問題
            String formattedDate = null;
            if (order.getCreatedAt() != null) {
                formattedDate = order.getCreatedAt().toString().replace('T', ' ').substring(0, 16);
            }

            model.addAttribute("order", order);
            model.addAttribute("orderItems", orderItems);
            model.addAttribute("formattedOrderDate", formattedDate);
            logger.info("訂單詳情頁面載入完成 - orderId: {}, orderNumber: {}, hasOrderAddress: {}", 
                       id, order.getOrderNumber(), order.getOrderAddress() != null);
            
        } catch (Exception e) {
            logger.error("載入訂單詳情時發生錯誤 - orderId: {}", id, e);
            model.addAttribute("error", "載入訂單詳情失敗：" + e.getMessage());
        }
        
        return "order-detail";
    }

    /**
     * 顯示結帳頁面
     * 從購物車進入結帳流程，顯示訂單資訊供確認
     * 
     * @param model 用於傳遞購物車和訂單資料到前端頁面
     * @return 結帳頁面模板名稱
     */
    @GetMapping("/checkout")
    public String showCheckout(Model model) {
        logger.info("進入結帳頁面");
        
        try {
            // 取得目前登入的用戶
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
                logger.warn("用戶未登入，無法結帳");
                return "redirect:/login";
            }
            
            User currentUser = userService.getByUsername(auth.getName());
            logger.debug("目前登入用戶 - username: {}", currentUser.getUsername());

            // 取得客戶資料
            Customer customer = customerService.getByUser(currentUser);
            if (customer == null) {
                logger.warn("客戶資料不存在 - username: {}", currentUser.getUsername());
                model.addAttribute("error", "請先完善您的客戶資料");
                return "redirect:/customers/edit";
            }

            // 取得購物車
            Cart cart = cartService.getByCustomer(customer);
            if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
                logger.warn("購物車為空或不存在 - customerId: {}", customer.getId());
                model.addAttribute("error", "購物車為空，無法結帳");
                return "redirect:/cart";
            }

            // 預填收件人資訊（使用客戶資料）
            model.addAttribute("cart", cart);
            model.addAttribute("recipientName", customer.getFullName());
            model.addAttribute("recipientPhone", customer.getPhone());
            
            // 添加付款方式選項
            model.addAttribute("paymentMethods", PaymentMethod.values());
            
            logger.info("結帳頁面載入完成 - cartId: {}", cart.getId());
            
        } catch (Exception e) {
            logger.error("載入結帳頁面時發生錯誤", e);
            model.addAttribute("error", "載入結帳頁面失敗：" + e.getMessage());
            return "redirect:/cart";
        }
        
        return "checkout";
    }

    /**
     * 提交訂單（結帳）
     * 從購物車建立訂單，並清空購物車
     * 
     * @param recipientName 收件人姓名
     * @param recipientPhone 收件人電話
     * @param streetAddress 街道地址
     * @param country 國家
     * @param city 城市
     * @param district 區/鄉鎮
     * @param postCode 郵遞區號
     * @param paymentMethod 付款方式
     * @param notes 訂單備註
     * @param redirectAttributes 用於傳遞重定向訊息
     * @return 重定向到訂單確認頁面
     */
    @PostMapping("/create")
    public String createOrder(@RequestParam("recipientName") String recipientName,
                             @RequestParam(value = "recipientPhone", required = false) String recipientPhone,
                             @RequestParam("streetAddress") String streetAddress,
                             @RequestParam(value = "country", required = false) String country,
                             @RequestParam(value = "city", required = false) String city,
                             @RequestParam(value = "district", required = false) String district,
                             @RequestParam(value = "postCode", required = false) String postCode,
                             @RequestParam("paymentMethod") String paymentMethod,
                             @RequestParam(value = "notes", required = false) String notes,
                             RedirectAttributes redirectAttributes) {
        logger.info("建立訂單 - recipientName: {}, paymentMethod: {}, streetAddress: {}", 
                   recipientName, paymentMethod, streetAddress);
        
        // 驗證必填欄位
        if (recipientName == null || recipientName.trim().isEmpty()) {
            logger.warn("收件人姓名為空");
            redirectAttributes.addFlashAttribute("error", "收件人姓名不能為空");
            return "redirect:/orders/checkout";
        }
        
        if (streetAddress == null || streetAddress.trim().isEmpty()) {
            logger.warn("街道地址為空");
            redirectAttributes.addFlashAttribute("error", "街道地址不能為空");
            return "redirect:/orders/checkout";
        }
        
        try {
            // 取得目前登入的用戶
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
                logger.warn("用戶未登入，無法建立訂單");
                return "redirect:/login";
            }
            
            User currentUser = userService.getByUsername(auth.getName());
            logger.debug("目前登入用戶 - username: {}", currentUser.getUsername());

            // 取得客戶資料
            Customer customer = customerService.getByUser(currentUser);
            if (customer == null) {
                logger.warn("客戶資料不存在");
                redirectAttributes.addFlashAttribute("error", "請先完善您的客戶資料");
                return "redirect:/customers/edit";
            }

            // 取得購物車
            Cart cart = cartService.getByCustomer(customer);
            if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
                logger.warn("購物車為空或不存在");
                redirectAttributes.addFlashAttribute("error", "購物車為空，無法建立訂單");
                return "redirect:/cart";
            }

            // 驗證庫存
            for (CartItem cartItem : cart.getCartItems()) {
                Product product = cartItem.getProduct();
                if (!product.hasEnoughStock(cartItem.getQuantity())) {
                    logger.warn("庫存不足 - productId: {}, requested: {}, available: {}", 
                               product.getId(), cartItem.getQuantity(), product.getStockQuantity());
                    redirectAttributes.addFlashAttribute("error", 
                        "商品「" + product.getName() + "」庫存不足，目前庫存：" + product.getStockQuantity());
                    return "redirect:/cart";
                }
            }

            // 轉換付款方式
            PaymentMethod paymentMethodEnum;
            try {
                paymentMethodEnum = PaymentMethod.valueOf(paymentMethod);
            } catch (IllegalArgumentException e) {
                logger.warn("無效的付款方式 - paymentMethod: {}", paymentMethod);
                redirectAttributes.addFlashAttribute("error", "無效的付款方式");
                return "redirect:/orders/checkout";
            }

            // 建立訂單
            Order order = orderService.createOrder(customer, paymentMethodEnum);
            
            // 設定其他訂單資訊
            if (notes != null && !notes.isEmpty()) {
                order.setNotes(notes);
                order = orderService.saveOrder(order);
            }

            // 建立訂單地址
            OrderAddress orderAddress = orderAddressService.createOrderAddress(
                order, recipientName, 
                recipientPhone != null ? recipientPhone : "", 
                streetAddress != null ? streetAddress : "",
                country != null ? country : "",
                city != null ? city : "",
                district != null ? district : "",
                postCode != null ? postCode : "");
            
            // 設置雙向關聯並保存
            order.setOrderAddress(orderAddress);
            order = orderService.saveOrder(order);

            // 從購物車項目建立訂單項目
            for (CartItem cartItem : cart.getCartItems()) {
                OrderItem orderItem = orderItemService.createOrderItem(order, cartItem.getProduct(), cartItem.getQuantity());
                logger.debug("建立訂單項目 - orderItemId: {}, productId: {}, quantity: {}", 
                           orderItem.getId(), cartItem.getProduct().getId(), cartItem.getQuantity());
            }

            // 重新計算訂單金額
            order = orderService.recalculateOrderAmount(order.getId());
            
            // 減少商品庫存
            for (CartItem cartItem : cart.getCartItems()) {
                Product product = cartItem.getProduct();
                product.reduceStock(cartItem.getQuantity());
                productService.updateProduct(product.getId(), product);
            }

            // 清空購物車
            cartService.clearCart(cart.getId());
            logger.info("訂單建立成功並清空購物車 - orderId: {}, orderNumber: {}", order.getId(), order.getOrderNumber());

            // 重定向到訂單確認頁面
            redirectAttributes.addFlashAttribute("success", "訂單建立成功！訂單編號：" + order.getOrderNumber());
            redirectAttributes.addFlashAttribute("order", order);
            redirectAttributes.addAttribute("orderId", order.getId());
            return "redirect:/orders/confirmation/{orderId}";
            
        } catch (Exception e) {
            logger.error("建立訂單時發生錯誤", e);
            redirectAttributes.addFlashAttribute("error", "建立訂單失敗：" + e.getMessage());
            return "redirect:/orders/checkout";
        }
    }

    /**
     * 取消訂單
     * 用戶可以取消自己的訂單（如果符合條件）
     * 
     * @param id 訂單ID
     * @param reason 取消原因
     * @param redirectAttributes 用於傳遞重定向訊息
     * @return 重定向到訂單列表頁面
     */
    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable("id") Long id,
                             @RequestParam(value = "reason", required = false) String reason,
                             RedirectAttributes redirectAttributes) {
        logger.info("取消訂單 - orderId: {}, reason: {}", id, reason);
        
        try {
            // 取得訂單
            Order order = orderService.getById(id);
            if (order == null) {
                logger.warn("訂單不存在 - orderId: {}", id);
                redirectAttributes.addFlashAttribute("error", "訂單不存在");
                return "redirect:/orders/my";
            }

            // 檢查權限
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
                logger.warn("用戶未登入");
                return "redirect:/login";
            }
            
            User currentUser = userService.getByUsername(auth.getName());
            Customer customer = customerService.getByUser(currentUser);
            
            boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            
            if (!isAdmin && (customer == null || !order.getCustomer().getId().equals(customer.getId()))) {
                logger.warn("無權限取消此訂單 - orderId: {}", id);
                redirectAttributes.addFlashAttribute("error", "您沒有權限取消此訂單");
                return "redirect:/orders/my";
            }

            // 檢查是否可以取消
            if (!orderService.canCancelOrder(id)) {
                logger.warn("訂單無法取消 - orderId: {}, status: {}", id, order.getStatus());
                redirectAttributes.addFlashAttribute("error", "此訂單無法取消，目前狀態：" + order.getStatus().getDescription());
                return "redirect:/orders/my";
            }

            // 取消訂單（會自動恢復庫存）
            Order cancelledOrder = orderService.cancelOrder(id, reason);
            logger.info("訂單已取消 - orderId: {}, orderNumber: {}", cancelledOrder.getId(), cancelledOrder.getOrderNumber());
            
            redirectAttributes.addFlashAttribute("success", "訂單已成功取消");
            
        } catch (Exception e) {
            logger.error("取消訂單時發生錯誤 - orderId: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "取消訂單失敗：" + e.getMessage());
        }
        
        return "redirect:/orders/my";
    }

    /**
     * 更新訂單狀態（管理員功能）
     * 管理員可以更新訂單狀態
     * 
     * @param id 訂單ID
     * @param status 新狀態
     * @param redirectAttributes 用於傳遞重定向訊息
     * @return 重定向到訂單詳情頁面
     */
    @PostMapping("/{id}/update-status")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateOrderStatus(@PathVariable("id") Long id,
                                   @RequestParam("status") String status,
                                   RedirectAttributes redirectAttributes) {
        logger.info("更新訂單狀態 - orderId: {}, status: {}", id, status);
        
        try {
            OrderStatus newStatus;
            try {
                newStatus = OrderStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                logger.warn("無效的訂單狀態 - status: {}", status);
                redirectAttributes.addFlashAttribute("error", "無效的訂單狀態");
                redirectAttributes.addAttribute("id", id);
                return "redirect:/orders/{id}";
            }

            orderService.updateOrderStatus(id, newStatus);
            logger.info("訂單狀態已更新 - orderId: {}, newStatus: {}", id, newStatus);
            
            redirectAttributes.addFlashAttribute("success", "訂單狀態已更新為：" + newStatus.getDescription());
            
        } catch (Exception e) {
            logger.error("更新訂單狀態時發生錯誤 - orderId: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "更新訂單狀態失敗：" + e.getMessage());
        }
        
        redirectAttributes.addAttribute("id", id);
        return "redirect:/orders/{id}";
    }

    /**
     * 更新付款狀態（管理員功能）
     * 管理員可以更新訂單的付款狀態
     * 
     * @param id 訂單ID
     * @param paymentStatus 新付款狀態
     * @param redirectAttributes 用於傳遞重定向訊息
     * @return 重定向到訂單詳情頁面
     */
    @PostMapping("/{id}/update-payment-status")
    @PreAuthorize("hasRole('ADMIN')")
    public String updatePaymentStatus(@PathVariable("id") Long id,
                                     @RequestParam("paymentStatus") String paymentStatus,
                                     RedirectAttributes redirectAttributes) {
        logger.info("更新付款狀態 - orderId: {}, paymentStatus: {}", id, paymentStatus);
        
        try {
            PaymentStatus newPaymentStatus;
            try {
                newPaymentStatus = PaymentStatus.valueOf(paymentStatus);
            } catch (IllegalArgumentException e) {
                logger.warn("無效的付款狀態 - paymentStatus: {}", paymentStatus);
                redirectAttributes.addFlashAttribute("error", "無效的付款狀態");
                redirectAttributes.addAttribute("id", id);
                return "redirect:/orders/{id}";
            }

            orderService.updatePaymentStatus(id, newPaymentStatus);
            logger.info("付款狀態已更新 - orderId: {}, newPaymentStatus: {}", id, newPaymentStatus);
            
            redirectAttributes.addFlashAttribute("success", "付款狀態已更新為：" + newPaymentStatus.getDescription());
            
        } catch (Exception e) {
            logger.error("更新付款狀態時發生錯誤 - orderId: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "更新付款狀態失敗：" + e.getMessage());
        }
        
        redirectAttributes.addAttribute("id", id);
        return "redirect:/orders/{id}";
    }
}

