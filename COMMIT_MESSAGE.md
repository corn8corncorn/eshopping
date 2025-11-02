## Git Commit Message

### Type: refactor
### Scope: auth-ui
### Subject: 調整 Logo 位置至表單外頁面中央上方

### Body:
將所有認證頁面的 Logo 從表單內部移至表單外部的頁面中央上方，調整 Logo 尺寸，提升視覺層次和品牌識別度。

**主要變更:**

1. **Logo 位置調整**
   - 將 Logo 從表單內部（`.loginRegForm` 內）移到表單外部
   - 使用絕對定位放置在頁面中央上方
   - Logo 容器使用 `.page-logo-container` 類別
   - 位置：距離頁面頂部 50px，水平居中

2. **CSS 樣式新增**
   - 新增 `.page-logo-container` 樣式：
     - 絕對定位（`position: absolute`）
     - 頁面上方（`top: 50px`）
     - 水平居中（`left: 50%; transform: translateX(-50%)`）
     - `z-index: 100` 確保在其他元素之上
   - 新增 `.page-logo` 樣式：
     - 尺寸：250px × 250px（由用戶調整）
     - 可點擊（cursor: pointer）
     - 過渡動畫（transition: transform 0.3s）
     - hover 效果：放大 1.1 倍
   - 移除表單內的 `.logo-container` 和 `.form-logo` 樣式（已棄用）

3. **頁面更新**
   - **登入頁面** (`login.html`)：
     - Logo 移到表單外部，位於 Notice 元件之後
     - 從表單內部移除 Logo
   - **註冊頁面** (`register.html`)：
     - Logo 移到表單外部
     - 從表單內部移除 Logo
   - **忘記密碼頁面** (`forgot-password.html`)：
     - Logo 移到表單外部
     - 從表單內部移除 Logo
   - **重設密碼頁面** (`reset-password.html`)：
     - Logo 移到表單外部
     - 從表單內部移除 Logo
   - **商店頁面** (`shop.html`)：
     - 輕微調整（4 行變更）

4. **Logo 圖片更新**
   - 更新 `logo.png` 圖片文件

**影響範圍:**
- `login.html` - Logo 位置調整（+9 行變更）
- `register.html` - Logo 位置調整（+7 行變更）
- `forgot-password.html` - Logo 位置調整（+7 行變更）
- `reset-password.html` - Logo 位置調整（+7 行變更）
- `shop.html` - 輕微調整（+4 行變更）
- `loginReg.css` - 新增 Logo 頁面定位樣式（+24 行）
- `logo.png` - 圖片文件更新

**統計資料:**
- 新增代碼：53 行
- 刪除代碼：5 行
- 淨增加：48 行
- 受影響檔案：7 個

**CSS 新增樣式:**
```css
/* Logo 容器 - 頁面中央上方（表單外面） */
.page-logo-container {
    display: flex;
    justify-content: center;
    align-items: center;
    position: absolute;
    top: 50px;
    left: 50%;
    transform: translateX(-50%);
    width: 100%;
    z-index: 100;
}

.page-logo {
    width: 250px;
    height: 250px;
    cursor: pointer;
    transition: transform 0.3s;
}

.page-logo:hover {
    transform: scale(1.1);
}
```

**視覺改進:**
- ✅ Logo 顯示在表單外面的頁面中央上方
- ✅ Logo 尺寸增大（250px × 250px），提升品牌識別度
- ✅ 水平居中對齊，視覺更平衡
- ✅ 保留可點擊返回首頁功能
- ✅ 保留 hover 放大效果
- ✅ 所有認證頁面 Logo 位置統一

**技術細節:**
- 使用絕對定位實現頁面級別定位
- 使用 `transform: translateX(-50%)` 實現水平居中
- 保持響應式設計兼容性
- Logo 獨立於表單結構，更易於維護

### Breaking Changes: 無

### Related Issues: 改善 Logo 顯示位置、提升品牌識別度、優化視覺層次

---

## 簡化版本（單行）

```
refactor(auth-ui): 調整 Logo 位置至表單外頁面中央上方

將所有認證頁面的 Logo 從表單內部移至表單外部的頁面中央上方，
調整 Logo 尺寸為 250px，使用絕對定位實現水平居中，提升品牌
識別度和視覺層次。
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
git commit -m "refactor(auth-ui): 調整 Logo 位置至表單外頁面中央上方

將所有認證頁面的 Logo 從表單內部移至表單外部的頁面中央上方，
調整 Logo 尺寸為 250px，使用絕對定位實現水平居中，提升品牌
識別度和視覺層次。"
```
