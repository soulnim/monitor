<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Goal - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="../includes/header.jsp" />
    
    <div class="dashboard-container">
        <jsp:include page="../includes/sidebar.jsp" />
        
        <main class="main-content">
            <div class="page-header">
                <h1>Add New Goal</h1>
                <a href="${pageContext.request.contextPath}/goals" class="btn btn-secondary">← Back to Goals</a>
            </div>
            
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error"><c:out value="${errorMessage}" /></div>
            </c:if>
            
            <div class="form-card">
                <form method="post" action="${pageContext.request.contextPath}/goals/add">
                    
                    <div class="form-group">
                        <label for="title">Goal Title *</label>
                        <input type="text" name="title" id="title" required maxlength="200" 
                               placeholder="e.g., Complete 30 books this year">
                    </div>
                    
                    <div class="form-group">
                        <label for="description">Description</label>
                        <textarea name="description" id="description" rows="4" 
                                  placeholder="Describe your goal and why it's important"></textarea>
                    </div>
                    
                    <div class="form-group">
                        <label for="categoryId">Category</label>
                        <select name="categoryId" id="categoryId">
                            <option value="">Select Category (Optional)</option>
                            <c:forEach items="${categories}" var="cat">
                                <option value="${cat.categoryId}"><c:out value="${cat.categoryName}" /></option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label for="targetDate">Target Date</label>
                        <input type="date" name="targetDate" id="targetDate">
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Create Goal</button>
                        <a href="${pageContext.request.contextPath}/goals" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </main>
    </div>
</body>
</html>
