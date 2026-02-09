<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bills - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="../includes/header.jsp" />
    
    <div class="dashboard-container">
        <jsp:include page="../includes/sidebar.jsp" />
        
        <main class="main-content">
            <div class="page-header">
                <h1>Bill Management</h1>
                <a href="${pageContext.request.contextPath}/bills/add" class="btn btn-primary">+ Add Bill</a>
            </div>
            
            <c:if test="${param.success}"><div class="alert alert-success">Bill added successfully!</div></c:if>
            <c:if test="${param.paid}"><div class="alert alert-success">Bill marked as paid!</div></c:if>
            <c:if test="${param.deleted}"><div class="alert alert-success">Bill deleted successfully!</div></c:if>
            
            <!-- Bills Summary -->
            <div class="summary-cards">
                <div class="summary-card">
                    <div class="card-icon">🧾</div>
                    <div class="card-info">
                        <h3>Upcoming (7 days)</h3>
                        <p class="amount">${upcomingBills.size()}</p>
                    </div>
                </div>
                <div class="summary-card expense">
                    <div class="card-icon">⚠️</div>
                    <div class="card-info">
                        <h3>Overdue</h3>
                        <p class="amount">${overdueBills.size()}</p>
                    </div>
                </div>
                <div class="summary-card">
                    <div class="card-icon">💵</div>
                    <div class="card-info">
                        <h3>Total Unpaid</h3>
                        <p class="amount">$<fmt:formatNumber value="${totalUnpaid}" pattern="#,##0.00" /></p>
                    </div>
                </div>
            </div>
            
            <!-- Upcoming Bills -->
            <c:if test="${not empty upcomingBills}">
                <div class="content-card">
                    <h2>Upcoming Bills (Next 7 Days)</h2>
                    <div class="table-responsive">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Bill Name</th>
                                    <th>Category</th>
                                    <th>Amount</th>
                                    <th>Due Date</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${upcomingBills}" var="bill">
                                    <tr>
                                        <td><c:out value="${bill.billName}" /></td>
                                        <td><c:out value="${bill.category}" /></td>
                                        <td class="text-danger">$<fmt:formatNumber value="${bill.amount}" pattern="#,##0.00" /></td>
                                        <td><fmt:formatDate value="${bill.dueDate}" pattern="MMM dd, yyyy" /></td>
                                        <td>
                                            <form method="post" action="${pageContext.request.contextPath}/bills/pay" style="display:inline;">
                                                <input type="hidden" name="billId" value="${bill.billId}" />
                                                <button type="submit" class="btn-small btn-success">Mark Paid</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:if>
            
            <!-- All Bills -->
            <div class="content-card">
                <h2>All Bills</h2>
                <c:choose>
                    <c:when test="${empty bills}">
                        <div class="empty-state">
                            <p>No bills found</p>
                            <a href="${pageContext.request.contextPath}/bills/add" class="btn btn-primary">Add Your First Bill</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Bill Name</th>
                                        <th>Category</th>
                                        <th>Amount</th>
                                        <th>Due Date</th>
                                        <th>Status</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${bills}" var="bill">
                                        <tr>
                                            <td><c:out value="${bill.billName}" /></td>
                                            <td><c:out value="${bill.category}" /></td>
                                            <td>$<fmt:formatNumber value="${bill.amount}" pattern="#,##0.00" /></td>
                                            <td><fmt:formatDate value="${bill.dueDate}" pattern="MMM dd, yyyy" /></td>
                                            <td>
                                                <span class="badge badge-${bill.status == 'PAID' ? 'success' : bill.status == 'OVERDUE' ? 'danger' : 'warning'}">
                                                    ${bill.status}
                                                </span>
                                            </td>
                                            <td>
                                                <c:if test="${bill.status != 'PAID'}">
                                                    <form method="post" action="${pageContext.request.contextPath}/bills/pay" style="display:inline;">
                                                        <input type="hidden" name="billId" value="${bill.billId}" />
                                                        <button type="submit" class="btn-icon" title="Mark Paid">💰</button>
                                                    </form>
                                                </c:if>
                                                <form method="post" action="${pageContext.request.contextPath}/bills/delete" style="display:inline;" 
                                                      onsubmit="return confirm('Delete this bill?');">
                                                    <input type="hidden" name="billId" value="${bill.billId}" />
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
