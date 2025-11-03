## Git Commit Message

### Type: feat
### Scope: account-ui
### Subject: 優化會員中心與客戶資料頁面 UI/UX

### Body:
優化會員中心（account.html）和客戶資料管理頁面（customer-profile.html、edit-customer.html）的 UI/UX，修正模板渲染錯誤，並統一頁面樣式與 products 頁面保持一致。

**主要變更:**

1. **account.html - 會員中心頁面**
   - 修正模板渲染錯誤（ERR_INCOMPLETE_CHUNKED_ENCODING）
     - 修正 Thymeleaf 表達式語法（`and` 改為 `&&`，`or` 改為 `||`）
     - 添加所有欄位的 null 檢查，避免 NullPointerException
     - 修正訂單狀態顯示：將多個 `th:classappend` 合併為單一的 `th:class` 表達式
     - 修正訂單詳情連結的表達式
   - 添加缺失的 CSS 樣式
     - 添加 `.header-gradient` 樣式（頁面頂部標題區塊）
     - 添加 `.btn` 和 `.btn-primary` 樣式（按鈕樣式）
   - 修正訂單統計計算邏輯
     - 問題：原本只使用最近 5 筆訂單來計算總訂單數，導致統計不正確
     - 修正：先獲取客戶的所有訂單計算統計，再取最近 5 筆顯示
     - 添加排序邏輯：按建立時間降序排序，並過濾 null 的 createdAt
   - 修正所有連結使用 Thymeleaf URL 重寫
     - 將硬編碼的 `href="/shop"` 等改為 `th:href="@{/shop}"`
     - 確保連結正確處理應用程式上下文路徑（`/eshop`）
   - 改進統計卡片互動
     - 將 `onclick` 改為使用 `<a>` 標籤包裹，符合語義化 HTML

2. **AccountController.java - 會員中心控制器**
   - 修正訂單統計計算邏輯
     - 獲取所有訂單後再計算總數和待處理訂單數
     - 對訂單進行排序（按建立時間降序）
     - 添加 null 檢查，過濾掉 `createdAt` 為 null 的訂單
     - 確保統計資料正確且頁面能正常顯示

3. **customer-profile.html - 個人資料頁面**
   - 統一頁面樣式與 products 頁面一致
     - 添加 Notice 元件支援顯示成功/錯誤訊息
     - 添加 Logo 區塊（頁面頂部中央）
     - 使用 `product-list-container` 和 `product-card` 樣式
     - 使用 `product-info`、`product-info-item`、`product-info-label`、`product-info-value` 樣式
     - 資訊顯示格式與商品列表一致（標籤和值並排顯示）
     - 按鈕樣式統一為 `btn btn-primary`
   - 改進頁面布局
     - 容器距頂 350px（與 Logo 間距）
     - 最大寬度 1200px，卡片最小寬度 800px
     - 響應式 flexbox 布局

4. **edit-customer.html - 編輯客戶資料頁面**
   - 統一表單樣式與 add-product/edit-product 頁面一致
     - 添加 Notice 元件支援顯示成功/錯誤訊息
     - 添加 Logo 區塊（頁面頂部中央）
     - 使用 `login-row` 和 `loginRegForm` 類
     - 添加右上角返回首頁圖標（`home-icon-top-right`）
     - 表單欄位使用 `form-group` 和 `input-row` 類
     - 添加錯誤訊息顯示區域（`error-message`）
     - 按鈕布局與 product 頁面一致（包含「更新資料」和「取消」按鈕）

**問題修復:**

- **問題**: 會員中心頁面出現 `ERR_INCOMPLETE_CHUNKED_ENCODING` 錯誤
  - **原因**: 
    1. Thymeleaf 表達式語法錯誤（使用 `and`/`or` 而非 `&&`/`||`）
    2. 缺少 null 檢查，導致日期格式化等操作失敗
    3. 同一個元素上使用了多個 `th:classappend` 屬性（Thymeleaf 不允許）
    4. 訂單統計計算錯誤（只計算最近 5 筆而非全部）
  - **解決**: 
    1. 修正所有 Thymeleaf 表達式語法
    2. 添加完整的 null 檢查
    3. 將多個 `th:classappend` 合併為單一 `th:class` 表達式
    4. 修正訂單統計計算邏輯

- **問題**: 點選購物、購物車等連結都 404
  - **原因**: 連結使用硬編碼路徑，未考慮應用程式上下文路徑（`/eshop`）
  - **解決**: 將所有連結改為使用 Thymeleaf 的 `@{...}` URL 重寫語法

- **問題**: 客戶資料頁面樣式不一致
  - **原因**: customer-profile 和 edit-customer 頁面使用舊的表格樣式
  - **解決**: 統一為與 products 頁面一致的卡片式布局和表單樣式

**技術細節:**

- 使用 Thymeleaf 的 URL 重寫功能（`@{...}`）自動處理上下文路徑
- 訂單排序使用 Java 8 Stream API，確保資料正確排序
- 模板渲染時添加完整的 null 檢查，避免渲染錯誤
- CSS 樣式統一使用相同的類別，保持視覺一致性

**UI/UX 改進:**

- 會員中心頁面現在能正確顯示用戶資訊、訂單統計和最近的訂單
- 所有頁面統一視覺風格，提升用戶體驗
- 連結正確導航，不再出現 404 錯誤
- 表單樣式統一，操作更加直觀
