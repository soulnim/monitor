<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=20260222-1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css?v=20260222-1">
    <!-- Chart.js for visualization -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>
    <!-- SortableJS for drag and drop -->
    <script src="https://cdn.jsdelivr.net/npm/sortablejs@1.15.0/Sortable.min.js"></script>
</head>
<body>
    <jsp:include page="includes/header.jsp" />
    
    <div class="dashboard-container">
        <jsp:include page="includes/sidebar.jsp" />
        
        <main class="main-content">
            <div class="page-header">
                <div class="header-left">
                    <h1>Dashboard</h1>
                    <p class="page-subtitle">Welcome back! Here's your personalized overview</p>
                </div>
                <div class="header-actions">
                    <button class="btn btn-primary" id="customizeToggleBtn">
                        <span class="icon">⚙️</span> <span id="customizeBtnText">Customize</span>
                    </button>
                </div>
            </div>
            
            <!-- Quick Stats Bar -->
            <div class="quick-stats">
                <div class="stat-item stat-income">
                    <div class="stat-icon">💵</div>
                    <div class="stat-content">
                        <span class="stat-label">This Month Income</span>
                        <span class="stat-value">$<fmt:formatNumber value="${monthlyIncome}" pattern="#,##0.00" /></span>
                    </div>
                </div>
                
                <div class="stat-item stat-expense">
                    <div class="stat-icon">💸</div>
                    <div class="stat-content">
                        <span class="stat-label">This Month Expenses</span>
                        <span class="stat-value">$<fmt:formatNumber value="${monthlyExpenses}" pattern="#,##0.00" /></span>
                    </div>
                </div>
                
                <div class="stat-item stat-savings">
                    <div class="stat-icon">💰</div>
                    <div class="stat-content">
                        <span class="stat-label">Net Savings</span>
                        <span class="stat-value">$<fmt:formatNumber value="${netSavings}" pattern="#,##0.00" /></span>
                    </div>
                </div>
                
                <div class="stat-item stat-tasks">
                    <div class="stat-icon">✓</div>
                    <div class="stat-content">
                        <span class="stat-label">Pending Tasks</span>
                        <span class="stat-value">${pendingTasks}</span>
                    </div>
                </div>
            </div>
            
            <!-- Widget Grid (Customizable & Draggable) -->
            <div class="widgets-grid" id="widgetsGrid">
                
                <!-- Financial Summary Widget -->
                <div class="widget" data-widget-id="financial-summary" data-widget-type="data">
                    <div class="widget-header">
                        <div class="widget-title">
                            <span class="widget-icon">💰</span>
                            <h3>Financial Summary</h3>
                        </div>
                        <div class="widget-controls">
                            <span class="widget-period">This Month</span>
                            <button class="widget-menu-btn" title="Widget options">⋮</button>
                        </div>
                    </div>
                    <div class="widget-body">
                        <div class="stat-row">
                            <span class="stat-label">Income:</span>
                            <span class="stat-value stat-positive">$<fmt:formatNumber value="${monthlyIncome}" pattern="#,##0.00" /></span>
                        </div>
                        <div class="stat-row">
                            <span class="stat-label">Expenses:</span>
                            <span class="stat-value stat-negative">$<fmt:formatNumber value="${monthlyExpenses}" pattern="#,##0.00" /></span>
                        </div>
                        <div class="stat-divider"></div>
                        <div class="stat-row stat-highlight">
                            <span class="stat-label">Net Savings:</span>
                            <span class="stat-value">$<fmt:formatNumber value="${netSavings}" pattern="#,##0.00" /></span>
                        </div>
                    </div>
                    <div class="widget-footer">
                        <a href="${pageContext.request.contextPath}/finance/transactions" class="widget-link">
                            View Details <span class="arrow">→</span>
                        </a>
                    </div>
                </div>
                
                <!-- Today's Tasks Widget -->
                <div class="widget" data-widget-id="today-tasks" data-widget-type="list">
                    <div class="widget-header">
                        <div class="widget-title">
                            <span class="widget-icon">✓</span>
                            <h3>Today's Tasks</h3>
                        </div>
                        <div class="widget-controls">
                            <span class="widget-badge">${fn:length(todayTasks)}</span>
                            <button class="widget-menu-btn" title="Widget options">⋮</button>
                        </div>
                    </div>
                    <div class="widget-body">
                        <c:choose>
                            <c:when test="${empty todayTasks}">
                                <div class="empty-state">
                                    <span class="empty-icon">✓</span>
                                    <p>No tasks for today</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <ul class="item-list">
                                    <c:forEach items="${todayTasks}" var="task" varStatus="status">
                                        <c:if test="${status.index < 3}">
                                            <li class="item">
                                                <div class="item-content">
                                                    <span class="priority-badge priority-${fn:toLowerCase(task.priority)}">${task.priority}</span>
                                                    <span class="item-title"><c:out value="${task.title}" /></span>
                                                </div>
                                                <span class="item-time">
                                                    <fmt:formatDate value="${task.dueDate}" pattern="HH:mm" />
                                                </span>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                                <c:if test="${fn:length(todayTasks) > 3}">
                                    <p class="more-items">+${fn:length(todayTasks) - 3} more tasks</p>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="widget-footer">
                        <a href="${pageContext.request.contextPath}/tasks" class="widget-link">
                            View All Tasks <span class="arrow">→</span>
                        </a>
                    </div>
                </div>
                
                <!-- Upcoming Bills Widget -->
                <div class="widget" data-widget-id="upcoming-bills" data-widget-type="list">
                    <div class="widget-header">
                        <div class="widget-title">
                            <span class="widget-icon">🧾</span>
                            <h3>Upcoming Bills</h3>
                        </div>
                        <div class="widget-controls">
                            <span class="widget-period">Next 7 Days</span>
                            <button class="widget-menu-btn" title="Widget options">⋮</button>
                        </div>
                    </div>
                    <div class="widget-body">
                        <c:choose>
                            <c:when test="${empty upcomingBillsList}">
                                <div class="empty-state">
                                    <span class="empty-icon">🧾</span>
                                    <p>No upcoming bills</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <ul class="item-list">
                                    <c:forEach items="${upcomingBillsList}" var="bill" varStatus="status">
                                        <c:if test="${status.index < 3}">
                                            <li class="item">
                                                <div class="item-content">
                                                    <span class="item-title"><c:out value="${bill.billName}" /></span>
                                                    <span class="item-subtitle">
                                                        Due <fmt:formatDate value="${bill.dueDate}" pattern="MMM dd" />
                                                    </span>
                                                </div>
                                                <span class="item-amount amount-negative">
                                                    $<fmt:formatNumber value="${bill.amount}" pattern="#,##0.00" />
                                                </span>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                                <c:if test="${fn:length(upcomingBillsList) > 3}">
                                    <p class="more-items">+${fn:length(upcomingBillsList) - 3} more bills</p>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="widget-footer">
                        <a href="${pageContext.request.contextPath}/bills" class="widget-link">
                            Manage Bills <span class="arrow">→</span>
                        </a>
                    </div>
                </div>
                
                <!-- Expense Chart Widget -->
                <div class="widget widget-chart" data-widget-id="expense-chart" data-widget-type="chart">
                    <div class="widget-header">
                        <div class="widget-title">
                            <span class="widget-icon">📊</span>
                            <h3>Expense Breakdown</h3>
                        </div>
                        <div class="widget-controls">
                            <span class="widget-period">This Month</span>
                            <button class="widget-menu-btn" title="Widget options">⋮</button>
                        </div>
                    </div>
                    <div class="widget-body">
                        <canvas id="expenseChart"></canvas>
                    </div>
                </div>
                
                <!-- Active Goals Widget -->
                <div class="widget" data-widget-id="active-goals" data-widget-type="progress">
                    <div class="widget-header">
                        <div class="widget-title">
                            <span class="widget-icon">🎯</span>
                            <h3>Active Goals</h3>
                        </div>
                        <div class="widget-controls">
                            <button class="widget-menu-btn" title="Widget options">⋮</button>
                        </div>
                    </div>
                    <div class="widget-body">
                        <c:choose>
                            <c:when test="${empty activeGoals}">
                                <div class="empty-state">
                                    <span class="empty-icon">🎯</span>
                                    <p>No active goals</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <ul class="progress-list">
                                    <c:forEach items="${activeGoals}" var="goal" varStatus="status">
                                        <c:if test="${status.index < 3}">
                                            <li class="progress-item">
                                                <div class="progress-header">
                                                    <span class="progress-title"><c:out value="${goal.title}" /></span>
                                                    <span class="progress-percent">${goal.completionPercentage}%</span>
                                                </div>
                                                <div class="progress-bar-container">
                                                    <div class="progress-bar-fill" style="width: ${goal.completionPercentage}%"></div>
                                                </div>
                                                <div class="progress-meta">
                                                    <span class="progress-category">${goal.categoryName}</span>
                                                    <span class="progress-deadline">
                                                        <fmt:formatDate value="${goal.targetDate}" pattern="MMM dd, yyyy" />
                                                    </span>
                                                </div>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="widget-footer">
                        <a href="${pageContext.request.contextPath}/goals" class="widget-link">
                            View All Goals <span class="arrow">→</span>
                        </a>
                    </div>
                </div>
                

                <!-- Budget Overview Widget -->
                <div class="widget" data-widget-id="budget-overview" data-widget-type="progress">
                    <div class="widget-header">
                        <div class="widget-title">
                            <span class="widget-icon">📉</span>
                            <h3>Budget Overview</h3>
                        </div>
                        <div class="widget-controls">
                            <span class="widget-period">This Month</span>
                        </div>
                    </div>
                    <div class="widget-body">
                        <c:choose>
                            <c:when test="${empty budgetOverview}">
                                <div class="empty-state">
                                    <span class="empty-icon">📉</span>
                                    <p>No budgets set for this month</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <ul class="progress-list">
                                    <c:forEach items="${budgetOverview}" var="budget" varStatus="status">
                                        <c:if test="${status.index < 3}">
                                            <li class="progress-item">
                                                <div class="progress-header">
                                                    <span class="progress-title"><c:out value="${budget.categoryName}" /></span>
                                                    <span class="progress-percent">$<fmt:formatNumber value="${budget.spentAmount}" pattern="#,##0.00" /> / $<fmt:formatNumber value="${budget.budgetLimit}" pattern="#,##0.00" /></span>
                                                </div>
                                                <div class="progress-bar-container">
                                                    <div class="progress-bar-fill" style="width: ${budget.usagePercentage > 100 ? 100 : budget.usagePercentage}%"></div>
                                                </div>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="widget-footer">
                        <a href="${pageContext.request.contextPath}/budgets" class="widget-link">Manage Budgets <span class="arrow">→</span></a>
                    </div>
                </div>

                                <!-- Income vs Expenses Trend Chart -->
                <div class="widget widget-chart widget-wide" data-widget-id="trend-chart" data-widget-type="chart">
                    <div class="widget-header">
                        <div class="widget-title">
                            <span class="widget-icon">📈</span>
                            <h3>Income vs Expenses Trend</h3>
                        </div>
                        <div class="widget-controls">
                            <span class="widget-period">Last 6 Months</span>
                            <button class="widget-menu-btn" title="Widget options">⋮</button>
                        </div>
                    </div>
                    <div class="widget-body">
                        <canvas id="trendChart"></canvas>
                    </div>
                </div>
                
                <!-- Upcoming Events Widget -->
                <div class="widget" data-widget-id="upcoming-events" data-widget-type="list">
                    <div class="widget-header">
                        <div class="widget-title">
                            <span class="widget-icon">📅</span>
                            <h3>Upcoming Events</h3>
                        </div>
                        <div class="widget-controls">
                            <span class="widget-period">Next 3 Days</span>
                            <button class="widget-menu-btn" title="Widget options">⋮</button>
                        </div>
                    </div>
                    <div class="widget-body">
                        <c:choose>
                            <c:when test="${empty upcomingEvents}">
                                <div class="empty-state">
                                    <span class="empty-icon">📅</span>
                                    <p>No upcoming events</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <ul class="item-list">
                                    <c:forEach items="${upcomingEvents}" var="event" varStatus="status">
                                        <c:if test="${status.index < 3}">
                                            <li class="item">
                                                <div class="item-date">
                                                    <span class="date-day"><fmt:formatDate value="${event.eventDate}" pattern="dd" /></span>
                                                    <span class="date-month"><fmt:formatDate value="${event.eventDate}" pattern="MMM" /></span>
                                                </div>
                                                <div class="item-content">
                                                    <span class="item-title"><c:out value="${event.title}" /></span>
                                                    <span class="item-subtitle">
                                                        <fmt:formatDate value="${event.startTime}" pattern="HH:mm" /> - 
                                                        <fmt:formatDate value="${event.endTime}" pattern="HH:mm" />
                                                    </span>
                                                </div>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="widget-footer">
                        <a href="${pageContext.request.contextPath}/schedule" class="widget-link">
                            View Calendar <span class="arrow">→</span>
                        </a>
                    </div>
                </div>
                
                <!-- Recent Notes Widget -->
                <div class="widget" data-widget-id="recent-notes" data-widget-type="list">
                    <div class="widget-header">
                        <div class="widget-title">
                            <span class="widget-icon">📝</span>
                            <h3>Recent Notes</h3>
                        </div>
                        <div class="widget-controls">
                            <button class="widget-menu-btn" title="Widget options">⋮</button>
                        </div>
                    </div>
                    <div class="widget-body">
                        <c:choose>
                            <c:when test="${empty recentNotes}">
                                <div class="empty-state">
                                    <span class="empty-icon">📝</span>
                                    <p>No notes yet</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <ul class="note-list">
                                    <c:forEach items="${recentNotes}" var="note" varStatus="status">
                                        <c:if test="${status.index < 3}">
                                            <li class="note-item">
                                                <h4 class="note-title"><c:out value="${note.title}" /></h4>
                                                <p class="note-preview"><c:out value="${note.preview}" /></p>
                                                <span class="note-date">
                                                    <fmt:formatDate value="${note.updatedAt}" pattern="MMM dd, HH:mm" />
                                                </span>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="widget-footer">
                        <a href="${pageContext.request.contextPath}/notes" class="widget-link">
                            View All Notes <span class="arrow">→</span>
                        </a>
                    </div>
                </div>
                
            </div>
        </main>
    </div>
    
    <!-- Widget Selection Panel (shown in customization mode) -->
    <div class="customization-panel" id="customizationPanel">
        <div class="panel-header">
            <h3>Select widgets to display</h3>
            <p>Check the widgets you want to see on your dashboard. Drag widgets to rearrange them.</p>
        </div>
        <div class="widget-toggles">
            <label class="widget-toggle">
                <input type="checkbox" name="widget" value="financial-summary" checked>
                <span class="toggle-label">
                    <span class="toggle-icon">💰</span>
                    <span>Financial Summary</span>
                </span>
            </label>
            
            <label class="widget-toggle">
                <input type="checkbox" name="widget" value="today-tasks" checked>
                <span class="toggle-label">
                    <span class="toggle-icon">✓</span>
                    <span>Today's Tasks</span>
                </span>
            </label>
            
            <label class="widget-toggle">
                <input type="checkbox" name="widget" value="upcoming-bills" checked>
                <span class="toggle-label">
                    <span class="toggle-icon">🧾</span>
                    <span>Upcoming Bills</span>
                </span>
            </label>
            
            <label class="widget-toggle">
                <input type="checkbox" name="widget" value="expense-chart" checked>
                <span class="toggle-label">
                    <span class="toggle-icon">📊</span>
                    <span>Expense Breakdown</span>
                </span>
            </label>
            
            <label class="widget-toggle">
                <input type="checkbox" name="widget" value="active-goals" checked>
                <span class="toggle-label">
                    <span class="toggle-icon">🎯</span>
                    <span>Active Goals</span>
                </span>
            </label>
            <label class="widget-toggle">
                <input type="checkbox" name="widget" value="budget-overview" checked>
                <span class="toggle-label">
                    <span class="toggle-icon">📉</span>
                    <span>Budget Overview</span>
                </span>
            </label>
            
            <label class="widget-toggle">
                <input type="checkbox" name="widget" value="trend-chart" checked>
                <span class="toggle-label">
                    <span class="toggle-icon">📈</span>
                    <span>Income vs Expenses Trend</span>
                </span>
            </label>
            
            <label class="widget-toggle">
                <input type="checkbox" name="widget" value="upcoming-events" checked>
                <span class="toggle-label">
                    <span class="toggle-icon">📅</span>
                    <span>Upcoming Events</span>
                </span>
            </label>
            
            <label class="widget-toggle">
                <input type="checkbox" name="widget" value="recent-notes" checked>
                <span class="toggle-label">
                    <span class="toggle-icon">📝</span>
                    <span>Recent Notes</span>
                </span>
            </label>
        </div>
    </div>
    <!-- Inject Chart Data from Backend -->
    <script>
        // This script injects real data from the server into the JavaScript
        // It runs before dashboard_v2.js so the data is available when charts initialize

        // Expense Chart Data
        const expenseLabels = ${expenseLabelsJson};
        const expenseAmounts = ${expenseAmountsJson};

        // Trend Chart Data  
        const trendMonths = ${trendMonthsJson};
        const trendIncome = ${trendIncomeJson};
        const trendExpenses = ${trendExpensesJson};

        // Wait for DOMContentLoaded to ensure dashboard_v2.js is loaded
        document.addEventListener('DOMContentLoaded', function() {
            // Set the chart data (this function is defined in dashboard_v2.js)
            if (typeof setChartData === 'function') {
                setChartData(expenseLabels, expenseAmounts, trendMonths, trendIncome, trendExpenses);
            } else {
                console.error('setChartData function not found. Make sure dashboard_v2.js is loaded.');
            }
        });
    </script>
    <script src="${pageContext.request.contextPath}/js/dashboard.js"></script>
</body>
</html>
