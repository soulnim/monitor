<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="includes/header.jsp" />
    
    <div class="dashboard-container">
        <jsp:include page="includes/sidebar.jsp" />
        
        <main class="main-content">
            <div class="page-header">
                <h1>Dashboard</h1>
                <p class="page-subtitle">Welcome back! Here's your overview</p>
            </div>
            
            <!-- Quick Stats -->
            <div class="summary-cards">
                <div class="summary-card income">
                    <div class="card-icon">💵</div>
                    <div class="card-info">
                        <h3>This Month Income</h3>
                        <p class="amount">$<fmt:formatNumber value="${monthlyIncome}" pattern="#,##0.00" /></p>
                    </div>
                </div>
                
                <div class="summary-card expense">
                    <div class="card-icon">💸</div>
                    <div class="card-info">
                        <h3>This Month Expenses</h3>
                        <p class="amount">$<fmt:formatNumber value="${monthlyExpenses}" pattern="#,##0.00" /></p>
                    </div>
                </div>
                
                <div class="summary-card">
                    <div class="card-icon">✓</div>
                    <div class="card-info">
                        <h3>Pending Tasks</h3>
                        <p class="amount">${pendingTasks}</p>
                    </div>
                </div>
                
                <div class="summary-card ${upcomingBills > 0 ? 'expense' : ''}">
                    <div class="card-icon">🧾</div>
                    <div class="card-info">
                        <h3>Upcoming Bills</h3>
                        <p class="amount">${upcomingBills}</p>
                    </div>
                </div>
            </div>
            
            <!-- Main Dashboard Grid -->
            <div class="dashboard-grid">
                
                <!-- Financial Summary -->
                <div class="dashboard-card">
                    <div class="card-header">
                        <h3>💰 Financial Summary</h3>
                        <span class="card-period">This Month</span>
                    </div>
                    <div class="card-body">
                        <div class="stat-row">
                            <span class="stat-label">Income:</span>
                            <span class="stat-value income">$<fmt:formatNumber value="${monthlyIncome}" pattern="#,##0.00" /></span>
                        </div>
                        <div class="stat-row">
                            <span class="stat-label">Expenses:</span>
                            <span class="stat-value expense">$<fmt:formatNumber value="${monthlyExpenses}" pattern="#,##0.00" /></span>
                        </div>
                        <div class="stat-row total">
                            <span class="stat-label">Net Savings:</span>
                            <span class="stat-value">$<fmt:formatNumber value="${netSavings}" pattern="#,##0.00" /></span>
                        </div>
                    </div>
                    <div class="card-footer">
                        <a href="${pageContext.request.contextPath}/finance/transactions">View Details →</a>
                    </div>
                </div>
                
                <!-- Today's Tasks -->
                <div class="dashboard-card">
                    <div class="card-header">
                        <h3>✓ Today's Tasks</h3>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty todayTasks}">
                                <p class="empty-state">No tasks for today</p>
                            </c:when>
                            <c:otherwise>
                                <ul class="task-list">
                                    <c:forEach items="${todayTasks}" var="task" varStatus="status">
                                        <c:if test="${status.index < 5}">
                                            <li>
                                                <span class="badge badge-${task.priority == 'HIGH' ? 'danger' : 'secondary'}">${task.priority}</span>
                                                <c:out value="${task.title}" />
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="card-footer">
                        <a href="${pageContext.request.contextPath}/tasks">View All Tasks →</a>
                    </div>
                </div>
                
                <!-- Upcoming Bills -->
                <div class="dashboard-card">
                    <div class="card-header">
                        <h3>🧾 Upcoming Bills</h3>
                        <span class="card-period">Next 7 Days</span>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty upcomingBillsList}">
                                <p class="empty-state">No upcoming bills</p>
                            </c:when>
                            <c:otherwise>
                                <ul class="bill-list">
                                    <c:forEach items="${upcomingBillsList}" var="bill" varStatus="status">
                                        <c:if test="${status.index < 5}">
                                            <li>
                                                <span><c:out value="${bill.billName}" /></span>
                                                <span class="amount-small">$<fmt:formatNumber value="${bill.amount}" pattern="#,##0.00" /></span>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="card-footer">
                        <a href="${pageContext.request.contextPath}/bills">Manage Bills →</a>
                    </div>
                </div>
                
                <!-- Active Goals -->
                <div class="dashboard-card">
                    <div class="card-header">
                        <h3>🎯 Active Goals</h3>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty activeGoals}">
                                <p class="empty-state">No active goals</p>
                            </c:when>
                            <c:otherwise>
                                <ul class="goal-list">
                                    <c:forEach items="${activeGoals}" var="goal" varStatus="status">
                                        <c:if test="${status.index < 3}">
                                            <li>
                                                <div class="goal-item">
                                                    <span><c:out value="${goal.title}" /></span>
                                                    <div class="progress-bar">
                                                        <div class="progress-fill" style="width: ${goal.completionPercentage}%"></div>
                                                    </div>
                                                    <span class="progress-text">${goal.completionPercentage}%</span>
                                                </div>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="card-footer">
                        <a href="${pageContext.request.contextPath}/goals">View All Goals →</a>
                    </div>
                </div>
                
                <!-- Upcoming Events -->
                <div class="dashboard-card">
                    <div class="card-header">
                        <h3>📅 Upcoming Events</h3>
                        <span class="card-period">Next 3 Days</span>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty upcomingEvents}">
                                <p class="empty-state">No upcoming events</p>
                            </c:when>
                            <c:otherwise>
                                <ul class="event-list">
                                    <c:forEach items="${upcomingEvents}" var="event" varStatus="status">
                                        <c:if test="${status.index < 5}">
                                            <li>
                                                <fmt:formatDate value="${event.eventDate}" pattern="MMM dd" />:
                                                <c:out value="${event.title}" />
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="card-footer">
                        <a href="${pageContext.request.contextPath}/schedule">View Calendar →</a>
                    </div>
                </div>
                
                <!-- Recent Notes -->
                <div class="dashboard-card">
                    <div class="card-header">
                        <h3>📝 Recent Notes</h3>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty recentNotes}">
                                <p class="empty-state">No notes yet</p>
                            </c:when>
                            <c:otherwise>
                                <ul class="note-list">
                                    <c:forEach items="${recentNotes}" var="note" varStatus="status">
                                        <c:if test="${status.index < 3}">
                                            <li>
                                                <strong><c:out value="${note.title}" /></strong>
                                                <p class="note-preview"><c:out value="${note.preview}" /></p>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="card-footer">
                        <a href="${pageContext.request.contextPath}/notes">View All Notes →</a>
                    </div>
                </div>
                
            </div>
        </main>
    </div>
</body>
</html>
