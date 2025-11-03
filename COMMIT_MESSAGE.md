# Git Commit Message 規範

## Commit Message 格式

```
<type>(<scope>): <subject>

<body>

<footer>
```

## Type 類型

- **feat**: 新功能
- **fix**: 修復 bug
- **docs**: 文檔變更
- **style**: 代碼格式變更（不影響代碼運行）
- **refactor**: 重構（既不是新功能也不是修復 bug）
- **perf**: 性能優化
- **test**: 測試相關
- **chore**: 構建過程或輔助工具的變動

## Scope 範圍（可選）

- **cart**: 購物車相關
- **product**: 商品相關
- **order**: 訂單相關
- **user**: 用戶相關
- **auth**: 認證相關

## Subject 主題

- 使用繁體中文
- 簡短描述變更內容
- 不使用句號結尾
- 首字母不大寫（除非是專有名詞）

## Body 正文（可選）

- 詳細說明變更原因和內容
- 列出主要變更項目
- 說明修改的檔案
- 如果有相關 issue，可以在此引用

## Footer 頁腳（可選）

- 關閉的 issue: `Closes #123`
- 破壞性變更: `BREAKING CHANGE: <描述>`

## 範例

### 範例 1: 新功能
```
feat(product-detail): 移除商品詳情頁的立即購買按鈕

移除商品詳情頁面的「立即購買」按鈕，簡化用戶操作流程。
現在用戶只能通過「加入購物車」功能將商品加入購物車。

主要變更:
- 移除立即購買表單和按鈕
- 保留「加入購物車」功能
- 保留「返回商品列表」連結

修改檔案:
- src/main/webapp/WEB-INF/views/product-detail.html
```

### 範例 2: 修復 bug
```
fix(cart): 修復商品詳情頁加入購物車功能並整合 Notice 元件

修復從商品詳情頁（/shop/product/{id}）加入購物車失敗的問題，
並將購物車頁面整合 Notice 元件以提供統一的訊息顯示體驗。

主要變更:
1. product-detail.html - 修復 JavaScript 錯誤和表單結構
2. CartController.java - 改進錯誤處理邏輯
3. cart.html - 整合 Notice 元件

修復的問題:
- JavaScript 錯誤: Cannot set properties of undefined
- 表單提交失敗
- 訊息顯示不一致

修改檔案:
- src/main/webapp/WEB-INF/views/product-detail.html
- src/main/java/com/example/demo/controller/CartController.java
- src/main/webapp/WEB-INF/views/cart.html
```

### 範例 3: 重構
```
refactor(cart): 使用 HQL DELETE 重構購物車項目刪除方法

將實體刪除改為 HQL DELETE 查詢，避免 StaleStateException 異常。

主要變更:
- CartItemDAOImpl.delete() 方法使用 HQL DELETE
- 移除對實體狀態的依賴
- 提高並發環境下的穩定性

修改檔案:
- src/main/java/com/example/demo/dao/impl/CartItemDAOImpl.java
```

## 注意事項

1. **一行 commit message 不超過 72 個字元**
2. **使用繁體中文撰寫**
3. **明確說明變更原因和影響範圍**
4. **列出所有修改的檔案**
5. **如果變更涉及多個模組，可以有多個 scope**
