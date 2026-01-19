# eShopping 專案 PPT 製作指南

## 🎯 解決方案

由於 PowerPoint 無法直接開啟 HTML 檔案，以下是幾種可行的製作方法：

## 方法一：使用瀏覽器列印為 PDF（推薦）

### 步驟：
1. **開啟 HTML 檔案**
   ```bash
   # 在瀏覽器中開啟
   firefox eShopping_專案簡報_列印版.html
   # 或
   google-chrome eShopping_專案簡報_列印版.html
   ```

2. **列印為 PDF**
   - 按 `Ctrl+P` 或 `Cmd+P`
   - 選擇「另存為 PDF」
   - 設定頁面大小：自訂 10 英吋 x 7.5 英吋
   - 列印

3. **匯入 PowerPoint**
   - 開啟 PowerPoint
   - 插入 → 圖片 → 選擇 PDF 檔案
   - 或直接將 PDF 拖入 PowerPoint

## 方法二：手動在 PowerPoint 中建立

### 步驟：
1. **開啟 PowerPoint**，建立新簡報

2. **設定投影片大小**
   - 設計 → 投影片大小 → 自訂大小
   - 寬度：10 英吋
   - 高度：7.5 英吋

3. **複製內容**
   - 開啟 `eShopping_專案簡報.html` 在瀏覽器中
   - 參考每張投影片的內容
   - 在 PowerPoint 中手動建立對應的投影片

4. **插入截圖**
   - 在每張投影片左側插入對應的畫面截圖
   - 右側輸入功能描述文字

## 方法三：使用 LibreOffice Impress

### 步驟：
1. **開啟 LibreOffice Impress**
   ```bash
   libreoffice --impress
   ```

2. **匯入 HTML**
   - 檔案 → 開啟 → 選擇 HTML 檔案
   - 或直接將 HTML 內容複製貼上

3. **另存為 PPTX**
   - 檔案 → 另存新檔
   - 選擇格式：PowerPoint (.pptx)

## 方法四：使用線上轉換工具

### 推薦工具：
1. **CloudConvert** (https://cloudconvert.com/)
   - 上傳 HTML 檔案
   - 轉換為 PPTX
   - 下載轉換後的檔案

2. **Zamzar** (https://www.zamzar.com/)
   - 類似功能

## 方法五：安裝 Python 套件並使用腳本

### 步驟：
1. **安裝 pip**（如果沒有）
   ```bash
   sudo apt update
   sudo apt install python3-pip
   ```

2. **安裝 python-pptx**
   ```bash
   pip3 install python-pptx
   ```

3. **執行腳本**
   ```bash
   python3 create_presentation.py
   ```

4. **插入截圖**
   - 開啟生成的 `eShopping_專案簡報.pptx`
   - 在每張投影片左側插入截圖

## 📋 投影片內容清單

每張投影片需要準備的內容：

1. **標題頁**
   - 專案名稱：eShopping 電商系統
   - 副標題：面試專案簡報

2. **專案概述** - 技術架構圖或技術棧截圖

3. **首頁功能** - 首頁截圖 (`/home`)

4. **用戶認證系統** - 登入/註冊頁面截圖

5. **商品展示功能** - 商品列表/詳情頁截圖

6. **購物車功能** - 購物車頁面截圖 (`/cart`)

7. **訂單管理系統** - 訂單列表/詳情頁截圖

8. **商品管理（後台）** - 後台商品管理頁面截圖

9. **用戶管理（後台）** - 後台用戶管理頁面截圖

10. **客戶管理** - 客戶管理頁面截圖

11. **訂單管理（後台）** - 後台訂單管理頁面截圖

12. **後台儀表板** - 後台儀表板截圖 (`/admin/dashboard`)

13. **技術特色** - 程式碼或架構圖截圖

14. **資料庫設計** - 資料庫 ER 圖截圖

15. **專案總結** - 專案總結圖或架構圖

## 💡 截圖建議

### 截圖工具：
- **Linux**: `gnome-screenshot` 或 `flameshot`
- **Windows**: `Snipping Tool` 或 `Win+Shift+S`
- **Mac**: `Cmd+Shift+4`

### 截圖技巧：
1. 使用瀏覽器全螢幕模式截圖
2. 確保截圖清晰，文字可讀
3. 建議解析度：1920x1080 或更高
4. 可以裁剪，只保留重要部分
5. 統一截圖風格（相同瀏覽器、相同縮放比例）

## 🚀 快速開始（最簡單）

1. **開啟 HTML 檔案**
   ```bash
   firefox eShopping_專案簡報_列印版.html
   ```

2. **列印為 PDF** (`Ctrl+P` → 另存為 PDF)

3. **在 PowerPoint 中插入 PDF**
   - 插入 → 物件 → 從檔案建立
   - 選擇 PDF 檔案

4. **手動插入截圖**
   - 在每張投影片左側插入對應截圖
   - 替換文字描述

完成！

