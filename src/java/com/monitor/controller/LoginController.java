package com.monitor.controller;

import com.monitor.dao.UserDAO;
import com.monitor.dao.UserDAOImpl;
import com.monitor.model.User;
import com.monitor.util.ValidationUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet for handling user login
 */
@WebServlet(name = "LoginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {
    
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
    }
    
    /**
     * Handles GET request - display login form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }
    
    /**
     * Handles POST request - process login
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get form parameters
        String emailOrUsername = request.getParameter("email");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");
        
        // Validation
        if (ValidationUtil.isEmpty(emailOrUsername) || ValidationUtil.isEmpty(password)) {
            request.setAttribute("errorMessage", "Email/Username and password are required");
            request.setAttribute("email", emailOrUsername);
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }
        
        try {
            // Validate user credentials
            User user = userDAO.validateUser(emailOrUsername, password);
            
            if (user != null) {
                // Login successful - create session
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("username", user.getUsername());
                
                // Set session timeout (30 minutes)
                session.setMaxInactiveInterval(30 * 60);
                
                // Redirect to dashboard
                response.sendRedirect(request.getContextPath() + "/dashboard");
                
            } else {
                // Login failed - invalid credentials
                request.setAttribute("errorMessage", "Invalid email/username or password");
                request.setAttribute("email", emailOrUsername);
                request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("Error during login: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again later.");
            request.setAttribute("email", emailOrUsername);
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }
}