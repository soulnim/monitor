<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Goal Details - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="../includes/header.jsp" />
    
    <div class="dashboard-container">
        <jsp:include page="../includes/sidebar.jsp" />
        
        <main class="main-content">
            <div class="page-header">
                <h1>Goal Details</h1>
                <a href="${pageContext.request.contextPath}/goals" class="btn btn-secondary">← Back to Goals</a>
            </div>
            
            <c:if test="${param.milestone == 'added'}">
                <div class="alert alert-success">Milestone added successfully!</div>
            </c:if>
            <c:if test="${param.milestone == 'completed'}">
                <div class="alert alert-success">Milestone marked as complete!</div>
            </c:if>
            
            <!-- Goal Information Card -->
            <div class="content-card">
                <div class="goal-detail-header">
                    <div>
                        <h2><c:out value="${goal.title}" /></h2>
                        <span class="badge badge-${goal.status == 'ACHIEVED' ? 'success' : 'info'}">${goal.status}</span>
                    </div>
                </div>
                
                <c:if test="${not empty goal.description}">
                    <p class="goal-description"><c:out value="${goal.description}" /></p>
                </c:if>
                
                <div class="goal-meta">
                    <c:if test="${not empty goal.categoryName}">
                        <p><strong>Category:</strong> <c:out value="${goal.categoryName}" /></p>
                    </c:if>
                    <c:if test="${not empty goal.targetDate}">
                        <p><strong>Target Date:</strong> <fmt:formatDate value="${goal.targetDate}" pattern="MMMM dd, yyyy" /></p>
                    </c:if>
                    <p><strong>Created:</strong> <fmt:formatDate value="${goal.createdAt}" pattern="MMM dd, yyyy" /></p>
                </div>
                
                <!-- Overall Progress -->
                <div class="progress-section">
                    <h3>Overall Progress</h3>
                    <div class="progress-container">
                        <div class="progress-bar large">
                            <div class="progress-fill" style="width: ${goal.completionPercentage}%"></div>
                        </div>
                        <span class="progress-text large">${goal.completionPercentage}% Complete</span>
                    </div>
                </div>
            </div>
            
            <!-- Milestones Section -->
            <div class="content-card">
                <div class="card-header-flex">
                    <h2>Milestones</h2>
                    <button onclick="toggleMilestoneForm()" class="btn btn-primary">+ Add Milestone</button>
                </div>
                
                <!-- Add Milestone Form (Hidden by default) -->
                <div id="milestoneForm" class="milestone-form" style="display:none;">
                    <form method="post" action="${pageContext.request.contextPath}/goals/add-milestone">
                        <input type="hidden" name="goalId" value="${goal.goalId}" />
                        
                        <div class="form-row">
                            <div class="form-group">
                                <label for="title">Milestone Title *</label>
                                <input type="text" name="title" id="title" required maxlength="200" 
                                       placeholder="e.g., Save first $2,500">
                            </div>
                            
                            <div class="form-group">
                                <label for="targetDate">Target Date</label>
                                <input type="date" name="targetDate" id="targetDate">
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label for="description">Description</label>
                            <input type="text" name="description" id="description" maxlength="255" 
                                   placeholder="Optional details">
                        </div>
                        
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">Add Milestone</button>
                            <button type="button" onclick="toggleMilestoneForm()" class="btn btn-secondary">Cancel</button>
                        </div>
                    </form>
                </div>
                
                <!-- Milestones List -->
                <c:choose>
                    <c:when test="${empty milestones}">
                        <p class="empty-state">No milestones yet. Add milestones to track your progress!</p>
                    </c:when>
                    <c:otherwise>
                        <div class="milestones-list">
                            <c:forEach items="${milestones}" var="milestone">
                                <div class="milestone-item ${milestone.completed ? 'completed' : ''}">
                                    <div class="milestone-checkbox">
                                        <c:choose>
                                            <c:when test="${milestone.completed}">
                                                <span class="checkbox-checked">✓</span>
                                            </c:when>
                                            <c:otherwise>
                                                <form method="post" action="${pageContext.request.contextPath}/goals/complete-milestone" style="display:inline;">
                                                    <input type="hidden" name="milestoneId" value="${milestone.milestoneId}" />
                                                    <input type="hidden" name="goalId" value="${goal.goalId}" />
                                                    <button type="submit" class="checkbox-unchecked" title="Mark as complete">○</button>
                                                </form>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    
                                    <div class="milestone-content">
                                        <h4><c:out value="${milestone.title}" /></h4>
                                        <c:if test="${not empty milestone.description}">
                                            <p class="milestone-desc"><c:out value="${milestone.description}" /></p>
                                        </c:if>
                                        <c:if test="${not empty milestone.targetDate}">
                                            <p class="milestone-date">
                                                Target: <fmt:formatDate value="${milestone.targetDate}" pattern="MMM dd, yyyy" />
                                            </p>
                                        </c:if>
                                        <c:if test="${milestone.completed && not empty milestone.completedAt}">
                                            <p class="milestone-completed">
                                                Completed: <fmt:formatDate value="${milestone.completedAt}" pattern="MMM dd, yyyy" />
                                            </p>
                                        </c:if>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>
    </div>
    
    <script>
        function toggleMilestoneForm() {
            var form = document.getElementById('milestoneForm');
            if (form.style.display === 'none') {
                form.style.display = 'block';
            } else {
                form.style.display = 'none';
            }
        }
    </script>
</body>
</html>
