package com.monitor.dao;

import com.monitor.model.Budget;
import com.monitor.util.DBConnection;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BudgetDAOImpl implements BudgetDAO {

    @Override
    public boolean createOrUpdate(Budget budget) throws SQLException {
        String updateSql = "UPDATE budgets SET budget_limit = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ? AND category_id = ? AND budget_month = ? AND budget_year = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
            pstmt.setBigDecimal(1, budget.getBudgetLimit());
            pstmt.setInt(2, budget.getUserId());
            pstmt.setInt(3, budget.getCategoryId());
            pstmt.setInt(4, budget.getBudgetMonth());
            pstmt.setInt(5, budget.getBudgetYear());
            int updated = pstmt.executeUpdate();
            if (updated > 0) {
                return true;
            }
        }

        String insertSql = "INSERT INTO budgets (user_id, category_id, budget_month, budget_year, budget_limit) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            pstmt.setInt(1, budget.getUserId());
            pstmt.setInt(2, budget.getCategoryId());
            pstmt.setInt(3, budget.getBudgetMonth());
            pstmt.setInt(4, budget.getBudgetYear());
            pstmt.setBigDecimal(5, budget.getBudgetLimit());
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteBudget(int budgetId, int userId) throws SQLException {
        String sql = "DELETE FROM budgets WHERE budget_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, budgetId);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<Budget> getBudgetsByMonth(int userId, int month, int year) throws SQLException {
        String sql = "SELECT b.*, c.category_name, COALESCE(SUM(t.amount), 0) as spent_amount "
                + "FROM budgets b "
                + "JOIN categories c ON b.category_id = c.category_id "
                + "LEFT JOIN transactions t ON t.user_id = b.user_id AND t.category_id = b.category_id "
                + "AND t.transaction_type = 'EXPENSE' AND MONTH(t.transaction_date) = b.budget_month AND YEAR(t.transaction_date) = b.budget_year "
                + "WHERE b.user_id = ? AND b.budget_month = ? AND b.budget_year = ? "
                + "GROUP BY b.budget_id, b.user_id, b.category_id, b.budget_month, b.budget_year, b.budget_limit, b.created_at, b.updated_at, c.category_name "
                + "ORDER BY c.category_name";

        List<Budget> budgets = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Budget budget = new Budget();
                budget.setBudgetId(rs.getInt("budget_id"));
                budget.setUserId(rs.getInt("user_id"));
                budget.setCategoryId(rs.getInt("category_id"));
                budget.setBudgetMonth(rs.getInt("budget_month"));
                budget.setBudgetYear(rs.getInt("budget_year"));
                budget.setBudgetLimit(rs.getBigDecimal("budget_limit"));
                budget.setCategoryName(rs.getString("category_name"));
                budget.setSpentAmount(rs.getBigDecimal("spent_amount"));
                budget.setCreatedAt(rs.getTimestamp("created_at"));
                budget.setUpdatedAt(rs.getTimestamp("updated_at"));
                budgets.add(budget);
            }
        }
        return budgets;
    }
}
