package com.monitor.controller;

import com.monitor.dao.TransactionDAO;
import com.monitor.dao.TransactionDAOImpl;
import com.monitor.dao.CategoryDAO;
import com.monitor.dao.CategoryDAOImpl;
import com.monitor.model.Transaction;
import com.monitor.model.Category;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet for listing and managing transactions
 */
@WebServlet(name = "TransactionController", urlPatterns = {"/finance/transactions"})
public class TransactionController extends HttpServlet {
    
    private TransactionDAO transactionDAO;
    private CategoryDAO categoryDAO;
    
    @Override
    public void init() throws ServletException {
        transactionDAO = new TransactionDAOImpl();
        categoryDAO = new CategoryDAOImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        int userId = (Integer) session.getAttribute("userId");
        
        try {
            // Get current month and year
            Calendar cal = Calendar.getInstance();
            int currentMonth = cal.get(Calendar.MONTH) + 1; // Calendar.MONTH is 0-based
            int currentYear = cal.get(Calendar.YEAR);
            
            // Get filter parameters (if any)
            String monthParam = request.getParameter("month");
            String yearParam = request.getParameter("year");
            String typeFilter = request.getParameter("type");
            
            int filterMonth = (monthParam != null) ? Integer.parseInt(monthParam) : currentMonth;
            int filterYear = (yearParam != null) ? Integer.parseInt(yearParam) : currentYear;
            
            // Get transactions
            List<Transaction> transactions;
            if (typeFilter != null && !typeFilter.isEmpty()) {
                transactions = transactionDAO.getTransactionsByType(userId, typeFilter);
            } else {
                transactions = transactionDAO.getTransactionsByMonth(userId, filterMonth, filterYear);
            }
            
            // Get financial summary
            BigDecimal totalIncome = transactionDAO.getTotalIncome(userId, filterMonth, filterYear);
            BigDecimal totalExpenses = transactionDAO.getTotalExpenses(userId, filterMonth, filterYear);
            BigDecimal netSavings = totalIncome.subtract(totalExpenses);
            
            // Get categories for filter dropdown
            List<Category> incomeCategories = categoryDAO.getCategoriesByType(userId, "INCOME");
            List<Category> expenseCategories = categoryDAO.getCategoriesByType(userId, "EXPENSE");
            
            // Set attributes
            request.setAttribute("transactions", transactions);
            request.setAttribute("totalIncome", totalIncome);
            request.setAttribute("totalExpenses", totalExpenses);
            request.setAttribute("netSavings", netSavings);
            request.setAttribute("incomeCategories", incomeCategories);
            request.setAttribute("expenseCategories", expenseCategories);
            request.setAttribute("filterMonth", filterMonth);
            request.setAttribute("filterYear", filterYear);
            request.setAttribute("currentMonth", currentMonth);
            request.setAttribute("currentYear", currentYear);
            
            // Forward to JSP
            request.getRequestDispatcher("/views/finance/transactions.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error loading transactions: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading transactions");
            request.getRequestDispatcher("/views/finance/transactions.jsp").forward(request, response);
        }
    }
}