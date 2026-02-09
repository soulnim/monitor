<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Transactions - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="../includes/header.jsp" />
    
    <div class="dashboard-container">
        <jsp:include page="../includes/sidebar.jsp" />
        
        <main class="main-content">
            <div class="page-header">
                <h1>Financial Transactions</h1>
                <div class="page-actions">
                    <a href="${pageContext.request.contextPath}/finance/add-transaction" class="btn btn-primary">
                        + Add Transaction
                    </a>
                </div>
            </div>
            
            <!-- Success/Error Messages -->
            <c:if test="${param.success == 'true'}">
                <div class="alert alert-success">
                    Transaction added successfully!
                </div>
            </c:if>
            
            <c:if test="${param.deleted == 'true'}">
                <div class="alert alert-success">
                    Transaction deleted successfully!
                </div>
            </c:if>
            
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error">
                    <c:out value="${errorMessage}" />
                </div>
            </c:if>
            
            <!-- Financial Summary Cards -->
            <div class="summary-cards">
                <div class="summary-card income">
                    <div class="card-icon">💵</div>
                    <div class="card-info">
                        <h3>Total Income</h3>
                        <p class="amount">
                            $<fmt:formatNumber value="${totalIncome}" pattern="#,##0.00" />
                        </p>
                    </div>
                </div>
                
                <div class="summary-card expense">
                    <div class="card-icon">💸</div>
                    <div class="card-info">
                        <h3>Total Expenses</h3>
                        <p class="amount">
                            $<fmt:formatNumber value="${totalExpenses}" pattern="#,##0.00" />
                        </p>
                    </div>
                </div>
                
                <div class="summary-card savings ${netSavings >= 0 ? 'positive' : 'negative'}">
                    <div class="card-icon">💰</div>
                    <div class="card-info">
                        <h3>Net Savings</h3>
                        <p class="amount">
                            $<fmt:formatNumber value="${netSavings}" pattern="#,##0.00" />
                        </p>
                    </div>
                </div>
            </div>
            
            <!-- Filter Section -->
            <div class="filter-section">
                <form method="get" action="${pageContext.request.contextPath}/finance/transactions" class="filter-form">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="month">Month:</label>
                            <select name="month" id="month">
                                <option value="1" ${filterMonth == 1 ? 'selected' : ''}>January</option>
                                <option value="2" ${filterMonth == 2 ? 'selected' : ''}>February</option>
                                <option value="3" ${filterMonth == 3 ? 'selected' : ''}>March</option>
                                <option value="4" ${filterMonth == 4 ? 'selected' : ''}>April</option>
                                <option value="5" ${filterMonth == 5 ? 'selected' : ''}>May</option>
                                <option value="6" ${filterMonth == 6 ? 'selected' : ''}>June</option>
                                <option value="7" ${filterMonth == 7 ? 'selected' : ''}>July</option>
                                <option value="8" ${filterMonth == 8 ? 'selected' : ''}>August</option>
                                <option value="9" ${filterMonth == 9 ? 'selected' : ''}>September</option>
                                <option value="10" ${filterMonth == 10 ? 'selected' : ''}>October</option>
                                <option value="11" ${filterMonth == 11 ? 'selected' : ''}>November</option>
                                <option value="12" ${filterMonth == 12 ? 'selected' : ''}>December</option>
                            </select>
                        </div>
                        
                        <div class="form-group">
                            <label for="year">Year:</label>
                            <select name="year" id="year">
                                <c:forEach begin="2020" end="2030" var="y">
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
            
            <!-- Transactions Table -->
            <div class="content-card">
                <h2>Transaction History</h2>
                
                <c:choose>
                    <c:when test="${empty transactions}">
                        <div class="empty-state">
                            <p>No transactions found for this period</p>
                            <a href="${pageContext.request.contextPath}/finance/add-transaction" class="btn btn-primary">
                                Add Your First Transaction
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Date</th>
                                        <th>Type</th>
                                        <th>Category</th>
                                        <th>Description</th>
                                        <th class="text-right">Amount</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${transactions}" var="transaction">
                                        <tr>
                                            <td>
                                                <fmt:formatDate value="${transaction.transactionDate}" pattern="MMM dd, yyyy" />
                                            </td>
                                            <td>
                                                <span class="badge badge-${transaction.transactionType == 'INCOME' ? 'success' : 'danger'}">
                                                    <c:out value="${transaction.transactionType}" />
                                                </span>
                                            </td>
                                            <td><c:out value="${transaction.categoryName}" /></td>
                                            <td><c:out value="${transaction.description}" /></td>
                                            <td class="text-right ${transaction.transactionType == 'INCOME' ? 'text-success' : 'text-danger'}">
                                                ${transaction.transactionType == 'INCOME' ? '+' : '-'}
                                                $<fmt:formatNumber value="${transaction.amount}" pattern="#,##0.00" />
                                            </td>
                                            <td>
                                                <form method="post" action="${pageContext.request.contextPath}/finance/delete-transaction" 
                                                      style="display: inline;" 
                                                      onsubmit="return confirm('Are you sure you want to delete this transaction?');">
                                                    <input type="hidden" name="transactionId" value="${transaction.transactionId}" />
                                                    <button type="submit" class="btn-icon btn-danger" title="Delete">
                                                        🗑️
                                                    </button>
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
