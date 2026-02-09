package com.monitor.dao;

import com.monitor.model.Goal;
import com.monitor.model.Milestone;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for Goal operations
 */
public interface GoalDAO {
    boolean createGoal(Goal goal) throws SQLException;
    List<Goal> getAllGoals(int userId) throws SQLException;
    List<Goal> getGoalsByStatus(int userId, String status) throws SQLException;
    Goal getGoalById(int goalId) throws SQLException;
    boolean updateGoal(Goal goal) throws SQLException;
    boolean deleteGoal(int goalId) throws SQLException;
    boolean markAsAchieved(int goalId) throws SQLException;
    int getGoalCountByStatus(int userId, String status) throws SQLException;
    
    // Milestone operations
    boolean createMilestone(Milestone milestone) throws SQLException;
    List<Milestone> getMilestonesByGoal(int goalId) throws SQLException;
    boolean markMilestoneComplete(int milestoneId) throws SQLException;
    boolean deleteMilestone(int milestoneId) throws SQLException;
    int getCompletionPercentage(int goalId) throws SQLException;
}