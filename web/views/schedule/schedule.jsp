<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Schedule - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="../includes/header.jsp" />
    
    <div class="dashboard-container">
        <jsp:include page="../includes/sidebar.jsp" />
        
        <main class="main-content">
            <div class="page-header">
                <h1>Schedule & Events</h1>
                <a href="${pageContext.request.contextPath}/schedule/add" class="btn btn-primary">+ Add Event</a>
            </div>
            
            <c:if test="${param.success}"><div class="alert alert-success">Event added successfully!</div></c:if>
            <c:if test="${param.deleted}"><div class="alert alert-success">Event deleted successfully!</div></c:if>
            
            <!-- Upcoming Events Card -->
            <c:if test="${not empty upcomingEvents}">
                <div class="content-card">
                    <h2>Upcoming Events (Next 7 Days)</h2>
                    <div class="event-list-upcoming">
                        <c:forEach items="${upcomingEvents}" var="event">
                            <div class="event-item-compact">
                                <div class="event-date-badge">
                                    <fmt:formatDate value="${event.eventDate}" pattern="MMM" />
                                    <span class="date-number"><fmt:formatDate value="${event.eventDate}" pattern="dd" /></span>
                                </div>
                                <div class="event-info">
                                    <h4><c:out value="${event.title}" /></h4>
                                    <c:if test="${not empty event.startTime}">
                                        <p class="event-time">
                                            <span class="icon">🕐</span> 
                                            <fmt:formatDate value="${event.startTime}" pattern="HH:mm" type="time" />
                                            <c:if test="${not empty event.endTime}">
                                                - <fmt:formatDate value="${event.endTime}" pattern="HH:mm" type="time" />
                                            </c:if>
                                        </p>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
            
            <!-- All Events -->
            <div class="content-card">
                <h2>All Events</h2>
                
                <c:choose>
                    <c:when test="${empty events}">
                        <div class="empty-state">
                            <p>No events scheduled</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Date</th>
                                        <th>Event</th>
                                        <th>Time</th>
                                        <th>Type</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${events}" var="event">
                                        <tr>
                                            <td>
                                                <fmt:formatDate value="${event.eventDate}" pattern="MMM dd, yyyy" />
                                            </td>
                                            <td>
                                                <strong><c:out value="${event.title}" /></strong>
                                                <c:if test="${not empty event.description}">
                                                    <br><small class="text-muted"><c:out value="${event.description}" /></small>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:if test="${not empty event.startTime}">
                                                    <fmt:formatDate value="${event.startTime}" pattern="HH:mm" type="time" />
                                                    <c:if test="${not empty event.endTime}">
                                                        - <fmt:formatDate value="${event.endTime}" pattern="HH:mm" type="time" />
                                                    </c:if>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:if test="${not empty event.eventType}">
                                                    <span class="badge badge-info"><c:out value="${event.eventType}" /></span>
                                                </c:if>
                                                <c:if test="${event.recurring}">
                                                    <span class="badge badge-secondary">🔄 Recurring</span>
                                                </c:if>
                                            </td>
                                            <td>
                                                <form method="post" action="${pageContext.request.contextPath}/schedule/delete" 
                                                      style="display:inline;" onsubmit="return confirm('Delete this event?');">
                                                    <input type="hidden" name="eventId" value="${event.eventId}" />
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
