<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Note - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="../includes/header.jsp" />
    
    <div class="dashboard-container">
        <jsp:include page="../includes/sidebar.jsp" />
        
        <main class="main-content">
            <div class="page-header">
                <h1>Edit Note</h1>
                <a href="${pageContext.request.contextPath}/notes" class="btn btn-secondary">← Back to Notes</a>
            </div>
            
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error"><c:out value="${errorMessage}" /></div>
            </c:if>
            
            <div class="form-card-wide">
                <form method="post" action="${pageContext.request.contextPath}/notes/edit">
                    <input type="hidden" name="noteId" value="${note.noteId}" />
                    
                    <div class="form-group">
                        <label for="title">Note Title *</label>
                        <input type="text" name="title" id="title" required maxlength="200" 
                               value="<c:out value="${note.title}" />" autofocus>
                    </div>
                    
                    <div class="form-group">
                        <label for="content">Content</label>
                        <textarea name="content" id="content" rows="15"><c:out value="${note.content}" /></textarea>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Update Note</button>
                        <a href="${pageContext.request.contextPath}/notes" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </main>
    </div>
</body>
</html>
