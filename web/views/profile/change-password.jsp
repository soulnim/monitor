<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Change Password - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="../includes/header.jsp" />
    
    <div class="dashboard-container">
        <jsp:include page="../includes/sidebar.jsp" />
        
        <main class="main-content">
            <div class="page-header">
                <h1>Change Password</h1>
                <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary">← Back to Profile</a>
            </div>
            
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error"><c:out value="${errorMessage}" /></div>
            </c:if>
            
            <div class="form-card">
                <form method="post" action="${pageContext.request.contextPath}/profile/change-password" id="passwordForm">
                    
                    <div class="form-group">
                        <label for="currentPassword">Current Password *</label>
                        <input type="password" 
                               name="currentPassword" 
                               id="currentPassword" 
                               required
                               autocomplete="current-password">
                    </div>
                    
                    <div class="form-group">
                        <label for="newPassword">New Password *</label>
                        <input type="password" 
                               name="newPassword" 
                               id="newPassword" 
                               required
                               minlength="6"
                               autocomplete="new-password">
                        <small class="form-text">Must be at least 6 characters</small>
                    </div>
                    
                    <div class="form-group">
                        <label for="confirmPassword">Confirm New Password *</label>
                        <input type="password" 
                               name="confirmPassword" 
                               id="confirmPassword" 
                               required
                               minlength="6"
                               autocomplete="new-password">
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Change Password</button>
                        <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
            
            <!-- Security Tips -->
            <div class="content-card">
                <h3>🔒 Password Security Tips</h3>
                <ul class="help-list">
                    <li><strong>Length:</strong> Use at least 6 characters (longer is better)</li>
                    <li><strong>Complexity:</strong> Mix uppercase, lowercase, numbers, and symbols</li>
                    <li><strong>Uniqueness:</strong> Don't reuse passwords from other sites</li>
                    <li><strong>Avoid:</strong> Personal information, common words, or simple patterns</li>
                    <li><strong>Regular Updates:</strong> Change your password periodically</li>
                </ul>
            </div>
        </main>
    </div>
    
    <script>
        document.getElementById('passwordForm').addEventListener('submit', function(e) {
            var newPassword = document.getElementById('newPassword').value;
            var confirmPassword = document.getElementById('confirmPassword').value;
            
            if (newPassword !== confirmPassword) {
                e.preventDefault();
                alert('New passwords do not match!');
                return false;
            }
        });
    </script>
</body>
</html>
