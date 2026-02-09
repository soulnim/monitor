package com.monitor.dao;

import com.monitor.model.Task;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for Task operations
 */
public interface TaskDAO {
    
    /**
     * Create a new task
     * @param task Task object
     * @return true if created successfully
     * @throws SQLException if database error occurs
     */
    boolean createTask(Task task) throws SQLException;
    
    /**
     * Get all tasks for a user
     * @param userId User ID
     * @return List of tasks
     * @throws SQLException if database error occurs
     */
    List<Task> getAllTasks(int userId) throws SQLException;
    
    /**
     * Get tasks by status
     * @param userId User ID
     * @param status Task status (PENDING, IN_PROGRESS, COMPLETED)
     * @return List of tasks
     * @throws SQLException if database error occurs
     */
    List<Task> getTasksByStatus(int userId, String status) throws SQLException;
    
    /**
     * Get tasks by priority
     * @param userId User ID
     * @param priority Task priority (HIGH, MEDIUM, LOW)
     * @return List of tasks
     * @throws SQLException if database error occurs
     */
    List<Task> getTasksByPriority(int userId, String priority) throws SQLException;
    
    /**
     * Get today's tasks
     * @param userId User ID
     * @return List of tasks due today
     * @throws SQLException if database error occurs
     */
    List<Task> getTodayTasks(int userId) throws SQLException;
    
    /**
     * Get overdue tasks
     * @param userId User ID
     * @return List of overdue tasks
     * @throws SQLException if database error occurs
     */
    List<Task> getOverdueTasks(int userId) throws SQLException;
    
    /**
     * Get upcoming tasks (next N days)
     * @param userId User ID
     * @param days Number of days
     * @return List of upcoming tasks
     * @throws SQLException if database error occurs
     */
    List<Task> getUpcomingTasks(int userId, int days) throws SQLException;
    
    /**
     * Get task by ID
     * @param taskId Task ID
     * @return Task object if found, null otherwise
     * @throws SQLException if database error occurs
     */
    Task getTaskById(int taskId) throws SQLException;
    
    /**
     * Update a task
     * @param task Task object with updated values
     * @return true if updated successfully
     * @throws SQLException if database error occurs
     */
    boolean updateTask(Task task) throws SQLException;
    
    /**
     * Mark task as completed
     * @param taskId Task ID
     * @return true if updated successfully
     * @throws SQLException if database error occurs
     */
    boolean markAsCompleted(int taskId) throws SQLException;
    
    /**
     * Delete a task
     * @param taskId Task ID
     * @return true if deleted successfully
     * @throws SQLException if database error occurs
     */
    boolean deleteTask(int taskId) throws SQLException;
    
    /**
     * Get task count by status
     * @param userId User ID
     * @param status Task status
     * @return Count of tasks
     * @throws SQLException if database error occurs
     */
    int getTaskCountByStatus(int userId, String status) throws SQLException;
}