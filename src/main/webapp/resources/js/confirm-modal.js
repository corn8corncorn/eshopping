/**
 * 確認刪除 Modal JavaScript
 * 提供顯示、隱藏、確認刪除等功能
 */

let currentDeleteForm = null;
let currentDeleteUrl = null;
let currentDeleteMethod = 'POST';

/**
 * 顯示確認刪除 Modal
 * @param {string} message - 要顯示的訊息
 * @param {HTMLFormElement|string} formOrUrl - 要提交的表單元素或 URL
 * @param {string} method - HTTP 方法（POST, GET）
 */
function showConfirmModal(message, formOrUrl, method = 'POST') {
    const modal = document.getElementById('confirmDeleteModal');
    const modalMessage = document.getElementById('modalMessage');
    const confirmBtn = document.getElementById('confirmDeleteBtn');

    if (!modal) {
        console.error('確認刪除 Modal 不存在');
        return;
    }

    // 設置訊息
    modalMessage.textContent = message || '確定要刪除此項目嗎？';

    // 儲存要提交的表單或 URL
    if (formOrUrl instanceof HTMLFormElement) {
        currentDeleteForm = formOrUrl;
        currentDeleteUrl = null;
    } else if (typeof formOrUrl === 'string') {
        currentDeleteForm = null;
        currentDeleteUrl = formOrUrl;
    }

    currentDeleteMethod = method || 'POST';

    // 設置確認按鈕點擊事件
    confirmBtn.onclick = function() {
        confirmDelete();
    };

    // 顯示 Modal
    modal.classList.add('show');
    document.body.style.overflow = 'hidden'; // 防止背景滾動
}

/**
 * 關閉確認刪除 Modal
 */
function closeConfirmModal() {
    const modal = document.getElementById('confirmDeleteModal');
    if (modal) {
        modal.classList.remove('show');
        document.body.style.overflow = ''; // 恢復背景滾動
        currentDeleteForm = null;
        currentDeleteUrl = null;
    }
}

/**
 * 確認刪除操作
 */
function confirmDelete() {
    if (currentDeleteForm) {
        // 提交表單
        currentDeleteForm.submit();
    } else if (currentDeleteUrl) {
        // 如果是 GET 請求，直接導航
        if (currentDeleteMethod.toUpperCase() === 'GET') {
            window.location.href = currentDeleteUrl;
        } else {
            // POST/PUT/DELETE 請求：創建並提交表單
            const form = document.createElement('form');
            form.method = currentDeleteMethod;
            form.action = currentDeleteUrl;
            document.body.appendChild(form);
            form.submit();
        }
    } else {
        console.error('沒有指定要刪除的目標');
    }

    closeConfirmModal();
}

// 點擊 Modal 背景關閉
document.addEventListener('DOMContentLoaded', function() {
    const modal = document.getElementById('confirmDeleteModal');
    if (modal) {
        modal.addEventListener('click', function(e) {
            if (e.target === modal) {
                closeConfirmModal();
            }
        });

        // ESC 鍵關閉
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape' && modal.classList.contains('show')) {
                closeConfirmModal();
            }
        });
    }
});

/**
 * 綁定刪除按鈕事件
 */
function bindDeleteButtons() {
    // 處理 data-delete-url 屬性
    document.querySelectorAll('[data-delete-url]').forEach(function(element) {
        // 避免重複綁定
        if (element.hasAttribute('data-modal-bound')) {
            return;
        }
        element.setAttribute('data-modal-bound', 'true');
        
        element.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            const url = this.getAttribute('data-delete-url');
            const message = this.getAttribute('data-delete-message') || '確定要刪除此項目嗎？';
            const method = this.getAttribute('data-delete-method') || 'POST';
            showConfirmModal(message, url, method);
        });
    });

    // 處理 data-delete-form 屬性（表單內的刪除按鈕）
    document.querySelectorAll('[data-delete-form]').forEach(function(button) {
        // 避免重複綁定
        if (button.hasAttribute('data-modal-bound')) {
            return;
        }
        button.setAttribute('data-modal-bound', 'true');
        
        button.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            const formId = this.getAttribute('data-delete-form');
            console.log('刪除按鈕點擊，表單ID:', formId); // 調試用
            const form = document.getElementById(formId);
            if (form) {
                const message = this.getAttribute('data-delete-message') || '確定要刪除此項目嗎？';
                console.log('顯示確認 Modal，訊息:', message); // 調試用
                showConfirmModal(message, form);
            } else {
                console.error('找不到表單:', formId);
                console.log('頁面中所有表單:', Array.from(document.querySelectorAll('form')).map(f => f.id));
            }
        });
    });
}

/**
 * 為刪除按鈕綁定確認 Modal
 * 自動為所有 data-delete-url 或 data-delete-form 屬性的元素綁定事件
 */
document.addEventListener('DOMContentLoaded', function() {
    bindDeleteButtons();
    
    // 延遲一下再綁定，確保 Thymeleaf 渲染完成
    setTimeout(bindDeleteButtons, 100);

    // 處理 class="delete-btn" 的按鈕（自動查找父級表單）
    document.querySelectorAll('button.delete-btn, .btn[onclick*="delete"]').forEach(function(button) {
        // 避免重複綁定
        if (button.hasAttribute('data-modal-bound')) {
            return;
        }
        button.setAttribute('data-modal-bound', 'true');

        // 如果是表單內的提交按鈕
        const form = button.closest('form');
        if (form && form.action && form.action.includes('/delete/')) {
            button.addEventListener('click', function(e) {
                e.preventDefault();
                const message = this.getAttribute('data-delete-message') || 
                               this.textContent.trim() + ' - 確定要刪除此項目嗎？';
                showConfirmModal(message, form);
            });
        }
    });
});

