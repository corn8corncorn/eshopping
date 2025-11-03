## Git Commit Message

### Type: fix
### Scope: cart
### Subject: 修復商品詳情頁加入購物車功能並整合 Notice 元件

### Body:
修復從商品詳情頁（/shop/product/{id}）加入購物車失敗的問題，並將購物車頁面整合 Notice 元件以提供統一的訊息顯示體驗。

**主要變更:**

1. **product-detail.html - 商品詳情頁**
   - 修復 JavaScript 錯誤
     - 移除內聯 onclick 事件，改用全局函數 `updateQuantityAndSubmit()`
     - 修正變數作用域問題（移除未定義變數的使用）
     - 添加錯誤處理和元素存在性檢查
   - 改進表單結構
     - 為「加入購物車」表單添加 `quantity` 隱藏欄位
     - 確保數量值能正確傳遞到後端
   - 添加 Notice 元件
     - 集成 `fragments/notice :: notice` fragment
     - 添加 `notice.js` 腳本引用
     - 移除舊的錯誤訊息顯示

2. **CartController.java - 購物車控制器**
   - 改進錯誤處理邏輯
     - `IllegalStateException`（庫存不足）→ 重定向回商品詳情頁
     - `IllegalArgumentException`（參數錯誤）→ 重定向回商品詳情頁
     - 其他異常 → 重定向到商品列表
   - 修正重定向路徑
     - 商品不存在時重定向到 `/shop`（而不是 `/products`）
     - 庫存不足時重定向回 `/shop/product/{id}`
   - 優化錯誤訊息傳遞
     - 使用 `redirectAttributes.addFlashAttribute()` 傳遞成功/錯誤訊息

3. **cart.html - 購物車頁面**
   - 整合 Notice 元件
     - 添加 `notice.js` 腳本引用
     - 添加 Notice 元件 fragment
     - 移除舊的訊息顯示 div（`.message.success` 和 `.message.error`）
   - 修正導航連結
     - 將商品列表連結從 `/products` 改為 `/shop`

**修復的問題:**

1. **JavaScript 錯誤**
   - 問題：`Cannot set properties of undefined (setting 'value')`
   - 原因：在 `onclick` 中使用 `document.getElementById('quantity')` 時，元素可能尚未渲染
   - 解決：改用全局函數，添加元素存在性檢查

2. **表單提交失敗**
   - 問題：從商品詳情頁點擊「加入購物車」按鈕後表單無法正確提交
   - 原因：表單缺少 `quantity` 隱藏欄位，或 JavaScript 未正確同步數量值
   - 解決：添加隱藏欄位，使用安全的 JavaScript 函數更新值

3. **訊息顯示不一致**
   - 問題：購物車頁面使用舊的訊息顯示方式，沒有自動消失功能
   - 解決：統一使用 Notice 元件，成功訊息會在 5 秒後自動消失

**技術細節:**

- 使用全局 JavaScript 函數避免作用域問題
- 添加 try-catch 錯誤處理確保穩定性
- 確保即使找不到 `quantity` 輸入框也能正常工作（使用默認值）
- Notice 元件提供統一的訊息顯示體驗，支援自動消失功能

**用戶體驗改進:**

- 修復商品詳情頁加入購物車功能，用戶可以順利將商品加入購物車
- 錯誤訊息會在商品詳情頁顯示，用戶無需離開頁面
- 成功訊息會在 5 秒後自動消失，不會遮擋用戶視線
- 統一的通知樣式，提供一致的用戶體驗

**測試場景:**

- ✅ 從商品列表頁加入購物車（原本正常）
- ✅ 從商品詳情頁加入購物車（已修復）
- ✅ 庫存不足時顯示錯誤訊息並停留在商品詳情頁
- ✅ 成功加入購物車後顯示成功訊息並在 5 秒後自動消失
