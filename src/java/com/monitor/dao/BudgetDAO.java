package com.monitor.dao;

import com.monitor.model.Budget;
import java.sql.SQLException;
import java.util.List;

public interface BudgetDAO {
    boolean createOrUpdate(Budget budget) throws SQLException;
    boolean deleteBudget(int budgetId, int userId) throws SQLException;
    List<Budget> getBudgetsByMonth(int userId, int month, int year) throws SQLException;
}
