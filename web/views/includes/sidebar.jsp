<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<aside class="sidebar">
    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/dashboard"
           class="nav-item ${pageContext.request.requestURI.contains('/dashboard') ? 'active' : ''}">
            <span class="icon">📊</span> Dashboard
        </a>
        <a href="${pageContext.request.contextPath}/finance/transactions"
           class="nav-item ${pageContext.request.requestURI.contains('/finance') ? 'active' : ''}">
            <span class="icon">💰</span> Finances
        </a>
        <a href="${pageContext.request.contextPath}/tasks"
           class="nav-item ${pageContext.request.requestURI.contains('/tasks') ? 'active' : ''}">
            <span class="icon">✓</span> Tasks
        </a>
        <a href="${pageContext.request.contextPath}/goals"
           class="nav-item ${pageContext.request.requestURI.contains('/goals') ? 'active' : ''}">
            <span class="icon">🎯</span> Goals
        </a>
        <a href="${pageContext.request.contextPath}/schedule"
           class="nav-item ${pageContext.request.requestURI.contains('/schedule') ? 'active' : ''}">
            <span class="icon">📅</span> Schedule
        </a>
        <a href="${pageContext.request.contextPath}/budgets"
           class="nav-item ${pageContext.request.requestURI.contains('/budgets') ? 'active' : ''}">
            <span class="icon">📉</span> Budgets
        </a>
        <a href="${pageContext.request.contextPath}/bills"
           class="nav-item ${pageContext.request.requestURI.contains('/bills') ? 'active' : ''}">
            <span class="icon">🧾</span> Bills
        </a>
        <a href="${pageContext.request.contextPath}/notes"
           class="nav-item ${pageContext.request.requestURI.contains('/notes') ? 'active' : ''}">
            <span class="icon">📝</span> Notes
        </a>
    </nav>
</aside>
