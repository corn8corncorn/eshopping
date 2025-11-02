## Git Commit Message

### Type: refactor
### Scope: views
### Subject: 將頁首樣式統一提取到 CSS 檔案

### Body:
重構前端樣式架構，將所有 HTML 內聯的 `.header` 樣式提取到 `loginReg.css` 統一管理，提升程式碼整潔度和維護性。

**主要變更:**
1. **loginReg.css 新增統一頁首樣式**
   - `.header` - 基礎頁首樣式（用於大部分頁面）
   - `.header-gradient` - 漸層背景頁首（用於會員中心等特殊頁面）
   - `.header-simple` - 簡化頁首（僅有 margin-bottom）
   - `.header-content`, `.nav`, `.cart-icon`, `.cart-badge` 等相關樣式

2. **移除 HTML 內聯樣式**
   - 從 10 個視圖檔案中移除 `.header` 相關樣式定義
   - 保持 HTML 結構不變，樣式現在統一從 CSS 檔案載入

**影響範圍:**
更新的視圖檔案：
- `account.html` - 移除漸層頁首樣式，改用 `.header-gradient` 類別
- `cart.html` - 移除頁首樣式定義
- `checkout.html` - 移除頁首樣式定義
- `home.html` - 移除頁首及導航樣式定義
- `order-confirmation.html` - 移除頁首樣式定義
- `order-detail.html` - 移除頁首樣式定義
- `orders.html` - 移除頁首樣式定義
- `product-detail.html` - 移除頁首樣式定義
- `search-results.html` - 移除頁首樣式定義
- `shop.html` - 移除頁首樣式定義

**優點:**
- ✅ 降低程式碼重複度（DRY 原則）
- ✅ 提升維護效率（樣式集中管理）
- ✅ 增強程式碼可讀性（HTML 更簡潔）
- ✅ 統一的視覺風格
- ✅ 易於未來擴展和調整

**技術細節:**
- 保留原有視覺效果
- 使用 CSS 類別選擇器提供靈活性
- 支援響應式設計
- 使用 Thymeleaf 路徑解析載入資源

### Breaking Changes: 無

### Related Issues: 重構前端樣式架構、提升程式碼整潔度

---

## 簡化版本（單行）

```
refactor(views): 將頁首樣式統一提取到 CSS 檔案

將所有 HTML 內聯的 .header 樣式提取到 loginReg.css 統一管理。
從 10 個視圖檔案中移除重複樣式定義，降低程式碼重複度並提升
維護效率。
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
git commit -m "chore(views): 統一所有視圖頁面引入 CSS 樣式表

在所有 21 個 HTML 視圖頁面中新增 loginReg.css 樣式表引入，確保
前端樣式一致性和正確載入。統一使用 Thymeleaf 資源路徑解析。"
```
