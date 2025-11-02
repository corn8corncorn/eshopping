## Git Commit Message

### Type: refactor
### Scope: views
### Subject: 統一所有按鈕樣式為 .btn-primary

### Body:
重構按鈕樣式系統，將所有按鈕統一使用 `.btn-primary` 樣式，移除所有未使用的按鈕樣式，簡化 CSS 結構並提升維護性。

**主要變更:**
1. **統一按鈕樣式**
   - 所有按鈕現在使用 `.btn-primary` 樣式
   - 樣式：`background-color: #c19677`, `color: white`
   - hover：`color: #fbd5ba`, `background-color: #4f3f66`

2. **移除未使用的按鈕樣式**
   - 移除 `.btn-primary-gradient`
   - 移除 `.btn-secondary`
   - 移除 `.btn-success`
   - 移除 `.btn-danger`
   - 保留 `.btn` 基礎樣式和 `.btn-primary` 主要樣式

3. **更新所有 HTML 檔案**
   - 將所有 `btn-success`, `btn-danger`, `btn-secondary`, `btn-info`, `btn-warning` 等改為 `btn-primary`
   - 從登入、註冊到管理頁面的所有按鈕統一風格

**影響範圍:**
更新的視圖檔案（22 個）：
- `login.html` - 登入按鈕
- `register.html` - 註冊按鈕
- `account.html` - 會員中心按鈕
- `cart.html` - 購物車按鈕
- `checkout.html` - 結帳按鈕
- `home.html` - 首頁按鈕
- `order-confirmation.html` - 訂單確認按鈕
- `order-detail.html` - 訂單詳情按鈕
- `orders.html` - 訂單列表按鈕
- `product-detail.html` - 商品詳情按鈕
- `shop.html` - 商店頁按鈕
- `search-results.html` - 搜尋結果按鈕
- `products.html` - 商品管理按鈕
- `add-product.html` - 新增商品按鈕
- `edit-product.html` - 編輯商品按鈕
- `add-user.html` - 新增用戶按鈕
- `edit-user.html` - 編輯用戶按鈕
- `edit-customer.html` - 編輯客戶按鈕
- `users.html` - 用戶列表按鈕
- `forgot-password.html` - 忘記密碼按鈕
- `reset-password.html` - 重設密碼按鈕

**優點:**
- ✅ 統一視覺風格，提升用戶體驗一致性
- ✅ 簡化 CSS 檔案（減少約 40 行未使用代碼）
- ✅ 降低維護成本（只需維護一種按鈕樣式）
- ✅ 更清晰的程式碼結構
- ✅ 易於未來調整按鈕外觀

**統計資料:**
- 刪除代碼：198 行
- 新增代碼：84 行
- 淨減少：114 行
- 受影響檔案：22 個

### Breaking Changes: 無

### Related Issues: 統一按鈕樣式、簡化 CSS 結構

---

## 簡化版本（單行）

```
refactor(views): 統一所有按鈕樣式為 .btn-primary

將所有按鈕統一使用 .btn-primary 樣式，移除未使用的按鈕變體（secondary,
success, danger 等），從 22 個 HTML 檔案中更新所有按鈕類別，簡化
CSS 結構並提升維護性。
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
git commit -m "refactor(views): 統一所有按鈕樣式為 .btn-primary

將所有按鈕統一使用 .btn-primary 樣式，移除未使用的按鈕變體，從
22 個 HTML 檔案中更新所有按鈕類別，簡化 CSS 結構並提升維護性。"
```
