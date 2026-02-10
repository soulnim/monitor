package com.monitor.controller;

import com.monitor.dao.*;
import com.monitor.model.User;
import com.google.gson.Gson;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Comprehensive Dashboard Controller loading data from all modules
 * Updated to provide real chart data
 */
@WebServlet(name = "DashboardController", urlPatterns = {"/dashboard"})
public class DashboardController extends HttpServlet {
    
    private TransactionDAO transactionDAO;
    private TaskDAO taskDAO;
    private BillDAO billDAO;
    private GoalDAO goalDAO;
    private EventDAO eventDAO;
    private NoteDAO noteDAO;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        transactionDAO = new TransactionDAOImpl();
        taskDAO = new TaskDAOImpl();
        billDAO = new BillDAOImpl();
        goalDAO = new GoalDAOImpl();
        eventDAO = new EventDAOImpl();
        noteDAO = new NoteDAOImpl();
        gson = new Gson();
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
            // Financial Data for Quick Stats
            BigDecimal monthlyIncome = transactionDAO.getTotalIncome(userId, currentMonth, currentYear);
            BigDecimal monthlyExpenses = transactionDAO.getTotalExpenses(userId, currentMonth, currentYear);
            BigDecimal netSavings = monthlyIncome.subtract(monthlyExpenses);
            
            request.setAttribute("monthlyIncome", monthlyIncome);
            request.setAttribute("monthlyExpenses", monthlyExpenses);
            request.setAttribute("netSavings", netSavings);
            
            // ======= CHART DATA =======
            
            // 1. Expense Breakdown Chart Data
            Map<String, BigDecimal> expensesByCategory = transactionDAO.getExpensesByCategory(
                userId, currentMonth, currentYear
            );
            
            // Convert to JSON for JavaScript
            List<String> expenseLabels = new ArrayList<>();
            List<Double> expenseAmounts = new ArrayList<>();
            
            if (expensesByCategory.isEmpty()) {
                // Provide default data when no expenses exist
                expenseLabels.add("No Expenses");
                expenseAmounts.add(0.0);
            } else {
                for (Map.Entry<String, BigDecimal> entry : expensesByCategory.entrySet()) {
                    expenseLabels.add(entry.getKey());
                    expenseAmounts.add(entry.getValue().doubleValue());
                }
            }
            
            request.setAttribute("expenseLabelsJson", gson.toJson(expenseLabels));
            request.setAttribute("expenseAmountsJson", gson.toJson(expenseAmounts));
            
            // 2. Income vs Expenses Trend Chart Data (Last 6 months)
            Map<String, List<?>> trendData = transactionDAO.getMonthlyTrend(userId, 6);
            
            @SuppressWarnings("unchecked")
            List<String> monthLabels = (List<String>) trendData.get("months");
            @SuppressWarnings("unchecked")
            List<BigDecimal> incomeList = (List<BigDecimal>) trendData.get("income");
            @SuppressWarnings("unchecked")
            List<BigDecimal> expensesList = (List<BigDecimal>) trendData.get("expenses");
            
            // Convert BigDecimal to Double for JSON
            List<Double> incomeAmounts = new ArrayList<>();
            List<Double> trendExpenseAmounts = new ArrayList<>();
            
            for (BigDecimal income : incomeList) {
                incomeAmounts.add(income.doubleValue());
            }
            
            for (BigDecimal expense : expensesList) {
                trendExpenseAmounts.add(expense.doubleValue());
            }
            
            request.setAttribute("trendMonthsJson", gson.toJson(monthLabels));
            request.setAttribute("trendIncomeJson", gson.toJson(incomeAmounts));
            request.setAttribute("trendExpensesJson", gson.toJson(trendExpenseAmounts));
            
            // ======= END CHART DATA =======
            
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
            
            // Set empty chart data
            request.setAttribute("expenseLabelsJson", "[]");
            request.setAttribute("expenseAmountsJson", "[]");
            request.setAttribute("trendMonthsJson", "[]");
            request.setAttribute("trendIncomeJson", "[]");
            request.setAttribute("trendExpensesJson", "[]");
        }
        
        // Set welcome message
        request.setAttribute("welcomeMessage", "Welcome, " + user.getUsername() + "!");
        
        // Forward to updated dashboard JSP
        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }
}