<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Task - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="../includes/header.jsp" />
    
    <div class="dashboard-container">
        <jsp:include page="../includes/sidebar.jsp" />
        
        <main class="main-content">
            <div class="page-header">
                <h1>Add New Task</h1>
                <a href="${pageContext.request.contextPath}/tasks" class="btn btn-secondary">← Back to Tasks</a>
            </div>
            
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error"><c:out value="${errorMessage}" /></div>
            </c:if>
            
            <div class="form-card">
                <form method="post" action="${pageContext.request.contextPath}/tasks/add">
                    
                    <div class="form-group">
                        <label for="title">Task Title *</label>
                        <input type="text" name="title" id="title" required maxlength="200" placeholder="Enter task title">
                    </div>
                    
                    <div class="form-group">
                        <label for="description">Description</label>
                        <textarea name="description" id="description" rows="4" placeholder="Enter task description"></textarea>
                    </div>
                    
                    <div class="form-group">
                        <label for="dueDate">Due Date</label>
                        <input type="date" name="dueDate" id="dueDate">
                    </div>
                    
                    <div class="form-group">
                        <label for="priority">Priority *</label>
                        <select name="priority" id="priority" required>
                            <option value="MEDIUM">Medium</option>
                            <option value="HIGH">High</option>
                            <option value="LOW">Low</option>
                        </select>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Create Task</button>
                        <a href="${pageContext.request.contextPath}/tasks" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </main>
    </div>
</body>
</html>
