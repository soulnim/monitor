<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <div class="form-container">
            <h1>Create Account</h1>
            <p class="subtitle">Join Monitor to manage your daily life</p>
            
            <!-- Display error message if any -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error">
                    <c:out value="${errorMessage}" />
                </div>
            </c:if>
            
            <!-- Display success message if any -->
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success">
                    <c:out value="${successMessage}" />
                </div>
            </c:if>
            
            <form action="${pageContext.request.contextPath}/register" method="post" class="auth-form">
                
                <div class="form-group">
                    <label for="username">Username *</label>
                    <input type="text" 
                           id="username" 
                           name="username" 
                           value="${username}" 
                           required 
                           placeholder="Enter username"
                           pattern="[a-zA-Z0-9_-]{3,50}"
                           title="3-50 characters: letters, numbers, underscore, hyphen">
                </div>
                
                <div class="form-group">
                    <label for="email">Email *</label>
                    <input type="email" 
                           id="email" 
                           name="email" 
                           value="${email}" 
                           required 
                           placeholder="Enter email">
                </div>
                
                <div class="form-group">
                    <label for="fullName">Full Name</label>
                    <input type="text" 
                           id="fullName" 
                           name="fullName" 
                           value="${fullName}" 
                           placeholder="Enter full name">
                </div>
                
                <div class="form-group">
                    <label for="password">Password *</label>
                    <input type="password" 
                           id="password" 
                           name="password" 
                           required 
                           placeholder="Enter password"
                           minlength="8">
                    <small class="form-text">
                        Must be at least 8 characters with uppercase, lowercase, and number
                    </small>
                </div>
                
                <div class="form-group">
                    <label for="confirmPassword">Confirm Password *</label>
                    <input type="password" 
                           id="confirmPassword" 
                           name="confirmPassword" 
                           required 
                           placeholder="Confirm password"
                           minlength="8">
                </div>
                
                <button type="submit" class="btn btn-primary btn-block">Register</button>
            </form>
            
            <div class="form-footer">
                <p>Already have an account? 
                    <a href="${pageContext.request.contextPath}/login">Login here</a>
                </p>
            </div>
        </div>
    </div>
</body>
</html>
