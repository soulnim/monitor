<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Transaction - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="../includes/header.jsp" />
    
    <div class="dashboard-container">
        <jsp:include page="../includes/sidebar.jsp" />
        
        <main class="main-content">
            <div class="page-header">
                <h1>Add New Transaction</h1>
                <a href="${pageContext.request.contextPath}/finance/transactions" class="btn btn-secondary">
                    ← Back to Transactions
                </a>
            </div>
            
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error">
                    <c:out value="${errorMessage}" />
                </div>
            </c:if>
            
            <div class="form-card">
                <form method="post" action="${pageContext.request.contextPath}/finance/add-transaction" id="transactionForm">
                    
                    <!-- Transaction Type -->
                    <div class="form-group">
                        <label for="transactionType">Transaction Type *</label>
                        <select name="transactionType" id="transactionType" required onchange="updateCategories()">
                            <option value="">Select Type</option>
                            <option value="INCOME">Income</option>
                            <option value="EXPENSE">Expense</option>
                        </select>
                    </div>
                    
                    <!-- Category -->
                    <div class="form-group">
                        <label for="categoryId">Category *</label>
                        <select name="categoryId" id="categoryId" required>
                            <option value="">Select transaction type first</option>
                        </select>
                    </div>
                    
                    <!-- Amount -->
                    <div class="form-group">
                        <label for="amount">Amount *</label>
                        <input type="number" 
                               name="amount" 
                               id="amount" 
                               step="0.01" 
                               min="0.01"
                               placeholder="0.00" 
                               required>
                    </div>
                    
                    <!-- Transaction Date -->
                    <div class="form-group">
                        <label for="transactionDate">Date *</label>
                        <input type="date" 
                               name="transactionDate" 
                               id="transactionDate" 
                               required
                               value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>">
                    </div>
                    
                    <!-- Description -->
                    <div class="form-group">
                        <label for="description">Description</label>
                        <textarea name="description" 
                                  id="description" 
                                  rows="3" 
                                  placeholder="Enter transaction details"></textarea>
                    </div>
                    
                    <!-- Recurring Transaction -->
                    <div class="form-group">
                        <div class="form-check">
                            <input type="checkbox" 
                                   name="isRecurring" 
                                   id="isRecurring" 
                                   onchange="toggleRecurring()">
                            <label for="isRecurring">Recurring Transaction</label>
                        </div>
                    </div>
                    
                    <!-- Recurring Frequency (hidden by default) -->
                    <div class="form-group" id="recurringFrequencyGroup" style="display: none;">
                        <label for="recurringFrequency">Frequency</label>
                        <select name="recurringFrequency" id="recurringFrequency">
                            <option value="">Select Frequency</option>
                            <option value="DAILY">Daily</option>
                            <option value="WEEKLY">Weekly</option>
                            <option value="MONTHLY">Monthly</option>
                            <option value="YEARLY">Yearly</option>
                        </select>
                    </div>
                    
                    <!-- Submit Buttons -->
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Save Transaction</button>
                        <a href="${pageContext.request.contextPath}/finance/transactions" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </main>
    </div>
    
    <script>
        // Store categories from JSP
        const incomeCategories = [
            <c:forEach items="${incomeCategories}" var="cat" varStatus="status">
                {id: ${cat.categoryId}, name: '<c:out value="${cat.categoryName}" />'}${!status.last ? ',' : ''}
            </c:forEach>
        ];
        
        const expenseCategories = [
            <c:forEach items="${expenseCategories}" var="cat" varStatus="status">
                {id: ${cat.categoryId}, name: '<c:out value="${cat.categoryName}" />'}${!status.last ? ',' : ''}
            </c:forEach>
        ];
        
        function updateCategories() {
            const type = document.getElementById('transactionType').value;
            const categorySelect = document.getElementById('categoryId');
            
            // Clear existing options
            categorySelect.innerHTML = '<option value="">Select Category</option>';
            
            // Add categories based on type
            let categories = type === 'INCOME' ? incomeCategories : expenseCategories;
            
            categories.forEach(cat => {
                const option = document.createElement('option');
                option.value = cat.id;
                option.textContent = cat.name;
                categorySelect.appendChild(option);
            });
        }
        
        function toggleRecurring() {
            const isRecurring = document.getElementById('isRecurring').checked;
            const frequencyGroup = document.getElementById('recurringFrequencyGroup');
            frequencyGroup.style.display = isRecurring ? 'block' : 'none';
        }
    </script>
</body>
</html>
