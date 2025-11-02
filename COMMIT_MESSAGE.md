## Git Commit Message

### Type: feat
### Scope: auth-validation
### Subject: 添加登入註冊頁面輸入驗證錯誤訊息顯示功能

### Body:
在登入和註冊頁面添加表單驗證錯誤訊息顯示功能，當用戶未填寫必填欄位時，在對應輸入框下方顯示驗證錯誤訊息，提升用戶體驗和表單填寫的友好性。

**主要變更:**

1. **HTML 結構重構**
   - 將每個輸入欄位包裝在 `.form-group` 容器中
   - 創建 `.input-row` 讓 label 和 input 在同一行顯示
   - 錯誤訊息 `<div class="error-message">` 放在輸入框下方
   - 保持 Thymeleaf 後端驗證錯誤顯示機制 (`th:errors`)

2. **CSS 樣式新增**
   - `.form-group` - 表單組容器，垂直排列輸入欄位和錯誤訊息
   - `.input-row` - 水平排列 label 和 input
   - `.error-message` - 錯誤訊息樣式（紅色文字、小字體、居中顯示）
   - 輸入框驗證狀態樣式（錯誤時紅色邊框，有效時綠色邊框）
   - 調整表單高度從 `350px` 改為 `min-height: 400px`，適應錯誤訊息顯示

3. **JavaScript 前端驗證**
   - **登入頁面驗證**：
     - 使用者名稱為空時顯示 "尚未輸入帳號"
     - 密碼為空時顯示 "尚未輸入密碼"
   - **註冊頁面驗證**：
     - 所有必填欄位為空時顯示 "尚未輸入[欄位名稱]"
   - 表單提交前進行驗證，阻止無效提交
   - 輸入時自動清除錯誤訊息（僅清除前端驗證訊息，保留後端錯誤）
   - 優先顯示後端驗證錯誤訊息

4. **驗證流程整合**
   - 前端驗證：JavaScript 在提交前檢查必填欄位
   - 後端驗證：Spring Validation 處理格式驗證和業務邏輯驗證
   - 兩者無縫整合，用戶體驗流暢

**影響範圍:**
- `login.html` - 重構表單結構，添加 JavaScript 驗證邏輯（+91 行）
- `register.html` - 重構表單結構，添加 JavaScript 驗證邏輯（+92 行）
- `loginReg.css` - 新增表單組、輸入行、錯誤訊息樣式（+53 行）

**統計資料:**
- 新增代碼：208 行
- 刪除代碼：28 行
- 淨增加：180 行
- 受影響檔案：3 個

**CSS 新增樣式範例:**
```css
.loginRegForm .form-group {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 15px;
    width: 100%;
}

.loginRegForm .input-row {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
}

.loginRegForm .error-message {
    color: #ffcccc;
    font-size: 12px;
    margin-top: 5px;
    text-align: center;
    width: 100%;
    min-height: 18px;
    font-weight: normal;
    padding: 0 10px;
}
```

**JavaScript 驗證邏輯:**
- 表單提交事件監聽
- 必填欄位空值檢查
- 動態創建/更新錯誤訊息 div
- 輸入事件監聽，實時清除錯誤訊息
- 與 Thymeleaf 後端驗證無衝突

**用戶體驗改進:**
- ✅ 錯誤訊息顯示在對應輸入框正下方，位置明確
- ✅ 即時驗證反饋，無需等待後端響應
- ✅ 錯誤訊息清晰易懂（如："尚未輸入帳號"）
- ✅ 輸入時自動清除錯誤，交互流暢
- ✅ 前後端驗證無縫整合
- ✅ 視覺樣式統一，紅色錯誤提示醒目

**技術細節:**
- 使用原生 JavaScript，無依賴外部庫
- CSS Flexbox 實現響應式布局
- 保持與 Thymeleaf 後端驗證的兼容性
- 錯誤訊息 div 動態創建（如果不存在）
- 檢查 `th:if` 屬性確保不覆蓋後端錯誤訊息

### Breaking Changes: 無

### Related Issues: 改善表單驗證用戶體驗、添加輸入驗證錯誤提示

---

## 簡化版本（單行）

```
feat(auth-validation): 添加登入註冊頁面輸入驗證錯誤訊息顯示功能

在登入和註冊頁面添加表單驗證錯誤訊息顯示，當用戶未填寫必填欄位時
在對應輸入框下方顯示驗證錯誤（如"尚未輸入帳號"）。重構 HTML 結構
使用 form-group 和 input-row，新增 CSS 樣式，添加 JavaScript 前端
驗證邏輯，與後端驗證無縫整合。
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
git commit -m "feat(auth-validation): 添加登入註冊頁面輸入驗證錯誤訊息顯示功能

在登入和註冊頁面添加表單驗證錯誤訊息顯示，當用戶未填寫必填欄位時
在對應輸入框下方顯示驗證錯誤（如"尚未輸入帳號"）。重構 HTML 結構
使用 form-group 和 input-row，新增 CSS 樣式，添加 JavaScript 前端
驗證邏輯，與後端驗證無縫整合。"
```
