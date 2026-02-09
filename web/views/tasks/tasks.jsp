<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tasks - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="../includes/header.jsp" />
    
    <div class="dashboard-container">
        <jsp:include page="../includes/sidebar.jsp" />
        
        <main class="main-content">
            <div class="page-header">
                <h1>Task Management</h1>
                <a href="${pageContext.request.contextPath}/tasks/add" class="btn btn-primary">+ Add Task</a>
            </div>
            
            <c:if test="${param.success}"><div class="alert alert-success">Task added successfully!</div></c:if>
            <c:if test="${param.completed}"><div class="alert alert-success">Task marked as completed!</div></c:if>
            <c:if test="${param.deleted}"><div class="alert alert-success">Task deleted successfully!</div></c:if>
            
            <!-- Task Summary Cards -->
            <div class="summary-cards">
                <div class="summary-card">
                    <div class="card-icon">📋</div>
                    <div class="card-info">
                        <h3>Pending</h3>
                        <p class="amount">${pendingCount}</p>
                    </div>
                </div>
                <div class="summary-card">
                    <div class="card-icon">⚙️</div>
                    <div class="card-info">
                        <h3>In Progress</h3>
                        <p class="amount">${inProgressCount}</p>
                    </div>
                </div>
                <div class="summary-card">
                    <div class="card-icon">✅</div>
                    <div class="card-info">
                        <h3>Completed</h3>
                        <p class="amount">${completedCount}</p>
                    </div>
                </div>
                <div class="summary-card ${overdueCount > 0 ? 'expense' : ''}">
                    <div class="card-icon">⚠️</div>
                    <div class="card-info">
                        <h3>Overdue</h3>
                        <p class="amount">${overdueCount}</p>
                    </div>
                </div>
            </div>
            
            <!-- Filter Section -->
            <div class="filter-section">
                <form method="get" class="filter-form">
                    <div class="form-row">
                        <div class="form-group">
                            <label>Filter by Status:</label>
                            <select name="status" onchange="this.form.submit()">
                                <option value="">All</option>
                                <option value="PENDING">Pending</option>
                                <option value="IN_PROGRESS">In Progress</option>
                                <option value="COMPLETED">Completed</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Filter by Priority:</label>
                            <select name="priority" onchange="this.form.submit()">
                                <option value="">All</option>
                                <option value="HIGH">High</option>
                                <option value="MEDIUM">Medium</option>
                                <option value="LOW">Low</option>
                            </select>
                        </div>
                    </div>
                </form>
            </div>
            
            <!-- Tasks Table -->
            <div class="content-card">
                <h2>All Tasks</h2>
                <c:choose>
                    <c:when test="${empty tasks}">
                        <div class="empty-state">
                            <p>No tasks found</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Task</th>
                                        <th>Due Date</th>
                                        <th>Priority</th>
                                        <th>Status</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${tasks}" var="task">
                                        <tr>
                                            <td>
                                                <strong><c:out value="${task.title}" /></strong>
                                                <c:if test="${not empty task.description}">
                                                    <br><small class="text-muted"><c:out value="${task.description}" /></small>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:if test="${not empty task.dueDate}">
                                                    <fmt:formatDate value="${task.dueDate}" pattern="MMM dd, yyyy" />
                                                </c:if>
                                            </td>
                                            <td>
                                                <span class="badge badge-${task.priority == 'HIGH' ? 'danger' : task.priority == 'MEDIUM' ? 'warning' : 'info'}">
                                                    ${task.priority}
                                                </span>
                                            </td>
                                            <td>
                                                <span class="badge badge-${task.status == 'COMPLETED' ? 'success' : 'secondary'}">
                                                    ${task.status}
                                                </span>
                                            </td>
                                            <td>
                                                <c:if test="${task.status != 'COMPLETED'}">
                                                    <form method="post" action="${pageContext.request.contextPath}/tasks/complete" style="display:inline;">
                                                        <input type="hidden" name="taskId" value="${task.taskId}" />
                                                        <button type="submit" class="btn-icon" title="Mark as Complete">✓</button>
                                                    </form>
                                                </c:if>
                                                <form method="post" action="${pageContext.request.contextPath}/tasks/delete" style="display:inline;" 
                                                      onsubmit="return confirm('Delete this task?');">
                                                    <input type="hidden" name="taskId" value="${task.taskId}" />
                                                    <button type="submit" class="btn-icon btn-danger" title="Delete">🗑️</button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>
    </div>
</body>
</html>
