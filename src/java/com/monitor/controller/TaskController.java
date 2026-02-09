package com.monitor.controller;

import com.monitor.dao.TaskDAO;
import com.monitor.dao.TaskDAOImpl;
import com.monitor.model.Task;
import com.monitor.util.ValidationUtil;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet for task management
 */
@WebServlet(name = "TaskController", urlPatterns = {"/tasks", "/tasks/add", "/tasks/complete", "/tasks/delete"})
public class TaskController extends HttpServlet {
    
    private TaskDAO taskDAO;
    
    @Override
    public void init() throws ServletException {
        taskDAO = new TaskDAOImpl();
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
        
        if (path.equals("/tasks/add")) {
            // Show add task form
            request.getRequestDispatcher("/views/tasks/add-task.jsp").forward(request, response);
        } else {
            // Show task list
            showTaskList(request, response);
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
        
        if (path.equals("/tasks/add")) {
            handleAddTask(request, response);
        } else if (path.equals("/tasks/complete")) {
            handleCompleteTask(request, response);
        } else if (path.equals("/tasks/delete")) {
            handleDeleteTask(request, response);
        }
    }
    
    private void showTaskList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int userId = (Integer) request.getSession().getAttribute("userId");
        
        try {
            // Get filter parameter
            String statusFilter = request.getParameter("status");
            String priorityFilter = request.getParameter("priority");
            
            List<Task> tasks;
            
            if (statusFilter != null && !statusFilter.isEmpty()) {
                tasks = taskDAO.getTasksByStatus(userId, statusFilter);
            } else if (priorityFilter != null && !priorityFilter.isEmpty()) {
                tasks = taskDAO.getTasksByPriority(userId, priorityFilter);
            } else {
                tasks = taskDAO.getAllTasks(userId);
            }
            
            // Get counts
            int pendingCount = taskDAO.getTaskCountByStatus(userId, "PENDING");
            int inProgressCount = taskDAO.getTaskCountByStatus(userId, "IN_PROGRESS");
            int completedCount = taskDAO.getTaskCountByStatus(userId, "COMPLETED");
            
            // Get overdue tasks
            List<Task> overdueTasks = taskDAO.getOverdueTasks(userId);
            
            request.setAttribute("tasks", tasks);
            request.setAttribute("pendingCount", pendingCount);
            request.setAttribute("inProgressCount", inProgressCount);
            request.setAttribute("completedCount", completedCount);
            request.setAttribute("overdueCount", overdueTasks.size());
            
            request.getRequestDispatcher("/views/tasks/tasks.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading tasks");
            request.getRequestDispatcher("/views/tasks/tasks.jsp").forward(request, response);
        }
    }
    
    private void handleAddTask(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int userId = (Integer) request.getSession().getAttribute("userId");
        
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String dueDateStr = request.getParameter("dueDate");
        String priority = request.getParameter("priority");
        
        // Validation
        if (ValidationUtil.isEmpty(title)) {
            request.setAttribute("errorMessage", "Task title is required");
            request.getRequestDispatcher("/views/tasks/add-task.jsp").forward(request, response);
            return;
        }
        
        try {
            Task task = new Task();
            task.setUserId(userId);
            task.setTitle(title);
            task.setDescription(description);
            
            if (dueDateStr != null && !dueDateStr.isEmpty()) {
                task.setDueDate(Date.valueOf(dueDateStr));
            }
            
            task.setPriority(priority != null ? priority : "MEDIUM");
            task.setStatus("PENDING");
            
            boolean created = taskDAO.createTask(task);
            
            if (created) {
                response.sendRedirect(request.getContextPath() + "/tasks?success=true");
            } else {
                request.setAttribute("errorMessage", "Failed to create task");
                request.getRequestDispatcher("/views/tasks/add-task.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("Error creating task: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error creating task");
            request.getRequestDispatcher("/views/tasks/add-task.jsp").forward(request, response);
        }
    }
    
    private void handleCompleteTask(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        String taskIdStr = request.getParameter("taskId");
        
        try {
            int taskId = Integer.parseInt(taskIdStr);
            taskDAO.markAsCompleted(taskId);
            response.sendRedirect(request.getContextPath() + "/tasks?completed=true");
        } catch (Exception e) {
            System.err.println("Error completing task: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/tasks?error=complete");
        }
    }
    
    private void handleDeleteTask(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        String taskIdStr = request.getParameter("taskId");
        
        try {
            int taskId = Integer.parseInt(taskIdStr);
            taskDAO.deleteTask(taskId);
            response.sendRedirect(request.getContextPath() + "/tasks?deleted=true");
        } catch (Exception e) {
            System.err.println("Error deleting task: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/tasks?error=delete");
        }
    }
}