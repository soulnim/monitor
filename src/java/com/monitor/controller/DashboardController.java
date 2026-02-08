package com.monitor.controller;

import com.monitor.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet for displaying user dashboard
 */
@WebServlet(name = "DashboardController", urlPatterns = {"/dashboard"})
public class DashboardController extends HttpServlet {
    
    /**
     * Handles GET request - display dashboard
     */
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
        
        // Set user info for display
        request.setAttribute("welcomeMessage", "Welcome, " + user.getUsername() + "!");
        
        // TODO: Load dashboard data (tasks, bills, events, financial summary, etc.)
        // For now, just forward to dashboard JSP
        
        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }
}