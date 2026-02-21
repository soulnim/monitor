package com.monitor.controller;

import com.monitor.dao.BudgetDAO;
import com.monitor.dao.BudgetDAOImpl;
import com.monitor.dao.CategoryDAO;
import com.monitor.dao.CategoryDAOImpl;
import com.monitor.model.Budget;
import com.monitor.model.Category;
import com.monitor.util.ValidationUtil;
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

@WebServlet(name = "BudgetController", urlPatterns = {"/budgets", "/budgets/add", "/budgets/delete"})
public class BudgetController extends HttpServlet {

    private BudgetDAO budgetDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() throws ServletException {
        budgetDAO = new BudgetDAOImpl();
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

        String path = request.getServletPath();
        if ("/budgets/add".equals(path)) {
            showAddForm(request, response);
        } else {
            showBudgetList(request, response);
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
        if ("/budgets/add".equals(path)) {
            handleAddOrUpdate(request, response);
        } else if ("/budgets/delete".equals(path)) {
            handleDelete(request, response);
        }
    }

    private void showBudgetList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = (Integer) request.getSession().getAttribute("userId");
        Calendar cal = Calendar.getInstance();
        int month = parseInt(request.getParameter("month"), cal.get(Calendar.MONTH) + 1);
        int year = parseInt(request.getParameter("year"), cal.get(Calendar.YEAR));

        try {
            List<Budget> budgets = budgetDAO.getBudgetsByMonth(userId, month, year);
            request.setAttribute("budgets", budgets);
            request.setAttribute("filterMonth", month);
            request.setAttribute("filterYear", year);
            request.getRequestDispatcher("/views/budgets/budgets.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Unable to load budgets");
            request.getRequestDispatcher("/views/budgets/budgets.jsp").forward(request, response);
        }
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = (Integer) request.getSession().getAttribute("userId");
        Calendar cal = Calendar.getInstance();
        try {
            List<Category> categories = categoryDAO.getCategoriesByType(userId, "EXPENSE");
            request.setAttribute("categories", categories);
            request.setAttribute("currentMonth", cal.get(Calendar.MONTH) + 1);
            request.setAttribute("currentYear", cal.get(Calendar.YEAR));
            request.getRequestDispatcher("/views/budgets/add-budget.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/budgets?error=load");
        }
    }

    private void handleAddOrUpdate(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int userId = (Integer) request.getSession().getAttribute("userId");
        String categoryId = request.getParameter("categoryId");
        String budgetLimit = request.getParameter("budgetLimit");
        String budgetMonth = request.getParameter("budgetMonth");
        String budgetYear = request.getParameter("budgetYear");

        if (ValidationUtil.isEmpty(categoryId) || ValidationUtil.isEmpty(budgetLimit)
                || ValidationUtil.isEmpty(budgetMonth) || ValidationUtil.isEmpty(budgetYear)) {
            request.setAttribute("errorMessage", "All fields are required");
            showAddForm(request, response);
            return;
        }

        try {
            Budget budget = new Budget();
            budget.setUserId(userId);
            budget.setCategoryId(Integer.parseInt(categoryId));
            budget.setBudgetLimit(new BigDecimal(budgetLimit));
            budget.setBudgetMonth(Integer.parseInt(budgetMonth));
            budget.setBudgetYear(Integer.parseInt(budgetYear));

            budgetDAO.createOrUpdate(budget);
            response.sendRedirect(request.getContextPath() + "/budgets?saved=true&month="
                    + budget.getBudgetMonth() + "&year=" + budget.getBudgetYear());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/budgets?error=save");
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = (Integer) request.getSession().getAttribute("userId");
        try {
            int budgetId = Integer.parseInt(request.getParameter("budgetId"));
            budgetDAO.deleteBudget(budgetId, userId);
            response.sendRedirect(request.getContextPath() + "/budgets?deleted=true");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/budgets?error=delete");
        }
    }

    private int parseInt(String val, int fallback) {
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
            return fallback;
        }
    }
}
