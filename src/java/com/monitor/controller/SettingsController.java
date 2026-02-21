package com.monitor.controller;

import com.monitor.dao.UserPreferenceDAO;
import com.monitor.dao.UserPreferenceDAOImpl;
import com.monitor.model.User;
import com.monitor.model.UserPreference;
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

    private UserPreferenceDAO userPreferenceDAO;

    @Override
    public void init() throws ServletException {
        userPreferenceDAO = new UserPreferenceDAOImpl();
        try {
            userPreferenceDAO.ensureTableExists();
        } catch (Exception e) {
            throw new ServletException("Failed to initialize settings storage", e);
        }
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
            UserPreference preference = userPreferenceDAO.getByUserId(user.getUserId());
            request.getSession().setAttribute("theme", preference.getTheme());
            request.setAttribute("user", user);
            request.setAttribute("preference", preference);

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
            UserPreference preference = userPreferenceDAO.getByUserId(user.getUserId());

            if ("notifications".equals(action)) {
                preference.setEmailNotifications("true".equals(request.getParameter("emailNotifications")));
                preference.setBillReminders("true".equals(request.getParameter("billReminders")));
                preference.setTaskReminders("true".equals(request.getParameter("taskReminders")));
                userPreferenceDAO.saveOrUpdate(preference);
                response.sendRedirect(request.getContextPath() + "/settings?updated=notifications");
            } else if ("preferences".equals(action)) {
                preference.setTheme(request.getParameter("theme"));
                preference.setLanguage(request.getParameter("language"));
                preference.setDateFormat(request.getParameter("dateFormat"));
                preference.setCurrency(request.getParameter("currency"));
                userPreferenceDAO.saveOrUpdate(preference);
                session.setAttribute("theme", preference.getTheme());
                response.sendRedirect(request.getContextPath() + "/settings?updated=preferences");
            } else if ("privacy".equals(action)) {
                preference.setProfileVisibility(request.getParameter("profileVisibility"));
                preference.setDataSharing("true".equals(request.getParameter("dataSharing")));
                userPreferenceDAO.saveOrUpdate(preference);
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
