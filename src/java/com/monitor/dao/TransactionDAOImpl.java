package com.monitor.dao;

import com.monitor.model.Transaction;
import com.monitor.util.DBConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of TransactionDAO interface
 */
public class TransactionDAOImpl implements TransactionDAO {
    
    @Override
    public boolean createTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (user_id, category_id, amount, transaction_type, " +
                    "transaction_date, description, is_recurring, recurring_frequency) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, transaction.getUserId());
            pstmt.setInt(2, transaction.getCategoryId());
            pstmt.setBigDecimal(3, transaction.getAmount());
            pstmt.setString(4, transaction.getTransactionType());
            pstmt.setDate(5, transaction.getTransactionDate());
            pstmt.setString(6, transaction.getDescription());
            pstmt.setInt(7, transaction.isRecurring() ? 1 : 0);
            pstmt.setString(8, transaction.getRecurringFrequency());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating transaction: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public List<Transaction> getAllTransactions(int userId) throws SQLException {
        String sql = "SELECT t.*, c.category_name FROM transactions t " +
                    "LEFT JOIN categories c ON t.category_id = c.category_id " +
                    "WHERE t.user_id = ? ORDER BY t.transaction_date DESC";
        
        return getTransactionsList(userId, sql);
    }
    
    @Override
    public List<Transaction> getTransactionsByType(int userId, String type) throws SQLException {
        String sql = "SELECT t.*, c.category_name FROM transactions t " +
                    "LEFT JOIN categories c ON t.category_id = c.category_id " +
                    "WHERE t.user_id = ? AND t.transaction_type = ? " +
                    "ORDER BY t.transaction_date DESC";
        
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, type);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(extractTransactionFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting transactions by type: " + e.getMessage());
            throw e;
        }
        
        return transactions;
    }
    
    @Override
    public List<Transaction> getTransactionsByMonth(int userId, int month, int year) throws SQLException {
        String sql = "SELECT t.*, c.category_name FROM transactions t " +
                    "LEFT JOIN categories c ON t.category_id = c.category_id " +
                    "WHERE t.user_id = ? AND MONTH(t.transaction_date) = ? " +
                    "AND YEAR(t.transaction_date) = ? " +
                    "ORDER BY t.transaction_date DESC";
        
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(extractTransactionFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting transactions by month: " + e.getMessage());
            throw e;
        }
        
        return transactions;
    }
    
    @Override
    public List<Transaction> getTransactionsByDateRange(int userId, Date startDate, Date endDate) 
            throws SQLException {
        String sql = "SELECT t.*, c.category_name FROM transactions t " +
                    "LEFT JOIN categories c ON t.category_id = c.category_id " +
                    "WHERE t.user_id = ? AND t.transaction_date BETWEEN ? AND ? " +
                    "ORDER BY t.transaction_date DESC";
        
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setDate(2, startDate);
            pstmt.setDate(3, endDate);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(extractTransactionFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting transactions by date range: " + e.getMessage());
            throw e;
        }
        
        return transactions;
    }
    
