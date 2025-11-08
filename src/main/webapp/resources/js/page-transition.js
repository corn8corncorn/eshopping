/**
 * 頁面過渡效果腳本
 * 用於減少 Spring MVC + Thymeleaf 全頁刷新時的閃爍感
 */

(function() {
    'use strict';
    
    // 頁面載入完成後移除載入遮罩
    function hideLoader() {
        const loader = document.querySelector('.page-loader');
        if (loader) {
            setTimeout(function() {
                loader.classList.add('hidden');
                setTimeout(function() {
                    loader.remove();
                }, 300);
            }, 100);
        }
    }
    
    // 點擊連結時顯示載入遮罩
    function setupLinkTransitions() {
        const links = document.querySelectorAll('a[href]');
        links.forEach(function(link) {
            link.addEventListener('click', function(e) {
                // 排除外部連結和錨點連結
                if (this.hostname && this.hostname !== window.location.hostname) {
                    return;
                }
                if (this.hash && this.hash !== '') {
                    return;
                }
                
                // 排除表單提交連結
                if (this.closest('form')) {
                    return;
                }
                
                // 創建載入遮罩
                const loader = document.createElement('div');
                loader.className = 'page-loader';
                loader.innerHTML = '<div style="font-size: 18px; color: #c99773;">載入中...</div>';
                document.body.appendChild(loader);
            });
        });
    }
    
    // 頁面載入完成後執行
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', function() {
            setupLinkTransitions();
        });
    } else {
        setupLinkTransitions();
    }
    
    // 頁面完全載入後移除遮罩
    window.addEventListener('load', hideLoader);
    
    // 如果頁面已經載入完成，立即執行
    if (document.readyState === 'complete') {
        hideLoader();
    }
})();

