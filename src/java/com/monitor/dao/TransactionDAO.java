package com.monitor.dao;

import com.monitor.model.Transaction;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object interface for Transaction operations
 */
public interface TransactionDAO {
    
    /**
     * Create a new transaction
     * @param transaction Transaction object
     * @return true if created successfully
     * @throws SQLException if database error occurs
     */
    boolean createTransaction(Transaction transaction) throws SQLException;
    
    /**
     * Get all transactions for a user
     * @param userId User ID
     * @return List of transactions
     * @throws SQLException if database error occurs
     */
    List<Transaction> getAllTransactions(int userId) throws SQLException;
    
    /**
     * Get transactions by type (INCOME or EXPENSE)
     * @param userId User ID
     * @param type Transaction type
     * @return List of transactions
     * @throws SQLException if database error occurs
     */
    List<Transaction> getTransactionsByType(int userId, String type) throws SQLException;
    
    /**
     * Get transactions for a specific month and year
     * @param userId User ID
     * @param month Month (1-12)
     * @param year Year
     * @return List of transactions
     * @throws SQLException if database error occurs
     */
    List<Transaction> getTransactionsByMonth(int userId, int month, int year) throws SQLException;
    
    /**
     * Get transactions by date range
     * @param userId User ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of transactions
     * @throws SQLException if database error occurs
     */
    List<Transaction> getTransactionsByDateRange(int userId, Date startDate, Date endDate) throws SQLException;
    
    /**
     * Get transaction by ID
     * @param transactionId Transaction ID
     * @return Transaction object if found, null otherwise
     * @throws SQLException if database error occurs
     */
    Transaction getTransactionById(int transactionId) throws SQLException;
    
    /**
     * Update a transaction
     * @param transaction Transaction object with updated values
     * @return true if updated successfully
     * @throws SQLException if database error occurs
     */
    boolean updateTransaction(Transaction transaction) throws SQLException;
    
    /**
     * Delete a transaction
     * @param transactionId Transaction ID
     * @return true if deleted successfully
     * @throws SQLException if database error occurs
     */
    boolean deleteTransaction(int transactionId) throws SQLException;
    
    /**
     * Get total income for a month
     * @param userId User ID
     * @param month Month (1-12)
     * @param year Year
     * @return Total income amount
     * @throws SQLException if database error occurs
     */
    BigDecimal getTotalIncome(int userId, int month, int year) throws SQLException;
    
    /**
     * Get total expenses for a month
     * @param userId User ID
     * @param month Month (1-12)
     * @param year Year
     * @return Total expenses amount
     * @throws SQLException if database error occurs
     */
    BigDecimal getTotalExpenses(int userId, int month, int year) throws SQLException;
    
    /**
     * Get expenses by category for a month
     * @param userId User ID
     * @param month Month (1-12)
     * @param year Year
     * @return Map of category name to total amount
     * @throws SQLException if database error occurs
     */
    Map<String, BigDecimal> getExpensesByCategory(int userId, int month, int year) throws SQLException;
    
    /**
     * Get recent transactions (last N transactions)
     * @param userId User ID
     * @param limit Number of transactions to retrieve
     * @return List of recent transactions
     * @throws SQLException if database error occurs
     */
    List<Transaction> getRecentTransactions(int userId, int limit) throws SQLException;
}