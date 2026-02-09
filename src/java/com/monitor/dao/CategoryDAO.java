package com.monitor.dao;

import com.monitor.model.Category;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for Category operations
 */
public interface CategoryDAO {
    
    /**
     * Get all categories for a user by type
     * @param userId User ID
     * @param categoryType Category type (INCOME, EXPENSE, GOAL)
     * @return List of categories
     * @throws SQLException if database error occurs
     */
    List<Category> getCategoriesByType(int userId, String categoryType) throws SQLException;
    
    /**
     * Get all categories for a user
     * @param userId User ID
     * @return List of all categories
     * @throws SQLException if database error occurs
     */
    List<Category> getAllCategories(int userId) throws SQLException;
    
    /**
     * Get category by ID
     * @param categoryId Category ID
     * @return Category object if found, null otherwise
     * @throws SQLException if database error occurs
     */
    Category getCategoryById(int categoryId) throws SQLException;
    
    /**
     * Create a new category
     * @param category Category object
     * @return true if created successfully
     * @throws SQLException if database error occurs
     */
    boolean createCategory(Category category) throws SQLException;
    
    /**
     * Initialize default categories for a new user
     * @param userId User ID
     * @return true if initialized successfully
     * @throws SQLException if database error occurs
     */
    boolean initializeDefaultCategories(int userId) throws SQLException;
    
    /**
     * Delete a category
     * @param categoryId Category ID
     * @return true if deleted successfully
     * @throws SQLException if database error occurs
     */
    boolean deleteCategory(int categoryId) throws SQLException;
}