    @Override
    public Transaction getTransactionById(int transactionId) throws SQLException {
        String sql = "SELECT t.*, c.category_name FROM transactions t " +
                    "LEFT JOIN categories c ON t.category_id = c.category_id " +
                    "WHERE t.transaction_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, transactionId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractTransactionFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting transaction by ID: " + e.getMessage());
            throw e;
        }
        
        return null;
    }
    
    @Override
    public boolean updateTransaction(Transaction transaction) throws SQLException {
        String sql = "UPDATE transactions SET category_id = ?, amount = ?, transaction_type = ?, " +
                    "transaction_date = ?, description = ?, is_recurring = ?, " +
                    "recurring_frequency = ?, updated_at = CURRENT_TIMESTAMP " +
                    "WHERE transaction_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, transaction.getCategoryId());
            pstmt.setBigDecimal(2, transaction.getAmount());
            pstmt.setString(3, transaction.getTransactionType());
            pstmt.setDate(4, transaction.getTransactionDate());
            pstmt.setString(5, transaction.getDescription());
            pstmt.setInt(6, transaction.isRecurring() ? 1 : 0);
            pstmt.setString(7, transaction.getRecurringFrequency());
            pstmt.setInt(8, transaction.getTransactionId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating transaction: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public boolean deleteTransaction(int transactionId) throws SQLException {
        String sql = "DELETE FROM transactions WHERE transaction_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, transactionId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting transaction: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public BigDecimal getTotalIncome(int userId, int month, int year) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) as total FROM transactions " +
                    "WHERE user_id = ? AND transaction_type = 'INCOME' " +
                    "AND MONTH(transaction_date) = ? AND YEAR(transaction_date) = ?";
        
        return getTotalAmount(userId, month, year, sql);
    }
    
    @Override
    public BigDecimal getTotalExpenses(int userId, int month, int year) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) as total FROM transactions " +
                    "WHERE user_id = ? AND transaction_type = 'EXPENSE' " +
                    "AND MONTH(transaction_date) = ? AND YEAR(transaction_date) = ?";
        
        return getTotalAmount(userId, month, year, sql);
    }
    
    @Override
    public Map<String, BigDecimal> getExpensesByCategory(int userId, int month, int year) 
            throws SQLException {
        String sql = "SELECT c.category_name, COALESCE(SUM(t.amount), 0) as total " +
                    "FROM transactions t " +
                    "LEFT JOIN categories c ON t.category_id = c.category_id " +
                    "WHERE t.user_id = ? AND t.transaction_type = 'EXPENSE' " +
                    "AND MONTH(t.transaction_date) = ? AND YEAR(t.transaction_date) = ? " +
                    "GROUP BY c.category_name " +
                    "ORDER BY total DESC";
        
        Map<String, BigDecimal> expensesByCategory = new LinkedHashMap<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String categoryName = rs.getString("category_name");
                BigDecimal total = rs.getBigDecimal("total");
                expensesByCategory.put(categoryName, total);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting expenses by category: " + e.getMessage());
            throw e;
        }
        
        return expensesByCategory;
    }
    
    @Override
    public List<Transaction> getRecentTransactions(int userId, int limit) throws SQLException {
        String sql = "SELECT t.*, c.category_name FROM transactions t " +
                    "LEFT JOIN categories c ON t.category_id = c.category_id " +
                    "WHERE t.user_id = ? ORDER BY t.transaction_date DESC " +
                    "FETCH FIRST ? ROWS ONLY";
        
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(extractTransactionFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting recent transactions: " + e.getMessage());
            throw e;
        }
        
        return transactions;
    }
    
    /**
     * Helper method to get list of transactions
     */
    private List<Transaction> getTransactionsList(int userId, String sql) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(extractTransactionFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting transactions: " + e.getMessage());
            throw e;
        }
        
        return transactions;
    }
    
    /**
     * Helper method to get total amount
     */
    private BigDecimal getTotalAmount(int userId, int month, int year, String sql) 
            throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getBigDecimal("total");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total amount: " + e.getMessage());
            throw e;
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * Helper method to extract Transaction from ResultSet
     */
    private Transaction extractTransactionFromResultSet(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getInt("transaction_id"));
        transaction.setUserId(rs.getInt("user_id"));
        transaction.setCategoryId(rs.getInt("category_id"));
        transaction.setAmount(rs.getBigDecimal("amount"));
        transaction.setTransactionType(rs.getString("transaction_type"));
        transaction.setTransactionDate(rs.getDate("transaction_date"));
        transaction.setDescription(rs.getString("description"));
        transaction.setRecurring(rs.getInt("is_recurring") == 1);
        transaction.setRecurringFrequency(rs.getString("recurring_frequency"));
        transaction.setCreatedAt(rs.getTimestamp("created_at"));
        transaction.setUpdatedAt(rs.getTimestamp("updated_at"));
        transaction.setCategoryName(rs.getString("category_name"));
        return transaction;
    }
}