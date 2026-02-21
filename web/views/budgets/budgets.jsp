<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Budgets - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="../includes/header.jsp" />
<div class="dashboard-container">
    <jsp:include page="../includes/sidebar.jsp" />
    <main class="main-content">
        <div class="page-header">
            <h1>Budgets</h1>
            <a href="${pageContext.request.contextPath}/budgets/add" class="btn btn-primary">+ Add Budget</a>
        </div>

        <c:if test="${param.saved}"><div class="alert alert-success">Budget saved successfully.</div></c:if>
        <c:if test="${param.deleted}"><div class="alert alert-success">Budget deleted successfully.</div></c:if>

        <div class="content-card">
            <form method="get" action="${pageContext.request.contextPath}/budgets" class="filter-form">
                <div class="form-row">
                    <div class="form-group">
                        <label for="month">Month</label>
                        <select name="month" id="month">
                            <c:forEach begin="1" end="12" var="m">
                                <option value="${m}" ${filterMonth == m ? 'selected' : ''}>${m}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="year">Year</label>
                        <select name="year" id="year">
                            <c:forEach begin="2024" end="2035" var="y">
                                <option value="${y}" ${filterYear == y ? 'selected' : ''}>${y}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <button type="submit" class="btn btn-secondary">Filter</button>
                    </div>
                </div>
            </form>
        </div>

        <div class="content-card">
            <h2>Budget Performance</h2>
            <c:choose>
                <c:when test="${empty budgets}">
                    <div class="empty-state"><p>No budgets configured for this period.</p></div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Category</th>
                                    <th>Limit</th>
                                    <th>Spent</th>
                                    <th>Remaining</th>
                                    <th>Usage</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${budgets}" var="budget">
                                <tr>
                                    <td><c:out value="${budget.categoryName}" /></td>
                                    <td>$<fmt:formatNumber value="${budget.budgetLimit}" pattern="#,##0.00" /></td>
                                    <td>$<fmt:formatNumber value="${budget.spentAmount}" pattern="#,##0.00" /></td>
                                    <td class="${budget.spentAmount > budget.budgetLimit ? 'text-danger' : 'text-success'}">
                                        $<fmt:formatNumber value="${budget.budgetLimit - budget.spentAmount}" pattern="#,##0.00" />
                                    </td>
                                    <td>
                                        <div class="progress-bar">
                                            <div class="progress-fill ${budget.usagePercentage > 100 ? 'progress-danger' : ''}" style="width: ${budget.usagePercentage > 100 ? 100 : budget.usagePercentage}%"></div>
                                        </div>
                                        <small>${budget.usagePercentage}%</small>
                                    </td>
                                    <td>
                                        <form method="post" action="${pageContext.request.contextPath}/budgets/delete" style="display:inline;" onsubmit="return confirm('Delete this budget?');">
                                            <input type="hidden" name="budgetId" value="${budget.budgetId}" />
                                            <button class="btn-icon btn-danger" type="submit">🗑️</button>
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
