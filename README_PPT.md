# eShopping 專案 PPT 簡報

## 📁 檔案說明

- **eShopping_專案簡報.html** - HTML 格式簡報（可直接在 PowerPoint 中開啟）
- **create_presentation.py** - Python 自動生成腳本（需安裝 python-pptx）
- **PPT製作說明.md** - 詳細製作說明文件

## 🚀 快速開始

### 方法一：使用 HTML 檔案（最簡單）

1. **開啟檔案**
   - 用瀏覽器開啟 `eShopping_專案簡報.html` 預覽
   - 或用 PowerPoint：檔案 → 開啟 → 選擇 `eShopping_專案簡報.html`

2. **插入截圖**
   - 每個投影片左側有灰色佔位區域
   - 刪除文字佔位符，插入對應的畫面截圖

3. **儲存為 PPTX**
   - 檔案 → 另存新檔 → 選擇 `.pptx` 格式

### 方法二：使用 Python 腳本

```bash
# 安裝依賴
pip3 install python-pptx

# 執行腳本
python3 create_presentation.py

# 會生成 eShopping_專案簡報.pptx
```

## 📸 需要準備的截圖

1. 首頁 (`/home`)
2. 登入頁面 (`/login`)
3. 商品列表 (`/shop`)
4. 商品詳情 (`/shop/product/{id}`)
5. 購物車 (`/cart`)
6. 結帳頁面 (`/orders/checkout`)
7. 訂單列表 (`/orders/my`)
8. 訂單詳情 (`/orders/{id}`)
9. 後台儀表板 (`/admin/dashboard`)
10. 商品管理 (`/products`)
11. 用戶管理 (`/users`)
12. 訂單管理 (`/orders`)

## 📋 投影片內容

共 15 張投影片：
- 標題頁（1 張）
- 專案概述（1 張）
- 功能介紹（10 張）
- 技術特色（1 張）
- 資料庫設計（1 張）
- 專案總結（1 張）

每張投影片採用左右分欄：
- **左側**：畫面截圖
- **右側**：功能描述

## 💡 提示

- 截圖建議使用瀏覽器全螢幕截圖
- 可以裁剪截圖，只保留重要部分
- 確保截圖清晰，文字可讀
- 建議截圖解析度至少 1920x1080

