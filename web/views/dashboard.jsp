<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <!-- Include header -->
    <jsp:include page="includes/header.jsp" />
    
    <div class="dashboard-container">
        <!-- Sidebar Navigation -->
        <aside class="sidebar">
            <nav class="sidebar-nav">
                <a href="${pageContext.request.contextPath}/dashboard" class="nav-item active">
                    <span class="icon">📊</span> Dashboard
                </a>
                <a href="${pageContext.request.contextPath}/finance/transactions" class="nav-item">
                    <span class="icon">💰</span> Finances
                </a>
                <a href="${pageContext.request.contextPath}/tasks" class="nav-item">
                    <span class="icon">✓</span> Tasks
                </a>
                <a href="${pageContext.request.contextPath}/goals" class="nav-item">
                    <span class="icon">🎯</span> Goals
                </a>
                <a href="${pageContext.request.contextPath}/schedule" class="nav-item">
                    <span class="icon">📅</span> Schedule
                </a>
                <a href="${pageContext.request.contextPath}/bills" class="nav-item">
                    <span class="icon">🧾</span> Bills
                </a>
                <a href="${pageContext.request.contextPath}/notes" class="nav-item">
                    <span class="icon">📝</span> Notes
                </a>
            </nav>
        </aside>
        
        <!-- Main Content -->
        <main class="main-content">
            <div class="page-header">
                <h1>Dashboard</h1>
                <p class="page-subtitle">Overview of your daily life management</p>
            </div>
            
            <!-- Welcome Message -->
            <div class="welcome-card">
                <h2><c:out value="${welcomeMessage}" /></h2>
                <p>Here's your summary for today</p>
            </div>
            
            <!-- Dashboard Grid -->
            <div class="dashboard-grid">
                
                <!-- Financial Summary Card -->
                <div class="dashboard-card">
                    <div class="card-header">
                        <h3>💰 Financial Summary</h3>
                        <span class="card-period">This Month</span>
                    </div>
                    <div class="card-body">
                        <div class="stat-row">
                            <span class="stat-label">Income:</span>
                            <span class="stat-value income">$0.00</span>
                        </div>
                        <div class="stat-row">
                            <span class="stat-label">Expenses:</span>
                            <span class="stat-value expense">$0.00</span>
                        </div>
                        <div class="stat-row total">
                            <span class="stat-label">Net Savings:</span>
                            <span class="stat-value">$0.00</span>
                        </div>
                    </div>
                    <div class="card-footer">
                        <a href="${pageContext.request.contextPath}/finance/transactions">View Details →</a>
                    </div>
                </div>
                
                <!-- Tasks Card -->
                <div class="dashboard-card">
                    <div class="card-header">
                        <h3>✓ Tasks</h3>
                        <span class="card-period">Today</span>
                    </div>
                    <div class="card-body">
                        <p class="empty-state">No tasks for today</p>
                        <p class="empty-subtext">Start by adding your first task</p>
                    </div>
                    <div class="card-footer">
                        <a href="${pageContext.request.contextPath}/tasks">Manage Tasks →</a>
                    </div>
                </div>
                
                <!-- Upcoming Bills Card -->
                <div class="dashboard-card">
                    <div class="card-header">
                        <h3>🧾 Upcoming Bills</h3>
                        <span class="card-period">Next 7 Days</span>
                    </div>
                    <div class="card-body">
                        <p class="empty-state">No upcoming bills</p>
                        <p class="empty-subtext">All caught up!</p>
                    </div>
                    <div class="card-footer">
                        <a href="${pageContext.request.contextPath}/bills">View All Bills →</a>
                    </div>
                </div>
                
                <!-- Upcoming Events Card -->
                <div class="dashboard-card">
                    <div class="card-header">
                        <h3>📅 Upcoming Events</h3>
                        <span class="card-period">Next 3 Days</span>
                    </div>
                    <div class="card-body">
                        <p class="empty-state">No upcoming events</p>
                        <p class="empty-subtext">Your schedule is clear</p>
                    </div>
                    <div class="card-footer">
                        <a href="${pageContext.request.contextPath}/schedule">View Calendar →</a>
                    </div>
                </div>
                
                <!-- Active Goals Card -->
                <div class="dashboard-card">
                    <div class="card-header">
                        <h3>🎯 Active Goals</h3>
                    </div>
                    <div class="card-body">
                        <p class="empty-state">No active goals</p>
                        <p class="empty-subtext">Set your first goal to get started</p>
                    </div>
                    <div class="card-footer">
                        <a href="${pageContext.request.contextPath}/goals">Manage Goals →</a>
                    </div>
                </div>
                
                <!-- Recent Notes Card -->
                <div class="dashboard-card">
                    <div class="card-header">
                        <h3>📝 Recent Notes</h3>
                    </div>
                    <div class="card-body">
                        <p class="empty-state">No notes yet</p>
                        <p class="empty-subtext">Create your first note</p>
                    </div>
                    <div class="card-footer">
                        <a href="${pageContext.request.contextPath}/notes">View All Notes →</a>
                    </div>
                </div>
                
            </div>
        </main>
    </div>
    
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
