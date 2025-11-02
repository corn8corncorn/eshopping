## Git Commit Message

### Type: chore
### Scope: views
### Subject: 統一所有視圖頁面引入 CSS 樣式表

### Body:
在所有 HTML 視圖頁面中統一引入 `loginReg.css` 樣式表，確保前端樣式一致性和正確載入。

**影響範圍:**
更新了以下 21 個視圖檔案：
- `account.html` - 會員中心頁面
- `add-product.html` - 新增商品頁面
- `add-user.html` - 新增用戶頁面
- `cart.html` - 購物車頁面
- `checkout.html` - 結帳頁面
- `customer-profile.html` - 客戶資料頁面
- `customers.html` - 客戶列表頁面
- `edit-customer.html` - 編輯客戶頁面
- `edit-product.html` - 編輯商品頁面
- `edit-user.html` - 編輯用戶頁面
- `forgot-password.html` - 忘記密碼頁面
- `home.html` - 首頁
- `order-confirmation.html` - 訂單確認頁面
- `order-detail.html` - 訂單詳情頁面
- `orders.html` - 訂單列表頁面
- `product-detail.html` - 商品詳情頁面
- `products.html` - 商品列表頁面
- `reset-password.html` - 重設密碼頁面
- `search-results.html` - 搜尋結果頁面
- `shop.html` - 商店頁面
- `users.html` - 用戶列表頁面

**變更內容:**
在每個 HTML 檔案的 `<head>` 區塊中新增：
```html
<link rel="stylesheet" type="text/css" th:href="@{/resources/css/loginReg.css}">
```

**目標:**
- 統一所有頁面的 CSS 引入方式
- 確保 `loginReg.css` 樣式在所有頁面正確載入
- 改善前端樣式一致性
- 提供統一的視覺體驗

**技術細節:**
- 使用 Thymeleaf 的 `@{...}` 語法進行資源路徑解析
- 在 `<meta charset>` 之後、`<title>` 之前插入 CSS 連結
- 保持原有的 `<style>` 標籤內聯樣式不變

### Breaking Changes: 無

### Related Issues: 統一前端樣式載入機制

---

## 簡化版本（單行）

```
chore(views): 統一所有視圖頁面引入 CSS 樣式表

在所有 21 個 HTML 視圖頁面中新增 loginReg.css 樣式表引入，確保
前端樣式一致性和正確載入。統一使用 Thymeleaf 資源路徑解析。
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
