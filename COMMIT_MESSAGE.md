## Git Commit Message

### Type: refactor
### Scope: products-ui
### Subject: 重構商品列表頁面樣式並添加分頁功能，優化商店頁面圖片連結

### Body:
重構商品管理列表頁面，將傳統表格改為卡片式設計（類似登入頁面表單風格），添加分頁功能（每頁 10 個商品），並優化商店頁面商品圖片可點擊跳轉到詳情頁。

**主要變更:**

1. **商品列表頁面重構 (products.html)**
   - 移除傳統 `<table>` 表格，改用卡片式設計
   - 每個商品使用 `.product-card` 卡片容器
   - 使用與登入頁面相同的背景色和樣式（`rgba(158, 154, 154, 0.5)`）
   - 添加 hover 效果（背景色加深）
   - 商品資訊使用 flex 布局顯示：
     - ID、名稱、類型、價格、狀態
     - 商品圖片（80x80px，圓角）
   - 操作按鈕：編輯、刪除（使用統一的 `.btn btn-primary` 樣式）
   - 添加 Notice 元件、Logo、返回首頁功能
   - 響應式設計，適應不同螢幕尺寸

2. **分頁功能實現**
   - **後端 (ProductController.java)**：
     - 添加 `page` 和 `size` 參數（默認：page=0, size=10）
     - 計算總頁數和當前頁的商品範圍
     - 頁碼範圍驗證（防止越界）
     - 向 Model 傳遞分頁資訊（currentPage, totalPages, totalProducts, pageSize）
   - **前端 (products.html)**：
     - 分頁導航 UI：
       - 上一頁/下一頁按鈕（自動禁用邊界狀態）
       - 頁碼按鈕（顯示所有頁碼，使用 `#numbers.sequence()`）
       - 當前頁高亮顯示（`.active` 類別）
       - 分頁資訊顯示（第 X 頁 / 共 Y 頁，共 Z 筆商品）
     - 響應式分頁導航（手機端自動換行）

3. **商店頁面優化 (shop.html)**
   - 將商品圖片包裝在 `<a>` 標籤中
   - 點擊商品圖片可跳轉到商品詳情頁面（`/shop/product/{id}`）
   - 添加 hover 效果（opacity: 0.8）提示可點擊
   - 添加 `cursor: pointer` 樣式

4. **CSS 樣式設計**
   - `.product-card` - 商品卡片容器（圓角、陰影、hover 效果）
   - `.product-info` - 商品資訊 flex 容器
   - `.product-info-item` - 單個資訊項（標籤 + 值）
   - `.product-info-label` - 標籤樣式（白色，右對齊）
   - `.product-info-value` - 值樣式（`#fbd5ba` 顏色）
   - `.product-image` - 商品圖片樣式（80x80px，圓角）
   - `.product-actions` - 操作按鈕容器
   - `.pagination` - 分頁導航容器
   - `.pagination-btn` - 分頁按鈕樣式
   - `.pagination-btn.active` - 當前頁按鈕高亮
   - `.pagination-btn.disabled` - 禁用狀態按鈕

**影響範圍:**
- `ProductController.java` - 添加分頁邏輯（+46 行變更）
- `products.html` - 完全重構（+260 行變更，90% 重寫）
- `shop.html` - 優化圖片連結（+21 行變更）

**統計資料:**
- 新增代碼：275 行
- 刪除代碼：52 行
- 淨增加：223 行
- 受影響檔案：3 個

**分頁功能詳細說明:**
```java
// 後端分頁參數
@RequestParam(value = "page", defaultValue = "0") int page
@RequestParam(value = "size", defaultValue = "10") int size

// 分頁計算
int totalPages = totalProducts > 0 ? (int) Math.ceil((double) totalProducts / size) : 0;
int start = page * size;
int end = Math.min(start + size, totalProducts);
List<Product> paginatedProducts = allProducts.subList(start, end);
```

**分頁 URL 範例:**
- `/products` - 第 1 頁（預設）
- `/products?page=0` - 第 1 頁
- `/products?page=1` - 第 2 頁
- `/products?page=2&size=10` - 第 3 頁，每頁 10 個

**視覺改進:**
- ✅ 商品列表從表格改為現代化的卡片設計
- ✅ 視覺風格與認證頁面統一（相同的背景色和樣式）
- ✅ 每頁顯示 10 個商品，提升載入速度
- ✅ 分頁導航清晰易懂（上一頁、頁碼、下一頁、資訊）
- ✅ 商品圖片可點擊，提升用戶體驗
- ✅ 響應式設計，適應不同螢幕
- ✅ 統一的按鈕和操作樣式

**技術細節:**
- 使用 Thymeleaf 的 `#numbers.sequence()` 生成頁碼列表
- 使用 `Math.ceil()` 計算總頁數
- 使用 `List.subList()` 實現分頁切片
- 卡片式布局使用 Flexbox
- 分頁按鈕使用條件判斷顯示禁用狀態
- 圖片連結使用 Thymeleaf URL 表達式

### Breaking Changes: 無

### Related Issues: 重構商品列表頁面、添加分頁功能、優化用戶體驗

---

## 簡化版本（單行）

```
refactor(products-ui): 重構商品列表頁面樣式並添加分頁功能，優化商店頁面圖片連結

將商品列表從表格改為卡片式設計（類似登入頁面風格），添加分頁功能
（每頁 10 個商品）。後端添加分頁邏輯，前端實現分頁導航 UI。同時優化
商店頁面，讓商品圖片可點擊跳轉到詳情頁。
```

---

## 使用方式

```bash
git add .
git commit -F COMMIT_MESSAGE.md
```

或者直接使用簡化版本：

```bash
git add .
git commit -m "refactor(products-ui): 重構商品列表頁面樣式並添加分頁功能，優化商店頁面圖片連結

將商品列表從表格改為卡片式設計（類似登入頁面風格），添加分頁功能
（每頁 10 個商品）。後端添加分頁邏輯，前端實現分頁導航 UI。同時優化
商店頁面，讓商品圖片可點擊跳轉到詳情頁。"
```
