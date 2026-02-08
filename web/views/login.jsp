<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <div class="form-container">
            <h1>Welcome Back</h1>
            <p class="subtitle">Login to access your dashboard</p>
            
            <!-- Display error message if any -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error">
                    <c:out value="${errorMessage}" />
                </div>
            </c:if>
            
            <!-- Display logout success message -->
            <c:if test="${param.logout == 'success'}">
                <div class="alert alert-success">
                    You have been successfully logged out.
                </div>
            </c:if>
            
            <form action="${pageContext.request.contextPath}/login" method="post" class="auth-form">
                
                <div class="form-group">
                    <label for="email">Email or Username *</label>
                    <input type="text" 
                           id="email" 
                           name="email" 
                           value="${email}" 
                           required 
                           placeholder="Enter email or username"
                           autofocus>
                </div>
                
                <div class="form-group">
                    <label for="password">Password *</label>
                    <input type="password" 
                           id="password" 
                           name="password" 
                           required 
                           placeholder="Enter password">
                </div>
                
                <div class="form-group form-check">
                    <input type="checkbox" 
                           id="rememberMe" 
                           name="rememberMe" 
                           value="true">
                    <label for="rememberMe">Remember me</label>
                </div>
                
                <button type="submit" class="btn btn-primary btn-block">Login</button>
            </form>
            
            <div class="form-footer">
                <p>Don't have an account? 
                    <a href="${pageContext.request.contextPath}/register">Register here</a>
                </p>
                <p>
                    <a href="${pageContext.request.contextPath}/forgot-password">Forgot password?</a>
                </p>
            </div>
        </div>
    </div>
</body>
</html>
