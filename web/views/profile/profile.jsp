<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="../includes/header.jsp" />
    
    <div class="dashboard-container">
        <jsp:include page="../includes/sidebar.jsp" />
        
        <main class="main-content">
            <div class="page-header">
                <h1>My Profile</h1>
                <div class="page-actions">
                    <a href="${pageContext.request.contextPath}/profile/edit" class="btn btn-primary">Edit Profile</a>
                    <a href="${pageContext.request.contextPath}/profile/change-password" class="btn btn-secondary">Change Password</a>
                </div>
            </div>
            
            <c:if test="${param.updated}">
                <div class="alert alert-success">Profile updated successfully!</div>
            </c:if>
            
            <c:if test="${param.password == 'changed'}">
                <div class="alert alert-success">Password changed successfully!</div>
            </c:if>
            
            <!-- Profile Information Card -->
            <div class="profile-container">
                <div class="profile-card">
                    <div class="profile-header">
                        <div class="profile-avatar">
                            <c:choose>
                                <c:when test="${not empty user.profilePicture}">
                                    <img src="${user.profilePicture}" alt="Profile Picture">
                                </c:when>
                                <c:otherwise>
                                    <div class="avatar-placeholder">
                                        <c:out value="${user.username.substring(0,1).toUpperCase()}" />
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        
                        <div class="profile-info">
                            <h2><c:out value="${not empty user.fullName ? user.fullName : user.username}" /></h2>
                            <p class="profile-username">@<c:out value="${user.username}" /></p>
                        </div>
                    </div>
                    
                    <div class="profile-details">
                        <div class="detail-row">
                            <div class="detail-label">
                                <span class="icon">📧</span> Email
                            </div>
                            <div class="detail-value">
                                <c:out value="${user.email}" />
                            </div>
                        </div>
                        
                        <div class="detail-row">
                            <div class="detail-label">
                                <span class="icon">👤</span> Full Name
                            </div>
                            <div class="detail-value">
                                <c:choose>
                                    <c:when test="${not empty user.fullName}">
                                        <c:out value="${user.fullName}" />
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">Not set</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        
                        <div class="detail-row">
                            <div class="detail-label">
                                <span class="icon">📅</span> Member Since
                            </div>
                            <div class="detail-value">
                                <fmt:formatDate value="${user.createdAt}" pattern="MMMM dd, yyyy" />
                            </div>
                        </div>
                        
                        <div class="detail-row">
                            <div class="detail-label">
                                <span class="icon">🔄</span> Last Updated
                            </div>
                            <div class="detail-value">
                                <fmt:formatDate value="${user.updatedAt}" pattern="MMM dd, yyyy HH:mm" />
                            </div>
                        </div>
                    </div>
                    
                    <div class="profile-actions">
                        <a href="${pageContext.request.contextPath}/profile/edit" class="btn btn-primary btn-block">
                            Edit Profile Information
                        </a>
                        <a href="${pageContext.request.contextPath}/profile/change-password" class="btn btn-secondary btn-block">
                            Change Password
                        </a>
                    </div>
                </div>
                
                <!-- Account Statistics -->
                <div class="stats-card">
                    <h3>Account Overview</h3>
                    
                    <div class="stat-item">
                        <div class="stat-icon">📊</div>
                        <div class="stat-content">
                            <h4>Dashboard</h4>
                            <p>Manage your daily activities</p>
                            <a href="${pageContext.request.contextPath}/dashboard">Go to Dashboard →</a>
                        </div>
                    </div>
                    
                    <div class="stat-item">
                        <div class="stat-icon">💰</div>
                        <div class="stat-content">
                            <h4>Financial Management</h4>
                            <p>Track income and expenses</p>
                            <a href="${pageContext.request.contextPath}/finance/transactions">View Finances →</a>
                        </div>
                    </div>
                    
                    <div class="stat-item">
                        <div class="stat-icon">✓</div>
                        <div class="stat-content">
                            <h4>Task Management</h4>
                            <p>Organize your to-do list</p>
                            <a href="${pageContext.request.contextPath}/tasks">View Tasks →</a>
                        </div>
                    </div>
                    
                    <div class="stat-item">
                        <div class="stat-icon">🎯</div>
                        <div class="stat-content">
                            <h4>Goals & Milestones</h4>
                            <p>Track your progress</p>
                            <a href="${pageContext.request.contextPath}/goals">View Goals →</a>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
</body>
</html>
