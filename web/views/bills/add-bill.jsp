<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Bill - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="../includes/header.jsp" />
    
    <div class="dashboard-container">
        <jsp:include page="../includes/sidebar.jsp" />
        
        <main class="main-content">
            <div class="page-header">
                <h1>Add New Bill</h1>
                <a href="${pageContext.request.contextPath}/bills" class="btn btn-secondary">← Back to Bills</a>
            </div>
            
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error"><c:out value="${errorMessage}" /></div>
            </c:if>
            
            <div class="form-card">
                <form method="post" action="${pageContext.request.contextPath}/bills/add">
                    
                    <div class="form-group">
                        <label for="billName">Bill Name *</label>
                        <input type="text" name="billName" id="billName" required maxlength="100" placeholder="e.g., Electric Bill">
                    </div>
                    
                    <div class="form-group">
                        <label for="amount">Amount *</label>
                        <input type="number" name="amount" id="amount" step="0.01" min="0.01" required placeholder="0.00">
                    </div>
                    
                    <div class="form-group">
                        <label for="dueDate">Due Date *</label>
                        <input type="date" name="dueDate" id="dueDate" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="category">Category</label>
                        <select name="category" id="category">
                            <option value="Utilities">Utilities</option>
                            <option value="Rent">Rent</option>
                            <option value="Insurance">Insurance</option>
                            <option value="Subscription">Subscription</option>
                            <option value="Loan">Loan</option>
                            <option value="Credit Card">Credit Card</option>
                            <option value="Other">Other</option>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <div class="form-check">
                            <input type="checkbox" name="isRecurring" id="isRecurring" onchange="toggleRecurring()">
                            <label for="isRecurring">Recurring Bill</label>
                        </div>
                    </div>
                    
                    <div class="form-group" id="recurringGroup" style="display:none;">
                        <label for="recurringFrequency">Frequency</label>
                        <select name="recurringFrequency" id="recurringFrequency">
                            <option value="MONTHLY">Monthly</option>
                            <option value="YEARLY">Yearly</option>
                        </select>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Add Bill</button>
                        <a href="${pageContext.request.contextPath}/bills" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </main>
    </div>
    
    <script>
        function toggleRecurring() {
            const isChecked = document.getElementById('isRecurring').checked;
            document.getElementById('recurringGroup').style.display = isChecked ? 'block' : 'none';
        }
    </script>
</body>
</html>
