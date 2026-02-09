package com.monitor.controller;

import com.monitor.dao.TransactionDAO;
import com.monitor.dao.TransactionDAOImpl;
import com.monitor.dao.CategoryDAO;
import com.monitor.dao.CategoryDAOImpl;
import com.monitor.model.Transaction;
import com.monitor.model.Category;
import com.monitor.util.ValidationUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet for adding new transactions
 */
@WebServlet(name = "AddTransactionController", urlPatterns = {"/finance/add-transaction"})
public class AddTransactionController extends HttpServlet {
    
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
            // Initialize default categories if not exists
            List<Category> allCategories = categoryDAO.getAllCategories(userId);
            if (allCategories.isEmpty()) {
                categoryDAO.initializeDefaultCategories(userId);
            }
            
            // Get categories for dropdowns
            List<Category> incomeCategories = categoryDAO.getCategoriesByType(userId, "INCOME");
            List<Category> expenseCategories = categoryDAO.getCategoriesByType(userId, "EXPENSE");
            
            request.setAttribute("incomeCategories", incomeCategories);
            request.setAttribute("expenseCategories", expenseCategories);
            
            request.getRequestDispatcher("/views/finance/add-transaction.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error loading add transaction page: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading page");
            request.getRequestDispatcher("/views/finance/add-transaction.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        int userId = (Integer) session.getAttribute("userId");
        
        // Get form parameters
        String transactionType = request.getParameter("transactionType");
        String categoryIdStr = request.getParameter("categoryId");
        String amountStr = request.getParameter("amount");
        String transactionDateStr = request.getParameter("transactionDate");
        String description = request.getParameter("description");
        String isRecurringStr = request.getParameter("isRecurring");
        String recurringFrequency = request.getParameter("recurringFrequency");
        
        // Validation
        StringBuilder errors = new StringBuilder();
        
        if (ValidationUtil.isEmpty(transactionType)) {
            errors.append("Transaction type is required. ");
        }
        
        if (ValidationUtil.isEmpty(categoryIdStr)) {
            errors.append("Category is required. ");
        }
        
        if (!ValidationUtil.isPositiveNumber(amountStr)) {
            errors.append("Amount must be a positive number. ");
        }
        
        if (!ValidationUtil.isValidDateFormat(transactionDateStr)) {
            errors.append("Invalid date format. ");
        }
        
        if (errors.length() > 0) {
            request.setAttribute("errorMessage", errors.toString());
            doGet(request, response);
            return;
        }
        
        try {
            // Create transaction object
            Transaction transaction = new Transaction();
            transaction.setUserId(userId);
            transaction.setCategoryId(Integer.parseInt(categoryIdStr));
            transaction.setAmount(new BigDecimal(amountStr));
            transaction.setTransactionType(transactionType);
            transaction.setTransactionDate(Date.valueOf(transactionDateStr));
            transaction.setDescription(description);
            transaction.setRecurring("on".equals(isRecurringStr));
            transaction.setRecurringFrequency(recurringFrequency);
            
            // Save transaction
            boolean created = transactionDAO.createTransaction(transaction);
            
            if (created) {
                response.sendRedirect(request.getContextPath() + "/finance/transactions?success=true");
            } else {
                request.setAttribute("errorMessage", "Failed to create transaction");
                doGet(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("Error creating transaction: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error creating transaction: " + e.getMessage());
            doGet(request, response);
        }
    }
}