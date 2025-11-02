# Notice 元件使用說明

## 概述
Notice 元件是一個通用的訊息提示元件，可以在所有頁面使用來顯示錯誤、成功、警告和資訊訊息。

## 使用方式

### 1. 在 HTML 頁面中引入

#### 引入 CSS 和 JavaScript
在 `<head>` 中添加：
```html
<link rel="stylesheet" type="text/css" th:href="@{/resources/css/loginReg.css}">
<script th:src="@{/resources/js/notice.js}"></script>
```

#### 在頁面中使用 Thymeleaf Fragment
在 `<body>` 開始處添加：
```html
<!-- Notice 元件 -->
<div th:replace="~{fragments/notice :: notice}"></div>
```

### 2. 後端傳遞訊息（Spring Controller）

在 Controller 中設置 Model 屬性：
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

### 3. JavaScript API 使用

如果需要動態顯示訊息，可以使用 JavaScript API：

```javascript
// 顯示錯誤訊息（5 秒後自動消失）
showError("這是一個錯誤訊息");

// 顯示成功訊息（3 秒後自動消失）
showSuccess("操作成功！");

// 顯示警告訊息（5 秒後自動消失）
showWarning("請注意這個警告");

// 顯示資訊訊息（5 秒後自動消失）
showInfo("這是一條資訊訊息");

// 自訂消失時間（10 秒後消失，0 表示不自動消失）
showNotice("自訂訊息", "success", 10000);

// 清除所有 Notice
clearNotices();
```

### 4. 訊息類型

- **error** (錯誤) - 紅色背景，用於顯示錯誤訊息
- **success** (成功) - 綠色背景，用於顯示成功訊息
- **warning** (警告) - 黃色背景，用於顯示警告訊息
- **info** (資訊) - 藍色背景，用於顯示一般資訊

## 功能特性

1. **自動消失**：
   - 成功訊息：3 秒後自動消失
   - 錯誤訊息：5 秒後自動消失
   - 警告/資訊訊息：5 秒後自動消失

2. **手動關閉**：點擊右上角的 × 按鈕可以手動關閉

3. **動畫效果**：從右側滑入，關閉時滑出

4. **響應式設計**：在手機等小螢幕設備上自動調整布局

5. **多訊息支持**：可以同時顯示多個訊息，垂直堆疊

6. **安全性**：JavaScript API 自動進行 HTML 轉義，防止 XSS 攻擊

## 樣式自訂

如果需要自訂樣式，可以修改 `loginReg.css` 中的 `.notice-*` 相關樣式。

## 範例

### 完整範例（登入頁面）
```html
<!DOCTYPE html>
<html lang="zh-TW" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" th:href="@{/resources/css/loginReg.css}">
    <script th:src="@{/resources/js/notice.js}"></script>
    <title>登入 - 電子商城</title>
</head>
<body>
    <!-- Notice 元件 -->
    <div th:replace="~{fragments/notice :: notice}"></div>
    
    <!-- 頁面其他內容 -->
</body>
</html>
```

## 注意事項

1. 確保已經引入 `loginReg.css`，因為 Notice 樣式定義在其中
2. 確保已經引入 `notice.js`，否則 JavaScript API 將無法使用
3. Thymeleaf Fragment 路徑為 `fragments/notice :: notice`
4. 後端 Model 屬性名稱必須與 Fragment 中定義的一致（error, success, warning, info）

