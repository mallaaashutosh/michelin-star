package com.restaurant.dao;

import com.restaurant.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AdminDaoImpl implements AdminDAO {

    @Override
    public Map<String, Integer> getDashboardStats() throws SQLException {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("menuItems", count("SELECT COUNT(*) FROM menu"));
        stats.put("customers", count("SELECT COUNT(*) FROM user WHERE role = 'customer'"));
        stats.put("admins", count("SELECT COUNT(*) FROM user WHERE role = 'admin'"));
        stats.put("pendingUsers", count("SELECT COUNT(*) FROM user WHERE status = 'pending'"));
        stats.put("pendingOrders", safeCount("SELECT COUNT(*) FROM orders WHERE status = 'pending'"));
        stats.put("totalOrders", safeCount("SELECT COUNT(*) FROM orders"));
        return stats;
    }

    private int count(String query) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private int safeCount(String query) {
        try {
            return count(query);
        } catch (SQLException e) {
            return 0;
        }
    }
}
