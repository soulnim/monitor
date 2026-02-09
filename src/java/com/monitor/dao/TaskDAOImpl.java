package com.monitor.dao;

import com.monitor.model.Task;
import com.monitor.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Implementation of TaskDAO interface
 */
public class TaskDAOImpl implements TaskDAO {
    
    @Override
    public boolean createTask(Task task) throws SQLException {
        String sql = "INSERT INTO tasks (user_id, title, description, due_date, priority, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, task.getUserId());
            pstmt.setString(2, task.getTitle());
            pstmt.setString(3, task.getDescription());
            pstmt.setDate(4, task.getDueDate());
            pstmt.setString(5, task.getPriority());
            pstmt.setString(6, task.getStatus());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating task: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public List<Task> getAllTasks(int userId) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE user_id = ? ORDER BY due_date, priority DESC";
        return getTasksList(userId, sql);
    }
    
    @Override
    public List<Task> getTasksByStatus(int userId, String status) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE user_id = ? AND status = ? ORDER BY due_date";
        
        List<Task> tasks = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, status);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting tasks by status: " + e.getMessage());
            throw e;
        }
        
        return tasks;
    }
    
    @Override
    public List<Task> getTasksByPriority(int userId, String priority) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE user_id = ? AND priority = ? " +
                    "AND status != 'COMPLETED' ORDER BY due_date";
        
        List<Task> tasks = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, priority);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting tasks by priority: " + e.getMessage());
            throw e;
        }
        
        return tasks;
    }
    
    @Override
    public List<Task> getTodayTasks(int userId) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE user_id = ? AND due_date = CURRENT_DATE " +
                    "AND status != 'COMPLETED' ORDER BY priority DESC";
        return getTasksList(userId, sql);
    }
    
    @Override
    public List<Task> getOverdueTasks(int userId) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE user_id = ? AND due_date < CURRENT_DATE " +
                    "AND status != 'COMPLETED' ORDER BY due_date";
        return getTasksList(userId, sql);
    }
    
    @Override
    public List<Task> getUpcomingTasks(int userId, int days) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE user_id = ? " +
                    "AND due_date BETWEEN CURRENT_DATE AND (CURRENT_DATE + ? DAY) " +
                    "AND status != 'COMPLETED' ORDER BY due_date";
        
        List<Task> tasks = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, days);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting upcoming tasks: " + e.getMessage());
            throw e;
        }
        
        return tasks;
    }
    
    @Override
    public Task getTaskById(int taskId) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE task_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, taskId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractTaskFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting task by ID: " + e.getMessage());
            throw e;
        }
        
        return null;
    }
    
    @Override
    public boolean updateTask(Task task) throws SQLException {
        String sql = "UPDATE tasks SET title = ?, description = ?, due_date = ?, " +
                    "priority = ?, status = ?, updated_at = CURRENT_TIMESTAMP " +
                    "WHERE task_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setDate(3, task.getDueDate());
            pstmt.setString(4, task.getPriority());
            pstmt.setString(5, task.getStatus());
            pstmt.setInt(6, task.getTaskId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating task: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public boolean markAsCompleted(int taskId) throws SQLException {
        String sql = "UPDATE tasks SET status = 'COMPLETED', completed_at = CURRENT_TIMESTAMP, " +
                    "updated_at = CURRENT_TIMESTAMP WHERE task_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, taskId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error marking task as completed: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public boolean deleteTask(int taskId) throws SQLException {
        String sql = "DELETE FROM tasks WHERE task_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, taskId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting task: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public int getTaskCountByStatus(int userId, String status) throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM tasks WHERE user_id = ? AND status = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, status);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting task count: " + e.getMessage());
            throw e;
        }
        
        return 0;
    }
    
    /**
     * Helper method to get list of tasks
     */
    private List<Task> getTasksList(int userId, String sql) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting tasks: " + e.getMessage());
            throw e;
        }
        
        return tasks;
    }
    
    /**
     * Helper method to extract Task from ResultSet
     */
    private Task extractTaskFromResultSet(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setTaskId(rs.getInt("task_id"));
        task.setUserId(rs.getInt("user_id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setDueDate(rs.getDate("due_date"));
        task.setPriority(rs.getString("priority"));
        task.setStatus(rs.getString("status"));
        task.setCompletedAt(rs.getTimestamp("completed_at"));
        task.setCreatedAt(rs.getTimestamp("created_at"));
        task.setUpdatedAt(rs.getTimestamp("updated_at"));
        return task;
    }
}