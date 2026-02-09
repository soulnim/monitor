package com.monitor.dao;

import com.monitor.model.Bill;
import com.monitor.util.DBConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of BillDAO interface
 */
public class BillDAOImpl implements BillDAO {
    
    @Override
    public boolean createBill(Bill bill) throws SQLException {
        String sql = "INSERT INTO bills (user_id, bill_name, amount, due_date, category, " +
                    "status, is_recurring, recurring_frequency) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bill.getUserId());
            pstmt.setString(2, bill.getBillName());
            pstmt.setBigDecimal(3, bill.getAmount());
            pstmt.setDate(4, bill.getDueDate());
            pstmt.setString(5, bill.getCategory());
            pstmt.setString(6, bill.getStatus());
            pstmt.setInt(7, bill.isRecurring() ? 1 : 0);
            pstmt.setString(8, bill.getRecurringFrequency());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating bill: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public List<Bill> getAllBills(int userId) throws SQLException {
        String sql = "SELECT * FROM bills WHERE user_id = ? ORDER BY due_date";
        return getBillsList(userId, sql);
    }
    
    @Override
    public List<Bill> getBillsByStatus(int userId, String status) throws SQLException {
        String sql = "SELECT * FROM bills WHERE user_id = ? AND status = ? ORDER BY due_date";
        
        List<Bill> bills = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, status);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                bills.add(extractBillFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting bills by status: " + e.getMessage());
            throw e;
        }
        
        return bills;
    }
    
    @Override
    public List<Bill> getUpcomingBills(int userId, int days) throws SQLException {
        String sql = "SELECT * FROM bills WHERE user_id = ? " +
                     "AND due_date BETWEEN CURRENT_DATE AND ? " +
                     "AND status = 'UNPAID' ORDER BY due_date";

        List<Bill> bills = new ArrayList<>();

        LocalDate endDate = LocalDate.now().plusDays(days);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setDate(2, Date.valueOf(endDate));

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bills.add(extractBillFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error getting upcoming bills: " + e.getMessage());
            throw e;
        }

        return bills;
    }

    
    @Override
    public List<Bill> getOverdueBills(int userId) throws SQLException {
        String sql = "SELECT * FROM bills WHERE user_id = ? AND status = 'OVERDUE' " +
                    "ORDER BY due_date";
        return getBillsList(userId, sql);
    }
    
    @Override
    public Bill getBillById(int billId) throws SQLException {
        String sql = "SELECT * FROM bills WHERE bill_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, billId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractBillFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting bill by ID: " + e.getMessage());
            throw e;
        }
        
        return null;
    }
    
    @Override
    public boolean updateBill(Bill bill) throws SQLException {
        String sql = "UPDATE bills SET bill_name = ?, amount = ?, due_date = ?, " +
                    "category = ?, status = ?, is_recurring = ?, recurring_frequency = ?, " +
                    "updated_at = CURRENT_TIMESTAMP WHERE bill_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, bill.getBillName());
            pstmt.setBigDecimal(2, bill.getAmount());
            pstmt.setDate(3, bill.getDueDate());
            pstmt.setString(4, bill.getCategory());
            pstmt.setString(5, bill.getStatus());
            pstmt.setInt(6, bill.isRecurring() ? 1 : 0);
            pstmt.setString(7, bill.getRecurringFrequency());
            pstmt.setInt(8, bill.getBillId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating bill: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public boolean markAsPaid(int billId, Date paymentDate) throws SQLException {
        String sql = "UPDATE bills SET status = 'PAID', payment_date = ?, " +
                    "updated_at = CURRENT_TIMESTAMP WHERE bill_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, paymentDate);
            pstmt.setInt(2, billId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error marking bill as paid: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public boolean deleteBill(int billId) throws SQLException {
        String sql = "DELETE FROM bills WHERE bill_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, billId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting bill: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public int updateOverdueStatus(int userId) throws SQLException {
        String sql = "UPDATE bills SET status = 'OVERDUE' " +
                    "WHERE user_id = ? AND status = 'UNPAID' AND due_date < CURRENT_DATE";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error updating overdue status: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public BigDecimal getTotalUnpaidAmount(int userId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) as total FROM bills " +
                    "WHERE user_id = ? AND status IN ('UNPAID', 'OVERDUE')";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getBigDecimal("total");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total unpaid amount: " + e.getMessage());
            throw e;
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * Helper method to get list of bills
     */
    private List<Bill> getBillsList(int userId, String sql) throws SQLException {
        List<Bill> bills = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                bills.add(extractBillFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting bills: " + e.getMessage());
            throw e;
        }
        
        return bills;
    }
    
    /**
     * Helper method to extract Bill from ResultSet
     */
    private Bill extractBillFromResultSet(ResultSet rs) throws SQLException {
        Bill bill = new Bill();
        bill.setBillId(rs.getInt("bill_id"));
        bill.setUserId(rs.getInt("user_id"));
        bill.setBillName(rs.getString("bill_name"));
        bill.setAmount(rs.getBigDecimal("amount"));
        bill.setDueDate(rs.getDate("due_date"));
        bill.setCategory(rs.getString("category"));
        bill.setStatus(rs.getString("status"));
        bill.setPaymentDate(rs.getDate("payment_date"));
        bill.setRecurring(rs.getInt("is_recurring") == 1);
        bill.setRecurringFrequency(rs.getString("recurring_frequency"));
        bill.setCreatedAt(rs.getTimestamp("created_at"));
        bill.setUpdatedAt(rs.getTimestamp("updated_at"));
        return bill;
    }
}