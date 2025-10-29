package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Product;
import com.example.demo.model.Product.ProductStatus;
import com.example.demo.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 前台商品展示控制器
 * 負責處理前台商品展示相關的請求，包括商品列表、商品詳情、分類瀏覽等
 */
@Controller
@RequestMapping("/shop")
public class ShopController {

    private static final Logger logger = LoggerFactory.getLogger(ShopController.class);

    @Autowired
    private ProductService productService;

    /**
     * 顯示商品展示頁面（前台）
     * 顯示所有上架中的商品，支援分類篩選和搜尋
     * 
     * @param type 商品類型（分類篩選）
     * @param search 搜尋關鍵字
     * @param model 用於傳遞商品資料到前端頁面
     * @return 商品展示頁面模板名稱
     */
    @GetMapping
    public String shopPage(@RequestParam(value = "type", required = false) String type,
                          @RequestParam(value = "search", required = false) String search,
                          Model model) {
        logger.info("進入商品展示頁面 - type: {}, search: {}", type, search);
        
        try {
            // 取得所有商品
            List<Product> allProducts = productService.getAll();
            
            // 只顯示上架中的商品
            List<Product> activeProducts = allProducts.stream()
                    .filter(p -> p.getStatus() == ProductStatus.ACTIVE)
                    .collect(Collectors.toList());
            
            // 根據分類篩選
            List<Product> filteredProducts = activeProducts;
            if (type != null && !type.isEmpty()) {
                filteredProducts = activeProducts.stream()
                        .filter(p -> p.getType().equalsIgnoreCase(type))
                        .collect(Collectors.toList());
                logger.debug("根據分類篩選 - type: {}, 結果數量: {}", type, filteredProducts.size());
            }
            
            // 根據搜尋關鍵字篩選
            if (search != null && !search.isEmpty()) {
                String searchLower = search.toLowerCase();
                filteredProducts = filteredProducts.stream()
                        .filter(p -> p.getName().toLowerCase().contains(searchLower) ||
                                   (p.getDescription() != null && p.getDescription().toLowerCase().contains(searchLower)))
                        .collect(Collectors.toList());
                logger.debug("根據關鍵字篩選 - search: {}, 結果數量: {}", search, filteredProducts.size());
            }
            
            // 取得所有商品類型（用於分類選單）
            List<String> categories = activeProducts.stream()
                    .map(Product::getType)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
            
            model.addAttribute("products", filteredProducts);
            model.addAttribute("categories", categories);
            model.addAttribute("selectedType", type);
            model.addAttribute("searchKeyword", search);
            
            logger.info("商品展示頁面載入完成 - 商品數量: {}", filteredProducts.size());
            
        } catch (Exception e) {
            logger.error("載入商品展示頁面時發生錯誤", e);
            model.addAttribute("error", "載入商品列表失敗：" + e.getMessage());
        }
        
        return "shop";
    }

    /**
     * 顯示商品詳情頁面（前台）
     * 根據商品ID顯示商品詳細資訊
     * 
     * @param id 商品ID
     * @param model 用於傳遞商品資料到前端頁面
     * @return 商品詳情頁面模板名稱
     */
    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        logger.info("進入商品詳情頁面 - productId: {}", id);
        
        try {
            Product product = productService.getById(id);
            
            if (product == null) {
                logger.warn("商品不存在 - productId: {}", id);
                model.addAttribute("error", "商品不存在");
                return "redirect:/shop";
            }
            
            // 只有上架中的商品才能查看詳情
            if (product.getStatus() != ProductStatus.ACTIVE) {
                logger.warn("商品已下架 - productId: {}, status: {}", id, product.getStatus());
                model.addAttribute("error", "此商品已下架");
                return "redirect:/shop";
            }
            
            model.addAttribute("product", product);
            logger.info("商品詳情頁面載入完成 - productId: {}, name: {}", id, product.getName());
            
        } catch (Exception e) {
            logger.error("載入商品詳情時發生錯誤 - productId: {}", id, e);
            model.addAttribute("error", "載入商品詳情失敗：" + e.getMessage());
            return "redirect:/shop";
        }
        
        return "product-detail";
    }

    /**
     * 從商品詳情頁加入購物車
     * 直接從商品詳情頁將商品添加到購物車
     * 
     * @param productId 商品ID
     * @param quantity 商品數量，預設為1
     * @param redirectAttributes 用於傳遞重定向訊息
     * @return 重定向到購物車頁面
     */
    @PostMapping("/product/{productId}/add-to-cart")
    public String addToCartFromDetail(@PathVariable("productId") Long productId,
                                      @RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
                                      RedirectAttributes redirectAttributes) {
        logger.info("從商品詳情頁加入購物車 - productId: {}, quantity: {}", productId, quantity);
        
        // 重定向到購物車的添加端點
        redirectAttributes.addAttribute("productId", productId);
        redirectAttributes.addAttribute("quantity", quantity);
        return "redirect:/cart/add";
    }

    /**
     * 顯示分類商品列表頁面
     * 根據商品類型顯示該分類的所有商品
     * 
     * @param category 商品分類
     * @param model 用於傳遞商品資料到前端頁面（目前未使用，但保留以備將來擴展）
     * @return 重定向到商品展示頁面並帶上分類參數
     */
    @GetMapping("/category/{category}")
    public String categoryPage(@PathVariable("category") String category, Model model) {
        logger.info("進入分類商品頁面 - category: {}", category);
        
        // 重定向到 shop 頁面並帶上分類參數
        return "redirect:/shop?type=" + category;
    }
}

