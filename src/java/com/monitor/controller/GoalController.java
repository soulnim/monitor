package com.monitor.controller;

import com.monitor.dao.*;
import com.monitor.model.*;
import com.monitor.util.ValidationUtil;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "GoalController", urlPatterns = {"/goals", "/goals/add", "/goals/view", "/goals/achieve", "/goals/delete", "/goals/add-milestone", "/goals/complete-milestone"})
public class GoalController extends HttpServlet {
    
    private GoalDAO goalDAO;
    private CategoryDAO categoryDAO;
    
    @Override
    public void init() throws ServletException {
        goalDAO = new GoalDAOImpl();
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
        
        if (path.equals("/goals/add")) {
            showAddForm(request, response);
        } else if (path.equals("/goals/view")) {
            showGoalDetails(request, response);
        } else {
            showGoalList(request, response);
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
        
        if (path.equals("/goals/add")) {
            handleAddGoal(request, response);
        } else if (path.equals("/goals/achieve")) {
            handleAchieveGoal(request, response);
        } else if (path.equals("/goals/delete")) {
            handleDeleteGoal(request, response);
        } else if (path.equals("/goals/add-milestone")) {
            handleAddMilestone(request, response);
        } else if (path.equals("/goals/complete-milestone")) {
            handleCompleteMilestone(request, response);
        }
    }
    
    private void showGoalList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = (Integer) request.getSession().getAttribute("userId");
        try {
            List<Goal> goals = goalDAO.getAllGoals(userId);
            int activeCount = goalDAO.getGoalCountByStatus(userId, "ACTIVE");
            int achievedCount = goalDAO.getGoalCountByStatus(userId, "ACHIEVED");
            
            request.setAttribute("goals", goals);
            request.setAttribute("activeCount", activeCount);
            request.setAttribute("achievedCount", achievedCount);
            request.getRequestDispatcher("/views/goals/goals.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading goals");
            request.getRequestDispatcher("/views/goals/goals.jsp").forward(request, response);
        }
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = (Integer) request.getSession().getAttribute("userId");
        try {
            List<Category> categories = categoryDAO.getCategoriesByType(userId, "GOAL");
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/views/goals/add-goal.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher("/views/goals/add-goal.jsp").forward(request, response);
        }
    }
    
    private void showGoalDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int goalId = Integer.parseInt(request.getParameter("id"));
            Goal goal = goalDAO.getGoalById(goalId);
            List<Milestone> milestones = goalDAO.getMilestonesByGoal(goalId);
            
            request.setAttribute("goal", goal);
            request.setAttribute("milestones", milestones);
            request.getRequestDispatcher("/views/goals/goal-details.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/goals?error=view");
        }
    }
    
    private void handleAddGoal(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = (Integer) request.getSession().getAttribute("userId");
        
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String targetDateStr = request.getParameter("targetDate");
        String categoryIdStr = request.getParameter("categoryId");
        
        if (ValidationUtil.isEmpty(title)) {
            request.setAttribute("errorMessage", "Goal title is required");
            showAddForm(request, response);
            return;
        }
        
        try {
            Goal goal = new Goal();
            goal.setUserId(userId);
            goal.setTitle(title);
            goal.setDescription(description);
            if (targetDateStr != null && !targetDateStr.isEmpty()) {
                goal.setTargetDate(Date.valueOf(targetDateStr));
            }
            if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
                goal.setCategoryId(Integer.parseInt(categoryIdStr));
            }
            goal.setStatus("ACTIVE");
            
            goalDAO.createGoal(goal);
            response.sendRedirect(request.getContextPath() + "/goals?success=true");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error creating goal");
            showAddForm(request, response);
        }
    }
    
    private void handleAchieveGoal(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int goalId = Integer.parseInt(request.getParameter("goalId"));
            goalDAO.markAsAchieved(goalId);
            response.sendRedirect(request.getContextPath() + "/goals?achieved=true");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/goals?error=achieve");
        }
    }
    
    private void handleDeleteGoal(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int goalId = Integer.parseInt(request.getParameter("goalId"));
            goalDAO.deleteGoal(goalId);
            response.sendRedirect(request.getContextPath() + "/goals?deleted=true");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/goals?error=delete");
        }
    }
    
    private void handleAddMilestone(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int goalId = Integer.parseInt(request.getParameter("goalId"));
            String title = request.getParameter("title");
            
            Milestone milestone = new Milestone();
            milestone.setGoalId(goalId);
            milestone.setTitle(title);
            milestone.setDescription(request.getParameter("description"));
            String targetDateStr = request.getParameter("targetDate");
            if (targetDateStr != null && !targetDateStr.isEmpty()) {
                milestone.setTargetDate(Date.valueOf(targetDateStr));
            }
            
            goalDAO.createMilestone(milestone);
            response.sendRedirect(request.getContextPath() + "/goals/view?id=" + goalId + "&milestone=added");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/goals?error=milestone");
        }
    }
    
    private void handleCompleteMilestone(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int milestoneId = Integer.parseInt(request.getParameter("milestoneId"));
            int goalId = Integer.parseInt(request.getParameter("goalId"));
            goalDAO.markMilestoneComplete(milestoneId);
            response.sendRedirect(request.getContextPath() + "/goals/view?id=" + goalId + "&milestone=completed");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/goals?error=complete");
        }
    }
}