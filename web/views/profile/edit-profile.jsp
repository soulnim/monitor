<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Profile - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="../includes/header.jsp" />
    
    <div class="dashboard-container">
        <jsp:include page="../includes/sidebar.jsp" />
        
        <main class="main-content">
            <div class="page-header">
                <h1>Edit Profile</h1>
                <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary">← Back to Profile</a>
            </div>
            
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error"><c:out value="${errorMessage}" /></div>
            </c:if>
            
            <div class="form-card">
                <form method="post" action="${pageContext.request.contextPath}/profile/edit">
                    
                    <div class="form-group">
                        <label for="username">Username *</label>
                        <input type="text" 
                               name="username" 
                               id="username" 
                               value="${not empty username ? username : user.username}" 
                               required 
                               pattern="[a-zA-Z0-9_-]{3,50}"
                               title="3-50 characters: letters, numbers, underscore, hyphen">
                        <small class="form-text">Your unique username for login</small>
                    </div>
                    
                    <div class="form-group">
                        <label for="email">Email Address *</label>
                        <input type="email" 
                               name="email" 
                               id="email" 
                               value="${not empty email ? email : user.email}" 
                               required>
                        <small class="form-text">Your email for account recovery</small>
                    </div>
                    
                    <div class="form-group">
                        <label for="fullName">Full Name</label>
                        <input type="text" 
                               name="fullName" 
                               id="fullName" 
                               value="${not empty fullName ? fullName : user.fullName}" 
                               maxlength="100">
                        <small class="form-text">Optional: Your full name for display</small>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Save Changes</button>
                        <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
            
            <!-- Help Section -->
            <div class="content-card">
                <h3>Profile Information Guidelines</h3>
                <ul class="help-list">
                    <li><strong>Username:</strong> Must be unique, 3-50 characters. Only letters, numbers, underscore, and hyphen allowed.</li>
                    <li><strong>Email:</strong> Must be a valid email address. Used for account recovery.</li>
                    <li><strong>Full Name:</strong> Optional. Your display name shown in the application.</li>
                </ul>
            </div>
        </main>
    </div>
</body>
</html>
