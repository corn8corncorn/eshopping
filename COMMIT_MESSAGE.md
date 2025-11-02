## Git Commit Message

### Type: refactor
### Scope: product-forms
### Subject: 統一商品管理表單樣式與認證頁面一致

### Body:
將新增商品和編輯商品頁面的表單樣式重構為與登入註冊頁面相同的風格，統一使用 form-group、input-row 結構，添加錯誤訊息顯示、Logo、返回首頁功能，提升整體視覺一致性和用戶體驗。

**主要變更:**

1. **表單結構統一**
   - 使用 `login-row` > `loginRegForm` 容器結構
   - 使用 `form-group` > `input-row` 統一輸入欄位結構
   - 所有輸入欄位（input、textarea、select）使用相同的布局
   - Label 和輸入框在同一行顯示，錯誤訊息在下方

2. **頁面元素添加**
   - **Notice 元件**：添加通用訊息提示元件
   - **Logo**：添加頁面中央上方的 Logo（可點擊返回首頁）
   - **返回首頁 icon**：表單右上角添加返回首頁按鈕（🏠）
   - 標題從 `<h1>` 改為 `<h2>`（與認證頁面一致）

3. **CSS 樣式擴展**
   - 擴展 `.loginRegForm` 樣式以支持 `textarea` 和 `select` 元素
   - **textarea 樣式**：
     - 寬度：200px（與其他輸入框一致）
     - 文字左對齊（text-align: left）
     - 內邊距和可調整大小（resize: vertical）
   - **select 樣式**：
     - 寬度：200px，高度：25px
     - 統一的邊框和圓角樣式
     - 統一的內邊距
   - 所有表單元素共享相同的基礎樣式

4. **錯誤訊息顯示**
   - 為所有輸入欄位添加 `.error-message` 顯示區域
   - 使用 Thymeleaf 的 `th:errors` 顯示後端驗證錯誤
   - 錯誤訊息顯示在輸入框下方，使用統一樣式

5. **按鈕布局優化**
   - 按鈕行使用 `form-group` > `input-row` 結構
   - 添加空 `<label></label>` 佔位，確保與輸入框對齊
   - 儲存/更新和取消按鈕並排顯示（space-between 分布）

**影響範圍:**
- `add-product.html` - 完全重構表單樣式（新增商品頁面）
- `edit-product.html` - 完全重構表單樣式（編輯商品頁面）
- `loginReg.css` - 擴展樣式支持 textarea 和 select（+24 行）

**統計資料:**
- 新增代碼：約 150 行
- 刪除代碼：約 30 行
- 淨增加：約 120 行
- 受影響檔案：3 個

**CSS 新增/修改樣式:**
```css
/* 統一 input、textarea、select 樣式 */
.loginRegForm input,
.loginRegForm textarea,
.loginRegForm select {
    width: 200px;
    border-radius: 5px;
    color: #c29678;
    font-weight: bold;
    border: 2px solid transparent;
}

.loginRegForm textarea {
    text-align: left;
    padding: 5px;
    resize: vertical;
}

.loginRegForm select {
    height: 25px;
    padding: 2px;
}
```

**表單欄位對應:**
- 名稱（name）- text input
- 類型（type）- text input
- 價格（price）- number input
- 描述（description）- textarea（多行輸入）
- 圖片連結（imageUrl）- text input
- 狀態（status）- select（下拉選單）

**視覺改進:**
- ✅ 表單樣式與認證頁面完全一致
- ✅ 所有輸入欄位對齊美觀
- ✅ 錯誤訊息顯示在對應輸入框下方
- ✅ Logo 和返回首頁功能統一体驗
- ✅ 統一的按鈕布局和樣式
- ✅ 響應式設計支持
- ✅ 統一的視覺風格

**技術細節:**
- 使用 Flexbox 實現響應式布局
- 所有表單元素共享 CSS 樣式類別
- 保持與現有樣式系統的一致性
- 支持所有表單元素類型（input、textarea、select）
- 錯誤訊息使用 Thymeleaf 動態渲染

### Breaking Changes: 無

### Related Issues: 統一表單樣式、提升視覺一致性、改善用戶體驗

---

## 簡化版本（單行）

```
refactor(product-forms): 統一商品管理表單樣式與認證頁面一致

將新增商品和編輯商品頁面的表單樣式重構為與登入註冊頁面相同的
風格，使用 form-group、input-row 結構，添加 Notice 元件、Logo、
返回首頁功能，擴展 CSS 支持 textarea 和 select，提升整體視覺
一致性。
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
git commit -m "refactor(product-forms): 統一商品管理表單樣式與認證頁面一致

將新增商品和編輯商品頁面的表單樣式重構為與登入註冊頁面相同的
風格，使用 form-group、input-row 結構，添加 Notice 元件、Logo、
返回首頁功能，擴展 CSS 支持 textarea 和 select，提升整體視覺
一致性。"
```
