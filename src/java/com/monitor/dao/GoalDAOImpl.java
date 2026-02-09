package com.monitor.dao;

import com.monitor.model.Goal;
import com.monitor.model.Milestone;
import com.monitor.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GoalDAOImpl implements GoalDAO {
    
    @Override
    public boolean createGoal(Goal goal) throws SQLException {
        String sql = "INSERT INTO goals (user_id, category_id, title, description, target_date, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, goal.getUserId());
            if (goal.getCategoryId() != null) {
                pstmt.setInt(2, goal.getCategoryId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setString(3, goal.getTitle());
            pstmt.setString(4, goal.getDescription());
            pstmt.setDate(5, goal.getTargetDate());
            pstmt.setString(6, goal.getStatus());
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public List<Goal> getAllGoals(int userId) throws SQLException {
        String sql = "SELECT g.*, c.category_name FROM goals g LEFT JOIN categories c ON g.category_id = c.category_id WHERE g.user_id = ? ORDER BY g.created_at DESC";
        List<Goal> goals = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Goal goal = extractGoal(rs);
                goal.setCompletionPercentage(getCompletionPercentage(goal.getGoalId()));
                goals.add(goal);
            }
        }
        return goals;
    }
    
    @Override
    public List<Goal> getGoalsByStatus(int userId, String status) throws SQLException {
        String sql = "SELECT g.*, c.category_name FROM goals g LEFT JOIN categories c ON g.category_id = c.category_id WHERE g.user_id = ? AND g.status = ? ORDER BY g.target_date";
        List<Goal> goals = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, status);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Goal goal = extractGoal(rs);
                goal.setCompletionPercentage(getCompletionPercentage(goal.getGoalId()));
                goals.add(goal);
            }
        }
        return goals;
    }
    
    @Override
    public Goal getGoalById(int goalId) throws SQLException {
        String sql = "SELECT g.*, c.category_name FROM goals g LEFT JOIN categories c ON g.category_id = c.category_id WHERE g.goal_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, goalId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Goal goal = extractGoal(rs);
                goal.setCompletionPercentage(getCompletionPercentage(goalId));
                return goal;
            }
        }
        return null;
    }
    
    @Override
    public boolean updateGoal(Goal goal) throws SQLException {
        String sql = "UPDATE goals SET title = ?, description = ?, target_date = ?, status = ?, updated_at = CURRENT_TIMESTAMP WHERE goal_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, goal.getTitle());
            pstmt.setString(2, goal.getDescription());
            pstmt.setDate(3, goal.getTargetDate());
            pstmt.setString(4, goal.getStatus());
            pstmt.setInt(5, goal.getGoalId());
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean deleteGoal(int goalId) throws SQLException {
        String sql = "DELETE FROM goals WHERE goal_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, goalId);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean markAsAchieved(int goalId) throws SQLException {
        String sql = "UPDATE goals SET status = 'ACHIEVED', updated_at = CURRENT_TIMESTAMP WHERE goal_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, goalId);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public int getGoalCountByStatus(int userId, String status) throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM goals WHERE user_id = ? AND status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, status);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("count");
        }
        return 0;
    }
    
    @Override
    public boolean createMilestone(Milestone milestone) throws SQLException {
        String sql = "INSERT INTO milestones (goal_id, title, description, target_date, is_completed) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, milestone.getGoalId());
            pstmt.setString(2, milestone.getTitle());
            pstmt.setString(3, milestone.getDescription());
            pstmt.setDate(4, milestone.getTargetDate());
            pstmt.setInt(5, milestone.isCompleted() ? 1 : 0);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public List<Milestone> getMilestonesByGoal(int goalId) throws SQLException {
        String sql = "SELECT * FROM milestones WHERE goal_id = ? ORDER BY target_date";
        List<Milestone> milestones = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, goalId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                milestones.add(extractMilestone(rs));
            }
        }
        return milestones;
    }
    
    @Override
    public boolean markMilestoneComplete(int milestoneId) throws SQLException {
        String sql = "UPDATE milestones SET is_completed = 1, completed_at = CURRENT_TIMESTAMP WHERE milestone_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, milestoneId);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean deleteMilestone(int milestoneId) throws SQLException {
        String sql = "DELETE FROM milestones WHERE milestone_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, milestoneId);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public int getCompletionPercentage(int goalId) throws SQLException {
        String sql = "SELECT COUNT(*) as total, SUM(CASE WHEN is_completed = 1 THEN 1 ELSE 0 END) as completed FROM milestones WHERE goal_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, goalId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int total = rs.getInt("total");
                if (total == 0) return 0;
                int completed = rs.getInt("completed");
                return (completed * 100) / total;
            }
        }
        return 0;
    }
    
    private Goal extractGoal(ResultSet rs) throws SQLException {
        Goal goal = new Goal();
        goal.setGoalId(rs.getInt("goal_id"));
        goal.setUserId(rs.getInt("user_id"));
        goal.setCategoryId((Integer) rs.getObject("category_id"));
        goal.setTitle(rs.getString("title"));
        goal.setDescription(rs.getString("description"));
        goal.setTargetDate(rs.getDate("target_date"));
        goal.setStatus(rs.getString("status"));
        goal.setCategoryName(rs.getString("category_name"));
        goal.setCreatedAt(rs.getTimestamp("created_at"));
        goal.setUpdatedAt(rs.getTimestamp("updated_at"));
        return goal;
    }
    
    private Milestone extractMilestone(ResultSet rs) throws SQLException {
        Milestone m = new Milestone();
        m.setMilestoneId(rs.getInt("milestone_id"));
        m.setGoalId(rs.getInt("goal_id"));
        m.setTitle(rs.getString("title"));
        m.setDescription(rs.getString("description"));
        m.setTargetDate(rs.getDate("target_date"));
        m.setCompleted(rs.getInt("is_completed") == 1);
        m.setCompletedAt(rs.getTimestamp("completed_at"));
        m.setCreatedAt(rs.getTimestamp("created_at"));
        m.setUpdatedAt(rs.getTimestamp("updated_at"));
        return m;
    }
}