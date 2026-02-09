package com.monitor.controller;

import com.monitor.dao.TransactionDAO;
import com.monitor.dao.TransactionDAOImpl;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet for deleting transactions
 */
@WebServlet(name = "DeleteTransactionController", urlPatterns = {"/finance/delete-transaction"})
public class DeleteTransactionController extends HttpServlet {
    
    private TransactionDAO transactionDAO;
    
    @Override
    public void init() throws ServletException {
        transactionDAO = new TransactionDAOImpl();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String transactionIdStr = request.getParameter("transactionId");
        
        if (transactionIdStr == null || transactionIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/finance/transactions?error=invalid");
            return;
        }
        
        try {
            int transactionId = Integer.parseInt(transactionIdStr);
            boolean deleted = transactionDAO.deleteTransaction(transactionId);
            
            if (deleted) {
                response.sendRedirect(request.getContextPath() + "/finance/transactions?deleted=true");
            } else {
                response.sendRedirect(request.getContextPath() + "/finance/transactions?error=notfound");
            }
            
        } catch (Exception e) {
            System.err.println("Error deleting transaction: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/finance/transactions?error=delete");
        }
    }
}