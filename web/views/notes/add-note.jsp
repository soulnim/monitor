<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Note - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="../includes/header.jsp" />
    
    <div class="dashboard-container">
        <jsp:include page="../includes/sidebar.jsp" />
        
        <main class="main-content">
            <div class="page-header">
                <h1>Create New Note</h1>
                <a href="${pageContext.request.contextPath}/notes" class="btn btn-secondary">← Back to Notes</a>
            </div>
            
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error"><c:out value="${errorMessage}" /></div>
            </c:if>
            
            <div class="form-card-wide">
                <form method="post" action="${pageContext.request.contextPath}/notes/add">
                    
                    <div class="form-group">
                        <label for="title">Note Title *</label>
                        <input type="text" name="title" id="title" required maxlength="200" 
                               placeholder="Enter note title" autofocus>
                    </div>
                    
                    <div class="form-group">
                        <label for="content">Content</label>
                        <textarea name="content" id="content" rows="15" 
                                  placeholder="Write your note here..."></textarea>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Save Note</button>
                        <a href="${pageContext.request.contextPath}/notes" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </main>
    </div>
</body>
</html>
