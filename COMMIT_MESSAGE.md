## Git Commit Message

### Type: refactor
### Scope: auth-ui
### Subject: 統一認證頁面表單樣式並優化按鈕布局

### Body:
統一所有認證相關頁面的表單樣式，將返回首頁 icon 移至表單右上角，優化按鈕布局和對齊方式，提升視覺一致性和用戶體驗。

**主要變更:**

1. **統一表單樣式**
   - 將 `forgot-password.html` 和 `reset-password.html` 調整為與登入/註冊頁面相同的表單結構
   - 使用 `login-row` > `loginRegForm` 統一容器結構
   - 使用 `form-group` > `input-row` 統一輸入欄位結構
   - 添加 JavaScript 前端驗證邏輯（與登入/註冊頁面一致）
   - `reset-password.html` 額外添加密碼一致性驗證

2. **返回首頁 icon 位置調整**
   - 將返回首頁 icon 從表單旁邊移到表單右上角
   - 新增 `.home-icon-top-right` CSS 樣式，使用絕對定位
   - 為 `.loginRegForm` 添加 `position: relative` 以支持絕對定位
   - 更新所有認證頁面（登入、註冊、忘記密碼、重設密碼）

3. **按鈕布局優化**
   - **登入頁面**：
     - 第一行：登入、註冊按鈕並排（`space-between` 分布）
     - 第二行：忘記密碼按鈕單獨一行（居中對齊）
     - 忘記密碼按鈕使用 `btn-warning` 樣式（由用戶調整）
   - **註冊頁面**：註冊、立即登入按鈕並排對齊
   - **忘記密碼頁面**：驗證身份、返回登入按鈕並排對齊
   - **重設密碼頁面**：重設密碼、返回登入按鈕並排對齊
   - 所有按鈕行使用 `form-group` > `input-row` 結構，添加空 `<label></label>` 佔位，確保與輸入框對齊

4. **CSS 樣式調整**
   - 新增 `.home-icon-top-right` 樣式（絕對定位，右上角）
   - 保留 `.home-icon-column` 樣式（標註為舊版，已棄用）
   - 調整 `.login-row` 移除 `gap: 20px`（不再需要並排布局）
   - 表單寬度調整為 `500px`，高度調整為 `min-height: 300px`（由用戶調整）

**影響範圍:**
- `login.html` - 按鈕布局調整，返回首頁 icon 移至右上角（+39 行變更）
- `register.html` - 按鈕對齊調整，返回首頁 icon 移至右上角（+25 行變更）
- `forgot-password.html` - 統一表單樣式，添加驗證邏輯（+110 行變更）
- `reset-password.html` - 統一表單樣式，添加驗證邏輯（+147 行變更）
- `loginReg.css` - 新增樣式和調整（+29 行變更）

**統計資料:**
- 新增代碼：281 行
- 刪除代碼：69 行
- 淨增加：212 行
- 受影響檔案：5 個

**CSS 新增樣式:**
```css
.home-icon-top-right {
    position: absolute;
    top: 10px;
    right: 10px;
    z-index: 10;
}

.loginRegForm {
    position: relative; /* 支持絕對定位 */
}
```

**按鈕布局結構:**
```html
<!-- 按鈕行對齊結構 -->
<div class="form-group">
    <div class="input-row">
        <label></label>  <!-- 空 label 佔位，對齊輸入框 -->
        <div style="display: flex; justify-content: space-between; width: 200px;">
            <!-- 按鈕內容 -->
        </div>
    </div>
</div>
```

**用戶體驗改進:**
- ✅ 所有認證頁面視覺風格統一
- ✅ 返回首頁 icon 位置更直觀（右上角）
- ✅ 按鈕與輸入框完美對齊，視覺更整潔
- ✅ 登入頁面按鈕布局更清晰（忘記密碼單獨一行）
- ✅ 統一的前端驗證體驗
- ✅ 表單樣式更現代化

**技術細節:**
- 使用 Flexbox 實現響應式布局
- 使用絕對定位實現右上角 icon
- 保持與現有樣式系統的一致性
- 所有頁面共享相同的 CSS 和驗證邏輯

### Breaking Changes: 無

### Related Issues: 統一認證頁面樣式、優化用戶體驗、改善按鈕布局

---

## 簡化版本（單行）

```
refactor(auth-ui): 統一認證頁面表單樣式並優化按鈕布局

統一所有認證頁面表單樣式，將返回首頁 icon 移至表單右上角，優化
按鈕布局和對齊方式。忘記密碼和重設密碼頁面採用與登入/註冊頁面相同
的表單結構，所有按鈕與輸入框完美對齊，提升視覺一致性。
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
git commit -m "refactor(auth-ui): 統一認證頁面表單樣式並優化按鈕布局

統一所有認證頁面表單樣式，將返回首頁 icon 移至表單右上角，優化
按鈕布局和對齊方式。忘記密碼和重設密碼頁面採用與登入/註冊頁面相同
的表單結構，所有按鈕與輸入框完美對齊，提升視覺一致性。"
```
