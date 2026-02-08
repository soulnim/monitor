package com.monitor.controller;

import com.monitor.dao.UserDAO;
import com.monitor.dao.UserDAOImpl;
import com.monitor.model.User;
import com.monitor.util.PasswordUtil;
import com.monitor.util.ValidationUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for handling user registration
 */
@WebServlet(name = "RegisterController", urlPatterns = {"/register"})
public class RegisterController extends HttpServlet {
    
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
    }
    
    /**
     * Handles GET request - display registration form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }
    
    /**
     * Handles POST request - process registration
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get form parameters
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullName");
        
        // Validation
        StringBuilder errors = new StringBuilder();
        
        // Validate username
        if (ValidationUtil.isEmpty(username)) {
            errors.append("Username is required. ");
        } else if (!ValidationUtil.isValidUsername(username)) {
            errors.append("Username must be 3-50 characters (alphanumeric, underscore, hyphen only). ");
        }
        
        // Validate email
        if (ValidationUtil.isEmpty(email)) {
            errors.append("Email is required. ");
        } else if (!ValidationUtil.isValidEmail(email)) {
            errors.append("Invalid email format. ");
        }
        
        // Validate password
        String passwordError = PasswordUtil.getPasswordStrengthMessage(password);
        if (passwordError != null) {
            errors.append(passwordError).append(" ");
        }
        
        // Validate password confirmation
        if (!password.equals(confirmPassword)) {
            errors.append("Passwords do not match. ");
        }
        
        // Check for validation errors
        if (errors.length() > 0) {
            request.setAttribute("errorMessage", errors.toString());
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("fullName", fullName);
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }
        
        try {
            // Check if email already exists
            if (userDAO.emailExists(email)) {
                request.setAttribute("errorMessage", "Email already registered");
                request.setAttribute("username", username);
                request.setAttribute("email", email);
                request.setAttribute("fullName", fullName);
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
                return;
            }
            
            // Check if username already exists
            if (userDAO.usernameExists(username)) {
                request.setAttribute("errorMessage", "Username already taken");
                request.setAttribute("username", username);
                request.setAttribute("email", email);
                request.setAttribute("fullName", fullName);
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
                return;
            }
            
            // Hash password
            String passwordHash = PasswordUtil.hashPassword(password);
            
            // Create user object
            User user = new User(username, email, passwordHash);
            user.setFullName(fullName);
            
            // Register user
            boolean registered = userDAO.registerUser(user);
            
            if (registered) {
                // Registration successful
                request.setAttribute("successMessage", "Registration successful! Please login.");
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                // Registration failed
                request.setAttribute("errorMessage", "Registration failed. Please try again.");
                request.setAttribute("username", username);
                request.setAttribute("email", email);
                request.setAttribute("fullName", fullName);
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("Error during registration: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again later.");
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("fullName", fullName);
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
        }
    }
}