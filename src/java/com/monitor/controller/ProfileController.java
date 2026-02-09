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
import javax.servlet.http.HttpSession;

/**
 * Servlet for user profile management
 */
@WebServlet(name = "ProfileController", urlPatterns = {"/profile", "/profile/edit", "/profile/change-password"})
public class ProfileController extends HttpServlet {
    
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
        
        String path = request.getServletPath();
        
        if (path.equals("/profile/edit")) {
            showEditProfile(request, response);
        } else if (path.equals("/profile/change-password")) {
            request.getRequestDispatcher("/views/profile/change-password.jsp").forward(request, response);
        } else {
            showProfile(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String path = request.getServletPath();
        
        if (path.equals("/profile/edit")) {
            handleEditProfile(request, response);
        } else if (path.equals("/profile/change-password")) {
            handleChangePassword(request, response);
        }
    }
    
    private void showProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        User user = (User) request.getSession().getAttribute("user");
        
        try {
            // Refresh user data from database
            User currentUser = userDAO.findById(user.getUserId());
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("/views/profile/profile.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading profile");
            request.getRequestDispatcher("/views/profile/profile.jsp").forward(request, response);
        }
    }
    
    private void showEditProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        User user = (User) request.getSession().getAttribute("user");
        
        try {
            User currentUser = userDAO.findById(user.getUserId());
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("/views/profile/edit-profile.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading profile");
            request.getRequestDispatcher("/views/profile/edit-profile.jsp").forward(request, response);
        }
    }
    
    private void handleEditProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("user");
        int userId = sessionUser.getUserId();
        
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");
        
        // Validation
        StringBuilder errors = new StringBuilder();
        
        if (ValidationUtil.isEmpty(username)) {
            errors.append("Username is required. ");
        } else if (!ValidationUtil.isValidUsername(username)) {
            errors.append("Username must be 3-50 characters (alphanumeric, underscore, hyphen only). ");
        }
        
        if (ValidationUtil.isEmpty(email)) {
            errors.append("Email is required. ");
        } else if (!ValidationUtil.isValidEmail(email)) {
            errors.append("Invalid email format. ");
        }
        
        if (errors.length() > 0) {
            request.setAttribute("errorMessage", errors.toString());
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("fullName", fullName);
            request.getRequestDispatcher("/views/profile/edit-profile.jsp").forward(request, response);
            return;
        }
        
        try {
            // Check if username/email already taken by another user
            if (!username.equals(sessionUser.getUsername())) {
                if (userDAO.usernameExists(username)) {
                    request.setAttribute("errorMessage", "Username already taken");
                    request.setAttribute("username", username);
                    request.setAttribute("email", email);
                    request.setAttribute("fullName", fullName);
                    request.getRequestDispatcher("/views/profile/edit-profile.jsp").forward(request, response);
                    return;
                }
            }
            
            if (!email.equals(sessionUser.getEmail())) {
                if (userDAO.emailExists(email)) {
                    request.setAttribute("errorMessage", "Email already registered");
                    request.setAttribute("username", username);
                    request.setAttribute("email", email);
                    request.setAttribute("fullName", fullName);
                    request.getRequestDispatcher("/views/profile/edit-profile.jsp").forward(request, response);
                    return;
                }
            }
            
            // Update user
            User user = userDAO.findById(userId);
            user.setUsername(username);
            user.setEmail(email);
            user.setFullName(fullName);
            
            boolean updated = userDAO.updateUser(user);
            
            if (updated) {
                // Update session
                User updatedUser = userDAO.findById(userId);
                session.setAttribute("user", updatedUser);
                session.setAttribute("username", updatedUser.getUsername());
                
                response.sendRedirect(request.getContextPath() + "/profile?updated=true");
            } else {
                request.setAttribute("errorMessage", "Failed to update profile");
                request.setAttribute("username", username);
                request.setAttribute("email", email);
                request.setAttribute("fullName", fullName);
                request.getRequestDispatcher("/views/profile/edit-profile.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error updating profile: " + e.getMessage());
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("fullName", fullName);
            request.getRequestDispatcher("/views/profile/edit-profile.jsp").forward(request, response);
        }
    }
    
    private void handleChangePassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        User user = (User) request.getSession().getAttribute("user");
        int userId = user.getUserId();
        
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validation
        StringBuilder errors = new StringBuilder();
        
        if (ValidationUtil.isEmpty(currentPassword)) {
            errors.append("Current password is required. ");
        }
        
        String passwordError = PasswordUtil.getPasswordStrengthMessage(newPassword);
        if (passwordError != null) {
            errors.append(passwordError).append(" ");
        }
        
        if (!newPassword.equals(confirmPassword)) {
            errors.append("New passwords do not match. ");
        }
        
        if (errors.length() > 0) {
            request.setAttribute("errorMessage", errors.toString());
            request.getRequestDispatcher("/views/profile/change-password.jsp").forward(request, response);
            return;
        }
        
        try {
            // Verify current password
            User dbUser = userDAO.findById(userId);
            if (!PasswordUtil.verifyPassword(currentPassword, dbUser.getPasswordHash())) {
                request.setAttribute("errorMessage", "Current password is incorrect");
                request.getRequestDispatcher("/views/profile/change-password.jsp").forward(request, response);
                return;
            }
            
            // Update password
            String newPasswordHash = PasswordUtil.hashPassword(newPassword);
            boolean updated = userDAO.updatePassword(userId, newPasswordHash);
            
            if (updated) {
                response.sendRedirect(request.getContextPath() + "/profile?password=changed");
            } else {
                request.setAttribute("errorMessage", "Failed to change password");
                request.getRequestDispatcher("/views/profile/change-password.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error changing password: " + e.getMessage());
            request.getRequestDispatcher("/views/profile/change-password.jsp").forward(request, response);
        }
    }
}