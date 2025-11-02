/**
 * Notice 元件 JavaScript
 * 提供動態顯示、隱藏、自動消失等功能
 */

// Notice 類型
const NoticeType = {
    ERROR: 'error',
    SUCCESS: 'success',
    WARNING: 'warning',
    INFO: 'info'
};

/**
 * 顯示 Notice
 * @param {string} message - 訊息內容
 * @param {string} type - 訊息類型 (error, success, warning, info)
 * @param {number} duration - 自動消失時間（毫秒），0 表示不自動消失
 */
function showNotice(message, type = NoticeType.INFO, duration = 5000) {
    // 確保 notice-container 存在
    let container = document.getElementById('noticeContainer');
    if (!container) {
        container = document.createElement('div');
        container.id = 'noticeContainer';
        container.className = 'notice-container';
        document.body.appendChild(container);
    }

    // 創建 notice 元素
    const notice = document.createElement('div');
    notice.className = `notice notice-${type}`;
    notice.setAttribute('role', 'alert');

    // 設置圖標
    const icons = {
        error: '⚠️',
        success: '✅',
        warning: '⚠️',
        info: 'ℹ️'
    };

    // 設置內容
    notice.innerHTML = `
        <div class="notice-content">
            <span class="notice-icon">${icons[type] || icons.info}</span>
            <span class="notice-message">${escapeHtml(message)}</span>
        </div>
        <button class="notice-close" onclick="removeNotice(this.parentElement)" aria-label="關閉">×</button>
    `;

    // 添加到容器
    container.appendChild(notice);

    // 觸發動畫
    setTimeout(() => {
        notice.style.opacity = '1';
    }, 10);

    // 自動消失
    if (duration > 0) {
        setTimeout(() => {
            removeNotice(notice);
        }, duration);
    }

    return notice;
}

/**
 * 移除 Notice
 * @param {HTMLElement} notice - Notice 元素
 */
function removeNotice(notice) {
    if (!notice) return;

    // 添加淡出動畫
    notice.classList.add('fade-out');

    // 動畫結束後移除元素
    setTimeout(() => {
        if (notice.parentElement) {
            notice.remove();
        }
        // 如果容器為空，移除容器
        const container = document.getElementById('noticeContainer');
        if (container && container.children.length === 0) {
            container.remove();
        }
    }, 300);
}

/**
 * 顯示錯誤訊息
 * @param {string} message - 訊息內容
 * @param {number} duration - 自動消失時間
 */
function showError(message, duration = 5000) {
    return showNotice(message, NoticeType.ERROR, duration);
}

/**
 * 顯示成功訊息
 * @param {string} message - 訊息內容
 * @param {number} duration - 自動消失時間
 */
function showSuccess(message, duration = 5000) {
    return showNotice(message, NoticeType.SUCCESS, duration);
}

/**
 * 顯示警告訊息
 * @param {string} message - 訊息內容
 * @param {number} duration - 自動消失時間
 */
function showWarning(message, duration = 5000) {
    return showNotice(message, NoticeType.WARNING, duration);
}

/**
 * 顯示資訊訊息
 * @param {string} message - 訊息內容
 * @param {number} duration - 自動消失時間
 */
function showInfo(message, duration = 5000) {
    return showNotice(message, NoticeType.INFO, duration);
}

/**
 * 清除所有 Notice
 */
function clearNotices() {
    const container = document.getElementById('noticeContainer');
    if (container) {
        const notices = container.querySelectorAll('.notice');
        notices.forEach(notice => {
            removeNotice(notice);
        });
    }
}

/**
 * HTML 轉義（防止 XSS）
 * @param {string} text - 要轉義的文字
 */
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// 頁面載入完成後，自動處理 Thymeleaf 的訊息
document.addEventListener('DOMContentLoaded', function() {
    // 檢查 Thymeleaf flash attributes (success, error)
    const successMsg = document.body.getAttribute('data-success') || 
                      (window.location.search.includes('success=') ? decodeURIComponent(window.location.search.split('success=')[1].split('&')[0]) : null);
    const errorMsg = document.body.getAttribute('data-error') || 
                     (window.location.search.includes('error=') ? decodeURIComponent(window.location.search.split('error=')[1].split('&')[0]) : null);

    // 如果有成功訊息，顯示它
    if (successMsg) {
        showSuccess(successMsg, 5000);
    }

    // 如果有錯誤訊息，顯示它
    if (errorMsg) {
        showError(errorMsg, 5000);
    }

    // 自動為 Thymeleaf 生成的 notice 添加自動消失功能
    const notices = document.querySelectorAll('.notice');
    notices.forEach(notice => {
        // 錯誤訊息 5 秒後消失
        if (notice.classList.contains('notice-error')) {
            setTimeout(() => removeNotice(notice), 5000);
        }
        // 成功訊息 3 秒後消失
        else if (notice.classList.contains('notice-success')) {
            setTimeout(() => removeNotice(notice), 5000);
        }
        // 其他訊息 5 秒後消失
        else {
            setTimeout(() => removeNotice(notice), 5000);
        }
    });
});

