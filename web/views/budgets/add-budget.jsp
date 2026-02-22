<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Budget - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="../includes/header.jsp" />
<div class="dashboard-container">
    <jsp:include page="../includes/sidebar.jsp" />
    <main class="main-content">
        <div class="page-header"><h1>Add / Update Budget</h1></div>
        <div class="content-card">
            <form method="post" action="${pageContext.request.contextPath}/budgets/add">
                <div class="form-group">
                    <label for="categoryId">Expense Category</label>
                    <select name="categoryId" id="categoryId" required>
                        <option value="">Select Category</option>
                        <c:forEach items="${categories}" var="category">
                            <option value="${category.categoryId}"><c:out value="${category.categoryName}" /></option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="budgetLimit">Budget Limit</label>
                    <input type="number" name="budgetLimit" id="budgetLimit" min="0.01" step="0.01" required placeholder="0.00"/>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label for="budgetMonth">Month</label>
                        <select name="budgetMonth" id="budgetMonth" required>
                            <c:forEach begin="1" end="12" var="m">
                                <option value="${m}" ${currentMonth == m ? 'selected' : ''}>${m}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="budgetYear">Year</label>
                        <select name="budgetYear" id="budgetYear" required>
                            <c:forEach begin="2024" end="2035" var="y">
                                <option value="${y}" ${currentYear == y ? 'selected' : ''}>${y}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary">Save Budget</button>
                <a href="${pageContext.request.contextPath}/budgets" class="btn btn-secondary">Cancel</a>
            </form>
        </div>
    </main>
</div>
</body>
</html>
