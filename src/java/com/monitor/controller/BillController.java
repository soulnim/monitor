package com.monitor.controller;

import com.monitor.dao.BillDAO;
import com.monitor.dao.BillDAOImpl;
import com.monitor.model.Bill;
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
 * Servlet for bill management
 */
@WebServlet(name = "BillController", urlPatterns = {"/bills", "/bills/add", "/bills/pay", "/bills/delete"})
public class BillController extends HttpServlet {
    
    private BillDAO billDAO;
    
    @Override
    public void init() throws ServletException {
        billDAO = new BillDAOImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String path = request.getServletPath();
        
        if (path.equals("/bills/add")) {
            request.getRequestDispatcher("/views/bills/add-bill.jsp").forward(request, response);
        } else {
            showBillList(request, response);
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
        
        String path = request.getServletPath();
        
        if (path.equals("/bills/add")) {
            handleAddBill(request, response);
        } else if (path.equals("/bills/pay")) {
            handlePayBill(request, response);
        } else if (path.equals("/bills/delete")) {
            handleDeleteBill(request, response);
        }
    }
    
    private void showBillList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int userId = (Integer) request.getSession().getAttribute("userId");
        
        try {
            // Update overdue status first
            billDAO.updateOverdueStatus(userId);
            
            // Get filter
            String statusFilter = request.getParameter("status");
            
            List<Bill> bills;
            if (statusFilter != null && !statusFilter.isEmpty()) {
                bills = billDAO.getBillsByStatus(userId, statusFilter);
            } else {
                bills = billDAO.getAllBills(userId);
            }
            
            // Get upcoming bills
            List<Bill> upcomingBills = billDAO.getUpcomingBills(userId, 7);
            
            // Get overdue bills
            List<Bill> overdueBills = billDAO.getOverdueBills(userId);
            
            // Get total unpaid
            BigDecimal totalUnpaid = billDAO.getTotalUnpaidAmount(userId);
            
            request.setAttribute("bills", bills);
            request.setAttribute("upcomingBills", upcomingBills);
            request.setAttribute("overdueBills", overdueBills);
            request.setAttribute("totalUnpaid", totalUnpaid);
            
            request.getRequestDispatcher("/views/bills/bills.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error loading bills: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading bills");
            request.getRequestDispatcher("/views/bills/bills.jsp").forward(request, response);
        }
    }
    
    private void handleAddBill(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int userId = (Integer) request.getSession().getAttribute("userId");
        
        String billName = request.getParameter("billName");
        String amountStr = request.getParameter("amount");
        String dueDateStr = request.getParameter("dueDate");
        String category = request.getParameter("category");
        String isRecurringStr = request.getParameter("isRecurring");
        String recurringFrequency = request.getParameter("recurringFrequency");
        
        // Validation
        if (ValidationUtil.isEmpty(billName) || !ValidationUtil.isPositiveNumber(amountStr)) {
            request.setAttribute("errorMessage", "Bill name and valid amount are required");
            request.getRequestDispatcher("/views/bills/add-bill.jsp").forward(request, response);
            return;
        }
        
        try {
            Bill bill = new Bill();
            bill.setUserId(userId);
            bill.setBillName(billName);
            bill.setAmount(new BigDecimal(amountStr));
            bill.setDueDate(Date.valueOf(dueDateStr));
            bill.setCategory(category);
            bill.setStatus("UNPAID");
            bill.setRecurring("on".equals(isRecurringStr));
            bill.setRecurringFrequency(recurringFrequency);
            
            boolean created = billDAO.createBill(bill);
            
            if (created) {
                response.sendRedirect(request.getContextPath() + "/bills?success=true");
            } else {
                request.setAttribute("errorMessage", "Failed to create bill");
                request.getRequestDispatcher("/views/bills/add-bill.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("Error creating bill: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error creating bill");
            request.getRequestDispatcher("/views/bills/add-bill.jsp").forward(request, response);
        }
    }
    
    private void handlePayBill(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        String billIdStr = request.getParameter("billId");
        
        try {
            int billId = Integer.parseInt(billIdStr);
            Date paymentDate = new Date(System.currentTimeMillis());
            billDAO.markAsPaid(billId, paymentDate);
            response.sendRedirect(request.getContextPath() + "/bills?paid=true");
        } catch (Exception e) {
            System.err.println("Error paying bill: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/bills?error=pay");
        }
    }
    
    private void handleDeleteBill(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        String billIdStr = request.getParameter("billId");
        
        try {
            int billId = Integer.parseInt(billIdStr);
            billDAO.deleteBill(billId);
            response.sendRedirect(request.getContextPath() + "/bills?deleted=true");
        } catch (Exception e) {
            System.err.println("Error deleting bill: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/bills?error=delete");
        }
    }
}