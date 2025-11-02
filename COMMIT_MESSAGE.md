## Git Commit Message

### Type: feat
### Scope: ui-components
### Subject: 新增通用 Notice 訊息提示元件

### Body:
創建一個通用的 Notice 元件系統，用於在所有頁面統一顯示錯誤、成功、警告和資訊訊息，提升用戶體驗和視覺一致性。

**主要變更:**

1. **Thymeleaf Fragment 元件**
   - 創建 `fragments/notice.html` - 可重用的訊息顯示元件
   - 支持 4 種訊息類型：error（錯誤）、success（成功）、warning（警告）、info（資訊）
   - 使用 Thymeleaf 條件渲染（`th:if`）顯示不同類型的訊息
   - 每個訊息包含圖標、訊息內容和關閉按鈕

2. **CSS 樣式設計**
   - 在 `loginReg.css` 中新增 Notice 元件完整樣式系統（+130 行）
   - 固定定位於頁面右上角（`position: fixed, top: 20px, right: 20px`）
   - 4 種訊息類型的顏色設計：
     - 錯誤訊息：紅色系（`#fee` 背景，`#dc3545` 邊框）
     - 成功訊息：綠色系（`#efe` 背景，`#28a745` 邊框）
     - 警告訊息：黃色系（`#fffbf0` 背景，`#ffc107` 邊框）
     - 資訊訊息：藍色系（`#e7f3ff` 背景，`#17a2b8` 邊框）
   - 滑入/滑出動畫效果（`slideInRight`, `slideOutRight`）
   - 響應式設計：手機端自動調整為全寬顯示
   - 陰影和圓角效果，提升視覺質感

3. **JavaScript API 系統**
   - 創建 `notice.js` - 完整的 Notice 元件 JavaScript API
   - **核心函數**：
     - `showNotice(message, type, duration)` - 通用顯示函數
     - `showError(message, duration)` - 顯示錯誤訊息
     - `showSuccess(message, duration)` - 顯示成功訊息
     - `showWarning(message, duration)` - 顯示警告訊息
     - `showInfo(message, duration)` - 顯示資訊訊息
     - `removeNotice(notice)` - 移除單個訊息
     - `clearNotices()` - 清除所有訊息
   - **自動功能**：
     - 自動為 Thymeleaf 生成的訊息添加自動消失功能
     - 成功訊息 3 秒後自動消失
     - 錯誤/警告/資訊訊息 5 秒後自動消失
   - **安全性**：HTML 轉義防止 XSS 攻擊
   - **動畫支持**：支持淡入淡出動畫效果

4. **頁面整合**
   - 更新所有認證頁面使用新的 Notice 元件：
     - `login.html` - 替換原有的簡單錯誤/成功訊息顯示
     - `register.html` - 整合 Notice 元件
     - `forgot-password.html` - 整合 Notice 元件
     - `reset-password.html` - 整合 Notice 元件
   - 在所有頁面添加 `notice.js` 腳本引用
   - 使用 Thymeleaf Fragment 引用：`<div th:replace="~{fragments/notice :: notice}"></div>`

5. **文件說明**
   - 創建 `fragments/README.md` - 詳細的使用說明文檔
   - 包含使用範例、API 說明、功能特性等

**影響範圍:**
- `login.html` - 整合 Notice 元件（+9 行變更）
- `register.html` - 整合 Notice 元件（+5 行變更）
- `forgot-password.html` - 整合 Notice 元件（+5 行變更）
- `reset-password.html` - 整合 Notice 元件（+5 行變更）
- `loginReg.css` - 新增 Notice 樣式系統（+130 行）
- 新建檔案：
  - `fragments/notice.html` - Thymeleaf Fragment 元件
  - `fragments/README.md` - 使用說明文檔
  - `resources/js/notice.js` - JavaScript API

**統計資料:**
- 新增代碼：約 250 行
- 修改代碼：24 行
- 新建檔案：3 個
- 受影響檔案：5 個

**CSS 新增樣式範例:**
```css
.notice-container {
    position: fixed;
    top: 20px;
    right: 20px;
    z-index: 10000;
    max-width: 400px;
}

.notice {
    display: flex;
    align-items: center;
    padding: 15px 20px;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    animation: slideInRight 0.3s ease-out;
}
```

**JavaScript API 使用範例:**
```javascript
// 顯示錯誤訊息（5 秒後自動消失）
showError("這是一個錯誤訊息");

// 顯示成功訊息（3 秒後自動消失）
showSuccess("操作成功！");

// 自訂消失時間
showNotice("自訂訊息", "success", 10000);

// 清除所有訊息
clearNotices();
```

**後端使用範例（Spring Controller）:**
```java
// 錯誤訊息
model.addAttribute("error", "登入失敗，請檢查您的帳號和密碼");

// 成功訊息
model.addAttribute("success", "註冊成功！請使用您的帳號和密碼登入");

// 警告訊息
model.addAttribute("warning", "您的帳號即將到期");

// 資訊訊息
model.addAttribute("info", "系統將於今晚進行維護");
```

**功能特性:**
- ✅ 統一訊息顯示風格，提升視覺一致性
- ✅ 4 種訊息類型（錯誤、成功、警告、資訊）
- ✅ 自動消失功能（可自訂時間）
- ✅ 手動關閉功能（點擊 × 按鈕）
- ✅ 滑入/滑出動畫效果
- ✅ 支持同時顯示多個訊息
- ✅ 響應式設計（手機端自動調整）
- ✅ XSS 防護（自動 HTML 轉義）
- ✅ 可重用元件（Thymeleaf Fragment）
- ✅ 完整的 JavaScript API
- ✅ 自動處理 Thymeleaf 訊息

**技術細節:**
- 使用 Thymeleaf Fragment 實現元件重用
- 固定定位（fixed positioning）確保訊息始終可見
- CSS 動畫實現流暢的視覺效果
- JavaScript 動態創建和移除 DOM 元素
- 自動清理空容器，優化 DOM 結構
- 事件委託和動態綁定

### Breaking Changes: 無

### Related Issues: 統一訊息顯示系統、提升用戶體驗、改善視覺一致性

---

## 簡化版本（單行）

```
feat(ui-components): 新增通用 Notice 訊息提示元件

創建通用的 Notice 元件系統，包含 Thymeleaf Fragment、CSS 樣式和
JavaScript API。支持 4 種訊息類型（錯誤、成功、警告、資訊），
具有自動消失、手動關閉、動畫效果等功能。更新所有認證頁面整合
新元件，統一訊息顯示風格。
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
git commit -m "feat(ui-components): 新增通用 Notice 訊息提示元件

創建通用的 Notice 元件系統，包含 Thymeleaf Fragment、CSS 樣式和
JavaScript API。支持 4 種訊息類型（錯誤、成功、警告、資訊），
具有自動消失、手動關閉、動畫效果等功能。更新所有認證頁面整合
新元件，統一訊息顯示風格。"
```
