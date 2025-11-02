## Git Commit Message

### Type: feat
### Scope: auth-ui
### Subject: 優化登入註冊頁面 UI 並添加返回首頁功能

### Body:
改善用戶認證頁面的用戶體驗，添加 logo、返回首頁功能，並優化頁面布局，解決頁面切換時的視覺閃爍問題。

**主要變更:**

1. **添加 Logo 和返回首頁功能**
   - 在登入頁面添加可點擊的 logo，點擊返回首頁
   - 在登入和註冊頁面添加返回首頁 icon（🏠）
   - 使用 Thymeleaf 資源路徑解析 `th:src="@{/resources/images/logo.png}"`

2. **優化頁面布局**
   - 創建 `.login-row` flex 容器，實現表單與返回首頁 icon 並排顯示
   - 創建 `.home-icon-column` 和 `.home-icon` 樣式
   - 調整表單高度從 300px 增加到 350px，適應新增的按鈕
   - 改進按鈕布局，使用 `justify-content: space-between` 分佈按鈕

3. **修復視覺閃爍問題**
   - 移除 `home.html` 中的 `background-color: #f5f5f5`
   - 統一使用 `loginReg.css` 的背景圖片，避免頁面切換時的背景閃爍
   - 確保所有頁面背景一致

4. **UI 改進**
   - 登入頁面：整合登入、註冊、忘記密碼按鈕
   - 註冊頁面：整合註冊、立即登入按鈕
   - 返回首頁 icon 使用圓形按鈕設計，hover 時有放大效果

**影響範圍:**
- `login.html` - 新增 logo、返回首頁 icon、優化布局
- `register.html` - 新增返回首頁 icon、優化布局
- `home.html` - 移除背景色，統一背景樣式
- `loginReg.css` - 新增 `.login-row`, `.home-icon-column`, `.home-icon` 樣式

**CSS 新增樣式:**
```css
.login-row {
    display: flex;
    align-items: center;
    gap: 20px;
    /* 定位樣式 */
}

.home-icon-column {
    display: flex;
    align-items: center;
    justify-content: center;
}

.home-icon {
    /* 圓形按鈕設計，60x60px */
    /* hover 時放大效果 */
}
```

**用戶體驗改進:**
- ✅ 新增多種返回首頁的方式（logo、icon）
- ✅ 優化按鈕布局，更直觀的操作
- ✅ 解決頁面切換時的視覺閃爍
- ✅ 統一的視覺風格
- ✅ 更友好的導航體驗

**技術細節:**
- 使用 Flexbox 實現響應式布局
- 使用 Thymeleaf 資源路徑解析，確保正確載入資源
- 過渡動畫效果（transition、transform）
- 保持與現有樣式系統的一致性

### Breaking Changes: 無

### Related Issues: 優化認證頁面 UI、改善用戶體驗、修復頁面切換閃爍問題

---

## 簡化版本（單行）

```
feat(auth-ui): 優化登入註冊頁面 UI 並添加返回首頁功能

在登入和註冊頁面添加 logo 和返回首頁 icon，優化頁面布局使用
row/column 結構，修復頁面切換時的視覺閃爍問題。改進按鈕布局，
提升用戶導航體驗。
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
git commit -m "feat(auth-ui): 優化登入註冊頁面 UI 並添加返回首頁功能

在登入和註冊頁面添加 logo 和返回首頁 icon，優化頁面布局使用
row/column 結構，修復頁面切換時的視覺閃爍問題。改進按鈕布局，
提升用戶導航體驗。"
```
