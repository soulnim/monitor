<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Goals - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="../includes/header.jsp" />
    
    <div class="dashboard-container">
        <jsp:include page="../includes/sidebar.jsp" />
        
        <main class="main-content">
            <div class="page-header">
                <h1>Personal Goal Tracking</h1>
                <a href="${pageContext.request.contextPath}/goals/add" class="btn btn-primary">+ Add Goal</a>
            </div>
            
            <!-- Success/Error Messages -->
            <c:if test="${param.success}"><div class="alert alert-success">Goal created successfully!</div></c:if>
            <c:if test="${param.achieved}"><div class="alert alert-success">Goal marked as achieved!</div></c:if>
            <c:if test="${param.deleted}"><div class="alert alert-success">Goal deleted successfully!</div></c:if>
            
            <!-- Goal Summary -->
            <div class="summary-cards">
                <div class="summary-card">
                    <div class="card-icon">🎯</div>
                    <div class="card-info">
                        <h3>Active Goals</h3>
                        <p class="amount">${activeCount}</p>
                    </div>
                </div>
                
                <div class="summary-card income">
                    <div class="card-icon">🏆</div>
                    <div class="card-info">
                        <h3>Achieved</h3>
                        <p class="amount">${achievedCount}</p>
                    </div>
                </div>
            </div>
            
            <!-- Goals List -->
            <div class="content-card">
                <h2>All Goals</h2>
                
                <c:choose>
                    <c:when test="${empty goals}">
                        <div class="empty-state">
                            <p>No goals yet</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="goals-grid">
                            <c:forEach items="${goals}" var="goal">
                                <div class="goal-card">
                                    <div class="goal-card-header">
                                        <h3><c:out value="${goal.title}" /></h3>
                                        <span class="badge badge-${goal.status == 'ACHIEVED' ? 'success' : goal.status == 'ACTIVE' ? 'info' : 'secondary'}">
                                            ${goal.status}
                                        </span>
                                    </div>
                                    
                                    <div class="goal-card-body">
                                        <c:if test="${not empty goal.description}">
                                            <p class="goal-description"><c:out value="${goal.description}" /></p>
                                        </c:if>
                                        
                                        <c:if test="${not empty goal.categoryName}">
                                            <p class="goal-category">
                                                <span class="icon">📂</span> <c:out value="${goal.categoryName}" />
                                            </p>
                                        </c:if>
                                        
                                        <c:if test="${not empty goal.targetDate}">
                                            <p class="goal-date">
                                                <span class="icon">📅</span> Target: <fmt:formatDate value="${goal.targetDate}" pattern="MMM dd, yyyy" />
                                            </p>
                                        </c:if>
                                        
                                        <!-- Progress Bar -->
                                        <div class="progress-container">
                                            <div class="progress-bar">
                                                <div class="progress-fill" style="width: ${goal.completionPercentage}%"></div>
                                            </div>
                                            <span class="progress-text">${goal.completionPercentage}% Complete</span>
                                        </div>
                                    </div>
                                    
                                    <div class="goal-card-footer">
                                        <a href="${pageContext.request.contextPath}/goals/view?id=${goal.goalId}" class="btn-small btn-secondary">
                                            View Details
                                        </a>
                                        
                                        <c:if test="${goal.status == 'ACTIVE'}">
                                            <form method="post" action="${pageContext.request.contextPath}/goals/achieve" style="display:inline;">
                                                <input type="hidden" name="goalId" value="${goal.goalId}" />
                                                <button type="submit" class="btn-small btn-success">Mark Achieved</button>
                                            </form>
                                        </c:if>
                                        
                                        <form method="post" action="${pageContext.request.contextPath}/goals/delete" style="display:inline;" 
                                              onsubmit="return confirm('Delete this goal and all its milestones?');">
                                            <input type="hidden" name="goalId" value="${goal.goalId}" />
                                            <button type="submit" class="btn-icon btn-danger" title="Delete">🗑️</button>
                                        </form>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>
    </div>
</body>
</html>
