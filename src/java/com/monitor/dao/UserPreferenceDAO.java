package com.monitor.dao;

import com.monitor.model.UserPreference;
import java.sql.SQLException;

public interface UserPreferenceDAO {
    void ensureTableExists() throws SQLException;
    UserPreference getByUserId(int userId) throws SQLException;
    boolean saveOrUpdate(UserPreference preference) throws SQLException;
}
