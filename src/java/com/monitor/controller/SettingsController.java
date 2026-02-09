package com.monitor.controller;

import com.monitor.dao.UserDAO;
import com.monitor.dao.UserDAOImpl;
import com.monitor.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet for application settings
 */
@WebServlet(name = "SettingsController", urlPatterns = {"/settings"})
public class SettingsController extends HttpServlet {
    
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        showSettings(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        handleUpdateSettings(request, response);
    }
    
    private void showSettings(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        User user = (User) request.getSession().getAttribute("user");
        
        try {
            // Load user preferences (could be from a preferences table)
            request.setAttribute("user", user);
            
            // Get session timeout from web.xml (default 30 minutes)
            int sessionTimeout = request.getSession().getMaxInactiveInterval() / 60;
            request.setAttribute("sessionTimeout", sessionTimeout);
            
            request.getRequestDispatcher("/views/settings/settings.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading settings");
            request.getRequestDispatcher("/views/settings/settings.jsp").forward(request, response);
        }
    }
    
    private void handleUpdateSettings(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        String action = request.getParameter("action");
        
        try {
            if ("notifications".equals(action)) {
                // Handle notification settings
                String emailNotifications = request.getParameter("emailNotifications");
                String billReminders = request.getParameter("billReminders");
                String taskReminders = request.getParameter("taskReminders");
                
                // In a real app, save these to a user_preferences table
                // For now, just show success
                
                response.sendRedirect(request.getContextPath() + "/settings?updated=notifications");
                
            } else if ("preferences".equals(action)) {
                // Handle general preferences
                String theme = request.getParameter("theme");
                String language = request.getParameter("language");
                String dateFormat = request.getParameter("dateFormat");
                String currency = request.getParameter("currency");
                
                // Save preferences
                response.sendRedirect(request.getContextPath() + "/settings?updated=preferences");
                
            } else if ("privacy".equals(action)) {
                // Handle privacy settings
                String profileVisibility = request.getParameter("profileVisibility");
                String dataSharing = request.getParameter("dataSharing");
                
                // Save privacy settings
                response.sendRedirect(request.getContextPath() + "/settings?updated=privacy");
                
            } else {
                response.sendRedirect(request.getContextPath() + "/settings");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error updating settings: " + e.getMessage());
            showSettings(request, response);
        }
    }
}