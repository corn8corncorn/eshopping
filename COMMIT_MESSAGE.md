## Git Commit Message

### Type: feat
### Scope: customer-phone-validation
### Subject: 添加客戶電話號碼格式驗證（09開頭+8碼數字，共10碼）

### Body:
為客戶資料編輯頁面添加電話號碼格式驗證功能，確保電話號碼必須以09開頭，後面接8碼數字，總共10碼，且不能有空白或其他符號。

**主要變更:**

1. **Customer.java - 客戶模型**
   - 添加 Bean Validation 註解
     - 在 `phone` 欄位添加 `@Pattern` 驗證註解
     - 正則表達式：`^$|^09\\d{8}$`
       - `^$` - 允許空值（電話是可選欄位）
       - `^09\\d{8}$` - 驗證必須以 09 開頭，後面 8 碼數字，共 10 碼
     - 錯誤訊息：「電話號碼必須以09開頭，後面接8碼數字，共10碼，不能有空白或其他符號」
   - 添加必要的 import
     - `import javax.validation.constraints.Pattern;`

2. **CustomerController.java - 客戶管理控制器**
   - 啟用 Bean Validation
     - 在 `updateCustomer()` 方法添加 `@Valid` 註解
     - 添加 `BindingResult` 參數檢查驗證結果
     - 驗證失敗時返回編輯頁面並顯示錯誤訊息
   - 添加必要的 import
     - `import javax.validation.Valid;`
     - `import org.springframework.validation.BindingResult;`
   - 改進驗證錯誤處理
     - 檢查 `bindingResult.hasErrors()`
     - 驗證失敗時記錄警告日誌
     - 返回 `"edit-customer"` 視圖以顯示驗證錯誤

3. **edit-customer.html - 編輯客戶資料頁面**
   - 添加 HTML5 驗證
     - 在電話輸入框添加 `pattern="^09\\d{8}$"` 屬性
     - 添加 `placeholder="例如：0912345678"` 提示文字
     - 添加格式說明：「※ 請輸入10碼手機號碼（09開頭 + 8碼數字，不能有空白或其他符號）」
   - 添加即時驗證 JavaScript
     - 實作 `validatePhone()` 函數
       - 使用 `oninput` 事件進行即時驗證
       - 空值視為有效（因為電話是可選欄位）
       - 不符合格式時顯示錯誤訊息
       - 使用 `setCustomValidity()` 與 HTML5 驗證整合
     - 表單提交時驗證
       - 在 `DOMContentLoaded` 事件中綁定表單提交驗證
       - 如果驗證失敗，阻止表單提交
   - 添加錯誤訊息顯示區域
     - 添加 `id="phoneError"` 的錯誤訊息 div
     - 動態顯示/隱藏驗證錯誤訊息

**驗證規則:**

- **允許空值**: 電話號碼是可選欄位，允許不填寫
- **格式要求**: 如果填寫，必須符合以下規則：
  - 必須以 `09` 開頭
  - 後面必須接 8 碼數字
  - 總共必須是 10 碼數字
  - 不能有空白或其他符號

**驗證層級:**

1. **前端 HTML5 驗證**: 使用 `pattern` 屬性進行基本的格式檢查
2. **前端 JavaScript 驗證**: 即時驗證並顯示友好的錯誤訊息
3. **後端 Bean Validation**: 使用 `@Pattern` 註解確保資料完整性

**測試範例:**

- **有效格式**: 
  - `0912345678` ✅
  - 空值 ✅
  
- **無效格式**: 
  - `091234567` ❌ (9碼)
  - `0812345678` ❌ (不以09開頭)
  - `09 12345678` ❌ (有空白)
  - `09123456789` ❌ (11碼)
  - `09-12345678` ❌ (有符號)

**技術細節:**

- 使用 Java Bean Validation (JSR-303/JSR-380) 進行後端驗證
- 使用 HTML5 `pattern` 屬性進行前端基本驗證
- JavaScript 驗證提供即時回饋，提升用戶體驗
- 驗證訊息使用中文，符合用戶需求
- 支援空值驗證（因為電話是可選欄位）

**用戶體驗改進:**

- 即時驗證：輸入時立即顯示錯誤訊息，無需等到提交
- 清晰的格式說明：提示文字清楚說明電話號碼格式要求
- 範例提示：placeholder 提供範例號碼（0912345678）
- 友好的錯誤訊息：明確指出格式錯誤的原因

**資料完整性:**

- 確保所有儲存的電話號碼都符合台灣手機號碼格式
- 防止錯誤格式的電話號碼進入資料庫
- 提升資料品質和一致性
