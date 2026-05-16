package com.restaurant.dao;

import com.restaurant.dto.DashboardAnalytics;
import java.sql.SQLException;
import java.util.Map;

public interface AdminDAO {

    Map<String, Integer> getDashboardStats() throws SQLException;

    DashboardAnalytics getDashboardAnalytics() throws SQLException;
}
