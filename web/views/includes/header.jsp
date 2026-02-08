<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<header class="main-header">
    <div class="header-content">
        <div class="header-left">
            <h1 class="logo">
                <a href="${pageContext.request.contextPath}/dashboard">Monitor</a>
            </h1>
        </div>
        
        <div class="header-right">
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
// Toggle user menu dropdown
document.addEventListener('DOMContentLoaded', function() {
    const menuToggle = document.getElementById('userMenuToggle');
    const menuDropdown = document.getElementById('userMenuDropdown');
    
    if (menuToggle && menuDropdown) {
        menuToggle.addEventListener('click', function(e) {
            e.stopPropagation();
            menuDropdown.classList.toggle('show');
        });
        
        // Close dropdown when clicking outside
        document.addEventListener('click', function() {
            menuDropdown.classList.remove('show');
        });
    }
});
</script>
