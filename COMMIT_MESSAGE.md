## Git Commit Message

### Type: feat
### Scope: notice-modal-validation
### Subject: 添加確認刪除 Modal、成功/失敗 Notice 提示，以及商品價格驗證

### Body:
實現通用的確認刪除 Modal 元件，整合成功/失敗 Notice 提示功能，並添加商品價格驗證（最小值為 0）。

**主要變更:**

1. **確認刪除 Modal 元件**
   - 創建 `fragments/confirm-modal.html` Thymeleaf Fragment
   - 添加 Modal CSS 樣式（`loginReg.css`）：
     - `.modal` - 遮罩層和動畫效果
     - `.modal-content` - Modal 內容容器（半透明背景）
     - `.modal-header`, `.modal-body`, `.modal-footer` - Modal 結構
     - `.btn-danger` - 確認刪除按鈕樣式
     - 響應式設計（手機端適配）
   - 創建 `confirm-modal.js` JavaScript 功能：
     - `showConfirmModal()` - 顯示確認 Modal
     - `closeConfirmModal()` - 關閉 Modal
     - `confirmDelete()` - 確認刪除操作
     - 自動綁定刪除按鈕事件（支援 `data-delete-url` 和 `data-delete-form` 屬性）
     - 支援 GET 和 POST 請求
     - ESC 鍵和背景點擊關閉功能

2. **Controller 成功/失敗訊息處理**
   - **ProductController.java**：
     - `saveUser()` - 新增商品時添加成功/失敗訊息
     - `updateProduct()` - 更新商品時添加成功/失敗訊息
     - `deleteProduct()` - 刪除商品時添加成功/失敗訊息
     - 使用 `RedirectAttributes` 傳遞 flash 訊息
     - 添加 `try-catch` 錯誤處理
   - **UserController.java**：
     - `saveUser()` - 新增用戶時添加成功/失敗訊息
     - `updateUser()` - 更新用戶時添加成功/失敗訊息
     - `deleteUser()` - 刪除用戶時添加成功/失敗訊息
     - 使用 `RedirectAttributes` 傳遞 flash 訊息
     - 添加 `try-catch` 錯誤處理

3. **商品價格驗證**
   - **前端 HTML (add-product.html, edit-product.html)**：
     - 價格輸入欄位添加 `min="0"` 屬性
     - 將 `step` 從 `"1"` 改為 `"0.01"` 支援小數
     - 添加 `validatePrice()` JavaScript 驗證函數
     - 表單提交前檢查價格是否為負數
   - **後端模型 (Product.java)**：
     - 添加 `@DecimalMin(value = "0.00", message = "價格不能為負數，最小值為 0 元")` 驗證註解
     - 添加 `@NotNull(message = "價格不能為空")` 驗證註解
     - 將資料庫欄位的 `scale` 從 `0` 改為 `2` 支援小數
   - **Controller 驗證 (ProductController.java)**：
     - `saveUser()` 和 `updateProduct()` 使用 `@Valid` 驗證
     - 添加 `BindingResult` 處理驗證錯誤
     - 驗證失敗時返回錯誤訊息並重定向

4. **頁面整合**
   - **products.html**：
     - 添加確認刪除 Modal Fragment
     - 刪除按鈕使用 `data-delete-form` 屬性綁定 Modal
     - 每個商品都有隱藏的刪除表單（`display:none`）
     - 整合 Notice 元件顯示成功/失敗訊息
   - **users.html**：
     - 添加確認刪除 Modal Fragment
     - 刪除按鈕使用 `data-delete-url` 屬性（GET 請求）
     - 整合 Notice 元件
     - 更新中文標籤
   - **add-product.html, edit-product.html**：
     - 整合 Notice 元件
     - 添加價格驗證 JavaScript
   - **add-user.html, edit-user.html**：
     - 整合 Notice 元件

5. **Notice 元件增強**
   - 更新 `notice.js` 自動處理 Thymeleaf flash attributes
   - 支援從 `success` 和 `error` flash attributes 自動顯示 Notice

**技術細節:**

- Modal 使用 flexbox 居中顯示
- 支援動畫效果（fadeIn, slideDown）
- 防止背景滾動（`overflow: hidden`）
- 表單驗證使用 Bean Validation（JSR-303）
- 價格使用 `BigDecimal` 類型確保精度

**影響範圍:**
- 所有刪除操作都需要確認
- 所有新增/修改/刪除操作都會顯示成功/失敗訊息
- 商品價格不能為負數

**測試建議:**
1. 測試刪除商品/用戶時的確認 Modal
2. 測試新增/修改商品時輸入負數價格的驗證
3. 測試成功/失敗 Notice 的顯示和自動消失
4. 測試 Modal 的 ESC 鍵和背景點擊關閉功能
