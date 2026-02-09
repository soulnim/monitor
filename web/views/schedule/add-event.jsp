<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Event - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="../includes/header.jsp" />
    
    <div class="dashboard-container">
        <jsp:include page="../includes/sidebar.jsp" />
        
        <main class="main-content">
            <div class="page-header">
                <h1>Add New Event</h1>
                <a href="${pageContext.request.contextPath}/schedule" class="btn btn-secondary">← Back to Schedule</a>
            </div>
            
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error"><c:out value="${errorMessage}" /></div>
            </c:if>
            
            <div class="form-card">
                <form method="post" action="${pageContext.request.contextPath}/schedule/add">
                    
                    <div class="form-group">
                        <label for="title">Event Title *</label>
                        <input type="text" name="title" id="title" required maxlength="200" 
                               placeholder="e.g., Team Meeting">
                    </div>
                    
                    <div class="form-group">
                        <label for="description">Description</label>
                        <textarea name="description" id="description" rows="3" 
                                  placeholder="Event details"></textarea>
                    </div>
                    
                    <div class="form-group">
                        <label for="eventDate">Date *</label>
                        <input type="date" name="eventDate" id="eventDate" required>
                    </div>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label for="startTime">Start Time</label>
                            <input type="time" name="startTime" id="startTime">
                        </div>
                        
                        <div class="form-group">
                            <label for="endTime">End Time</label>
                            <input type="time" name="endTime" id="endTime">
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="eventType">Event Type</label>
                        <select name="eventType" id="eventType">
                            <option value="">Select Type (Optional)</option>
                            <option value="Meeting">Meeting</option>
                            <option value="Appointment">Appointment</option>
                            <option value="Personal">Personal</option>
                            <option value="Work">Work</option>
                            <option value="Birthday">Birthday</option>
                            <option value="Anniversary">Anniversary</option>
                            <option value="Other">Other</option>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <div class="form-check">
                            <input type="checkbox" name="isRecurring" id="isRecurring" onchange="toggleRecurring()">
                            <label for="isRecurring">Recurring Event</label>
                        </div>
                    </div>
                    
                    <div class="form-group" id="recurringGroup" style="display:none;">
                        <label for="recurringFrequency">Frequency</label>
                        <select name="recurringFrequency" id="recurringFrequency">
                            <option value="DAILY">Daily</option>
                            <option value="WEEKLY">Weekly</option>
                            <option value="MONTHLY">Monthly</option>
                        </select>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Add Event</button>
                        <a href="${pageContext.request.contextPath}/schedule" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </main>
    </div>
    
    <script>
        function toggleRecurring() {
            var isChecked = document.getElementById('isRecurring').checked;
            document.getElementById('recurringGroup').style.display = isChecked ? 'block' : 'none';
        }
    </script>
</body>
</html>
