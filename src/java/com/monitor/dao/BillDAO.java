package com.monitor.dao;

import com.monitor.model.Bill;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for Bill operations
 */
public interface BillDAO {
    
    /**
     * Create a new bill
     * @param bill Bill object
     * @return true if created successfully
     * @throws SQLException if database error occurs
     */
    boolean createBill(Bill bill) throws SQLException;
    
    /**
     * Get all bills for a user
     * @param userId User ID
     * @return List of bills
     * @throws SQLException if database error occurs
     */
    List<Bill> getAllBills(int userId) throws SQLException;
    
    /**
     * Get bills by status
     * @param userId User ID
     * @param status Bill status (UNPAID, PAID, OVERDUE)
     * @return List of bills
     * @throws SQLException if database error occurs
     */
    List<Bill> getBillsByStatus(int userId, String status) throws SQLException;
    
    /**
     * Get upcoming bills (due within N days)
     * @param userId User ID
     * @param days Number of days
     * @return List of upcoming bills
     * @throws SQLException if database error occurs
     */
    List<Bill> getUpcomingBills(int userId, int days) throws SQLException;
    
    /**
     * Get overdue bills
     * @param userId User ID
     * @return List of overdue bills
     * @throws SQLException if database error occurs
     */
    List<Bill> getOverdueBills(int userId) throws SQLException;
    
    /**
     * Get bill by ID
     * @param billId Bill ID
     * @return Bill object if found, null otherwise
     * @throws SQLException if database error occurs
     */
    Bill getBillById(int billId) throws SQLException;
    
    /**
     * Update a bill
     * @param bill Bill object with updated values
     * @return true if updated successfully
     * @throws SQLException if database error occurs
     */
    boolean updateBill(Bill bill) throws SQLException;
    
    /**
     * Mark bill as paid
     * @param billId Bill ID
     * @param paymentDate Payment date
     * @return true if updated successfully
     * @throws SQLException if database error occurs
     */
    boolean markAsPaid(int billId, java.sql.Date paymentDate) throws SQLException;
    
    /**
     * Delete a bill
     * @param billId Bill ID
     * @return true if deleted successfully
     * @throws SQLException if database error occurs
     */
    boolean deleteBill(int billId) throws SQLException;
    
    /**
     * Update overdue bills status
     * @param userId User ID
     * @return Number of bills updated to overdue
     * @throws SQLException if database error occurs
     */
    int updateOverdueStatus(int userId) throws SQLException;
    
    /**
     * Get total unpaid bills amount
     * @param userId User ID
     * @return Total amount of unpaid bills
     * @throws SQLException if database error occurs
     */
    BigDecimal getTotalUnpaidAmount(int userId) throws SQLException;
}