package com.monitor.dao;

import com.monitor.model.Category;
import com.monitor.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of CategoryDAO interface
 */
public class CategoryDAOImpl implements CategoryDAO {
    
    @Override
    public List<Category> getCategoriesByType(int userId, String categoryType) throws SQLException {
        String sql = "SELECT * FROM categories WHERE user_id = ? AND category_type = ? ORDER BY category_name";
        List<Category> categories = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, categoryType);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                categories.add(extractCategoryFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting categories by type: " + e.getMessage());
            throw e;
        }
        
        return categories;
    }
    
    @Override
    public List<Category> getAllCategories(int userId) throws SQLException {
        String sql = "SELECT * FROM categories WHERE user_id = ? ORDER BY category_type, category_name";
        List<Category> categories = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                categories.add(extractCategoryFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all categories: " + e.getMessage());
            throw e;
        }
        
        return categories;
    }
    
    @Override
    public Category getCategoryById(int categoryId) throws SQLException {
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, categoryId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractCategoryFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting category by ID: " + e.getMessage());
            throw e;
        }
        
        return null;
    }
    
    @Override
    public boolean createCategory(Category category) throws SQLException {
        String sql = "INSERT INTO categories (user_id, category_name, category_type, is_default) " +
                    "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, category.getUserId());
            pstmt.setString(2, category.getCategoryName());
            pstmt.setString(3, category.getCategoryType());
            pstmt.setInt(4, category.isDefault() ? 1 : 0);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating category: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public boolean initializeDefaultCategories(int userId) throws SQLException {
        String sql = "INSERT INTO categories (user_id, category_name, category_type, is_default) " +
                    "VALUES (?, ?, ?, 1)";
        
        // Default categories
        String[][] defaultCategories = {
            // Income categories
            {"Salary", "INCOME"},
            {"Business", "INCOME"},
            {"Investment", "INCOME"},
            {"Other Income", "INCOME"},
            // Expense categories
            {"Food & Dining", "EXPENSE"},
            {"Transportation", "EXPENSE"},
            {"Shopping", "EXPENSE"},
            {"Entertainment", "EXPENSE"},
            {"Bills & Utilities", "EXPENSE"},
            {"Healthcare", "EXPENSE"},
            {"Education", "EXPENSE"},
            {"Other Expense", "EXPENSE"},
            // Goal categories
            {"Financial", "GOAL"},
            {"Health", "GOAL"},
            {"Career", "GOAL"},
            {"Personal", "GOAL"}
        };
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (String[] cat : defaultCategories) {
                pstmt.setInt(1, userId);
                pstmt.setString(2, cat[0]);
                pstmt.setString(3, cat[1]);
                pstmt.addBatch();
            }
            
            int[] results = pstmt.executeBatch();
            return results.length == defaultCategories.length;
            
        } catch (SQLException e) {
            System.err.println("Error initializing default categories: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public boolean deleteCategory(int categoryId) throws SQLException {
        String sql = "DELETE FROM categories WHERE category_id = ? AND is_default = 0";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, categoryId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting category: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Helper method to extract Category from ResultSet
     */
    private Category extractCategoryFromResultSet(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setCategoryId(rs.getInt("category_id"));
        category.setUserId(rs.getInt("user_id"));
        category.setCategoryName(rs.getString("category_name"));
        category.setCategoryType(rs.getString("category_type"));
        category.setDefault(rs.getInt("is_default") == 1);
        category.setCreatedAt(rs.getTimestamp("created_at"));
        return category;
    }
}