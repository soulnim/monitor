package com.monitor.controller;

import com.monitor.dao.*;
import com.monitor.model.*;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "ScheduleController", urlPatterns = {"/schedule", "/schedule/add", "/schedule/delete"})
public class ScheduleController extends HttpServlet {
    private EventDAO eventDAO;
    
    @Override
    public void init() { eventDAO = new EventDAOImpl(); }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        if (request.getServletPath().equals("/schedule/add")) {
            request.getRequestDispatcher("/views/schedule/add-event.jsp").forward(request, response);
        } else {
            showSchedule(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        if (request.getServletPath().equals("/schedule/add")) {
            handleAddEvent(request, response);
        } else if (request.getServletPath().equals("/schedule/delete")) {
            handleDeleteEvent(request, response);
        }
    }
    
    private void showSchedule(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (Integer) request.getSession().getAttribute("userId");
        try {
            List<Event> events = eventDAO.getAllEvents(userId);
            List<Event> upcomingEvents = eventDAO.getUpcomingEvents(userId, 7);
            request.setAttribute("events", events);
            request.setAttribute("upcomingEvents", upcomingEvents);
            request.getRequestDispatcher("/views/schedule/schedule.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading schedule");
            request.getRequestDispatcher("/views/schedule/schedule.jsp").forward(request, response);
        }
    }
    
    private void handleAddEvent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = (Integer) request.getSession().getAttribute("userId");
        try {
            Event event = new Event();
            event.setUserId(userId);
            event.setTitle(request.getParameter("title"));
            event.setDescription(request.getParameter("description"));
            event.setEventDate(Date.valueOf(request.getParameter("eventDate")));
            String startTime = request.getParameter("startTime");
            if (startTime != null && !startTime.isEmpty()) {
                event.setStartTime(Time.valueOf(startTime + ":00"));
            }
            String endTime = request.getParameter("endTime");
            if (endTime != null && !endTime.isEmpty()) {
                event.setEndTime(Time.valueOf(endTime + ":00"));
            }
            event.setEventType(request.getParameter("eventType"));
            
            eventDAO.createEvent(event);
            response.sendRedirect(request.getContextPath() + "/schedule?success=true");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/schedule?error=add");
        }
    }
    
    private void handleDeleteEvent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int eventId = Integer.parseInt(request.getParameter("eventId"));
            eventDAO.deleteEvent(eventId);
            response.sendRedirect(request.getContextPath() + "/schedule?deleted=true");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/schedule?error=delete");
        }
    }
}