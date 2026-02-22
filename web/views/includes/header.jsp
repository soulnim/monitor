<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="serverTheme" value="${empty sessionScope.theme ? 'dark' : sessionScope.theme}" />

<header class="main-header">
    <div class="header-content">
        <div class="header-left">
            <h1 class="logo">
                <a href="${pageContext.request.contextPath}/dashboard">Monitor</a>
            </h1>
        </div>

        <div class="header-right">
            <button type="button" class="btn btn-secondary btn-theme-toggle" id="themeToggleBtn">🌓 Theme</button>
            <span class="user-greeting">
                Hello, <c:out value="${sessionScope.username}" />
            </span>

            <div class="user-menu">
                <button class="user-menu-toggle" id="userMenuToggle">
                    <span class="user-avatar">
                        <c:out value="${sessionScope.username.substring(0,1).toUpperCase()}" />
                    </span>
                </button>

                <div class="user-menu-dropdown" id="userMenuDropdown">
                    <a href="${pageContext.request.contextPath}/profile" class="menu-item">
                        👤 My Profile
                    </a>
                    <a href="${pageContext.request.contextPath}/settings" class="menu-item">
                        ⚙️ Settings
                    </a>
                    <hr class="menu-divider">
                    <a href="${pageContext.request.contextPath}/logout" class="menu-item">
                        🚪 Logout
                    </a>
                </div>
            </div>
        </div>
    </div>
</header>

<script>
(function() {
    var serverTheme = '<c:out value="${serverTheme}" />';
    var hasUpdatedPreferences = window.location.search.indexOf('updated=preferences') !== -1;
    var storedTheme = localStorage.getItem('monitor-theme');
    var themeToApply = (hasUpdatedPreferences || !storedTheme) ? serverTheme : storedTheme;

    document.documentElement.setAttribute('data-theme', themeToApply);

    if (hasUpdatedPreferences || !storedTheme) {
        localStorage.setItem('monitor-theme', themeToApply);
    }
})();

// Toggle user menu dropdown
document.addEventListener('DOMContentLoaded', function() {
    var menuToggle = document.getElementById('userMenuToggle');
    var menuDropdown = document.getElementById('userMenuDropdown');
    var themeToggleBtn = document.getElementById('themeToggleBtn');

    if (menuToggle && menuDropdown) {
        menuToggle.addEventListener('click', function(e) {
            e.stopPropagation();
            menuDropdown.classList.toggle('show');
        });

        document.addEventListener('click', function() {
            menuDropdown.classList.remove('show');
        });
    }

    if (themeToggleBtn) {
        themeToggleBtn.addEventListener('click', function() {
            var current = document.documentElement.getAttribute('data-theme') || 'dark';
            var next = current === 'dark' ? 'light' : 'dark';
            document.documentElement.setAttribute('data-theme', next);
            localStorage.setItem('monitor-theme', next);
        });
    }
});
</script>
