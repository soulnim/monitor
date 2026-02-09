package com.monitor.controller;

import com.monitor.dao.*;
import com.monitor.model.User;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Comprehensive Dashboard Controller loading data from all modules
 */
@WebServlet(name = "DashboardController", urlPatterns = {"/dashboard"})
public class DashboardController extends HttpServlet {
    
    private TransactionDAO transactionDAO;
    private TaskDAO taskDAO;
    private BillDAO billDAO;
    private GoalDAO goalDAO;
    private EventDAO eventDAO;
    private NoteDAO noteDAO;
    
    @Override
    public void init() throws ServletException {
        transactionDAO = new TransactionDAOImpl();
        taskDAO = new TaskDAOImpl();
        billDAO = new BillDAOImpl();
        goalDAO = new GoalDAOImpl();
        eventDAO = new EventDAOImpl();
        noteDAO = new NoteDAOImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get user from session
        User user = (User) session.getAttribute("user");
        int userId = user.getUserId();
        
        // Get current month and year
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int currentYear = cal.get(Calendar.YEAR);
        
        try {
            // Financial Data
            BigDecimal monthlyIncome = transactionDAO.getTotalIncome(userId, currentMonth, currentYear);
            BigDecimal monthlyExpenses = transactionDAO.getTotalExpenses(userId, currentMonth, currentYear);
            BigDecimal netSavings = monthlyIncome.subtract(monthlyExpenses);
            
            request.setAttribute("monthlyIncome", monthlyIncome);
            request.setAttribute("monthlyExpenses", monthlyExpenses);
            request.setAttribute("netSavings", netSavings);
            
            // Task Data
            request.setAttribute("todayTasks", taskDAO.getTodayTasks(userId));
            request.setAttribute("pendingTasks", taskDAO.getTaskCountByStatus(userId, "PENDING"));
            
            // Bill Data
            request.setAttribute("upcomingBillsList", billDAO.getUpcomingBills(userId, 7));
            request.setAttribute("upcomingBills", billDAO.getUpcomingBills(userId, 7).size());
            
            // Goal Data
            request.setAttribute("activeGoals", goalDAO.getGoalsByStatus(userId, "ACTIVE"));
            
            // Event Data
            request.setAttribute("upcomingEvents", eventDAO.getUpcomingEvents(userId, 3));
            
            // Note Data
            request.setAttribute("recentNotes", noteDAO.getRecentNotes(userId, 3));
            
        } catch (Exception e) {
            System.err.println("Error loading dashboard data: " + e.getMessage());
            e.printStackTrace();
            // Set default values
            request.setAttribute("monthlyIncome", BigDecimal.ZERO);
            request.setAttribute("monthlyExpenses", BigDecimal.ZERO);
            request.setAttribute("netSavings", BigDecimal.ZERO);
            request.setAttribute("pendingTasks", 0);
            request.setAttribute("upcomingBills", 0);
        }
        
        // Set welcome message
        request.setAttribute("welcomeMessage", "Welcome, " + user.getUsername() + "!");
        
        // Forward to updated dashboard JSP
        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }
}