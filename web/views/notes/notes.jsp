<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Notes - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="../includes/header.jsp" />
    
    <div class="dashboard-container">
        <jsp:include page="../includes/sidebar.jsp" />
        
        <main class="main-content">
            <div class="page-header">
                <h1>Notes</h1>
                <a href="${pageContext.request.contextPath}/notes/add" class="btn btn-primary">+ New Note</a>
            </div>
            
            <c:if test="${param.success}"><div class="alert alert-success">Note created successfully!</div></c:if>
            <c:if test="${param.updated}"><div class="alert alert-success">Note updated successfully!</div></c:if>
            <c:if test="${param.deleted}"><div class="alert alert-success">Note deleted successfully!</div></c:if>
            
            <!-- Search Section -->
            <div class="filter-section">
                <form method="get" action="${pageContext.request.contextPath}/notes" class="search-form">
                    <div class="form-row">
                        <div class="form-group" style="flex: 1;">
                            <input type="text" name="search" placeholder="Search notes..." 
                                   value="${param.search}" class="search-input">
                        </div>
                        <div class="form-group">
                            <button type="submit" class="btn btn-primary">Search</button>
                            <c:if test="${not empty param.search}">
                                <a href="${pageContext.request.contextPath}/notes" class="btn btn-secondary">Clear</a>
                            </c:if>
                        </div>
                    </div>
                </form>
            </div>
            
            <!-- Notes Grid -->
            <div class="notes-container">
                <c:choose>
                    <c:when test="${empty notes}">
                        <div class="empty-state">
                            <p>
                                <c:choose>
                                    <c:when test="${not empty param.search}">
                                        No notes found matching "<c:out value="${param.search}" />"
                                    </c:when>
                                    <c:otherwise>
                                        No notes yet
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="notes-grid">
                            <c:forEach items="${notes}" var="note">
                                <div class="note-card">
                                    <div class="note-card-header">
                                        <h3><c:out value="${note.title}" /></h3>
                                        <div class="note-actions">
                                            <a href="${pageContext.request.contextPath}/notes/edit?id=${note.noteId}" 
                                               class="btn-icon" title="Edit">✏️</a>
                                            <form method="post" action="${pageContext.request.contextPath}/notes/delete" 
                                                  style="display:inline;" onsubmit="return confirm('Delete this note?');">
                                                <input type="hidden" name="noteId" value="${note.noteId}" />
                                                <button type="submit" class="btn-icon btn-danger" title="Delete">🗑️</button>
                                            </form>
                                        </div>
                                    </div>
                                    
                                    <div class="note-card-body">
                                        <c:choose>
                                            <c:when test="${not empty note.content}">
                                                <p class="note-content"><c:out value="${note.preview}" /></p>
                                            </c:when>
                                            <c:otherwise>
                                                <p class="note-content empty">No content</p>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    
                                    <div class="note-card-footer">
                                        <small class="text-muted">
                                            Updated: <fmt:formatDate value="${note.updatedAt}" pattern="MMM dd, yyyy HH:mm" />
                                        </small>
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
