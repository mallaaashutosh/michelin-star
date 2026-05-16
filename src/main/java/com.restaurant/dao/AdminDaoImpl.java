package com.restaurant.dao;

import com.restaurant.dto.ChartSeries;
import com.restaurant.dto.DashboardAnalytics;
import com.restaurant.utils.DBConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminDaoImpl implements AdminDAO {

    @Override
    public Map<String, Integer> getDashboardStats() throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            return loadDashboardStats(conn);
        }
    }

    @Override
    public DashboardAnalytics getDashboardAnalytics() throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            Map<String, Integer> stats = loadDashboardStats(conn);

            double totalRevenue = 0.0;
            double averageSale = 0.0;
            int completedLines = 0;
            try {
                totalRevenue = sumDouble(conn,
                        "SELECT COALESCE(SUM(total_amount), 0) FROM orders WHERE status = 'completed'");
                averageSale = sumDouble(conn,
                        "SELECT COALESCE(AVG(total_amount), 0) FROM orders WHERE status = 'completed'");
                completedLines = count(conn, "SELECT COUNT(*) FROM orders WHERE status = 'completed'");
            } catch (SQLException e) {
                // orders table or column mismatch — keep KPIs at zero
            }

            ChartSeries last7 = ChartSeries.empty();
            ChartSeries statusMix = ChartSeries.empty();
            ChartSeries payMix = ChartSeries.empty();
            ChartSeries topItems = ChartSeries.empty();
            ChartSeries byCategory = ChartSeries.empty();

            try {
                last7 = loadOrdersLast7Days(conn);
            } catch (SQLException ignored) {
                // empty chart
            }
            try {
                statusMix = loadGroupedCount(conn,
                        "SELECT status, COUNT(*) AS c FROM orders GROUP BY status ORDER BY c DESC");
            } catch (SQLException ignored) {
            }
            try {
                payMix = loadGroupedCount(conn,
                        "SELECT COALESCE(NULLIF(TRIM(payment_method), ''), 'unknown') AS g, COUNT(*) AS c "
                                + "FROM orders GROUP BY COALESCE(NULLIF(TRIM(payment_method), ''), 'unknown') "
                                + "ORDER BY c DESC");
            } catch (SQLException ignored) {
            }
            try {
                topItems = loadTopMenuByRevenue(conn);
            } catch (SQLException ignored) {
            }
            try {
                byCategory = loadSalesByCategory(conn);
            } catch (SQLException ignored) {
            }

            return new DashboardAnalytics(stats,
                    totalRevenue,
                    averageSale,
                    completedLines,
                    last7,
                    statusMix,
                    payMix,
                    topItems,
                    byCategory);
        }
    }

    private Map<String, Integer> loadDashboardStats(Connection conn) throws SQLException {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("menuItems", count(conn, "SELECT COUNT(*) FROM menu"));
        stats.put("customers", count(conn, "SELECT COUNT(*) FROM user WHERE role = 'customer'"));
        stats.put("admins", count(conn, "SELECT COUNT(*) FROM user WHERE role = 'admin'"));
        stats.put("pendingUsers", count(conn, "SELECT COUNT(*) FROM user WHERE status = 'pending'"));
        stats.put("pendingOrders", safeCount(conn, "SELECT COUNT(*) FROM orders WHERE status = 'pending'"));
        stats.put("totalOrders", safeCount(conn, "SELECT COUNT(*) FROM orders"));
        return stats;
    }

    private ChartSeries loadOrdersLast7Days(Connection conn) throws SQLException {
        LocalDate end = LocalDate.now(ZoneId.systemDefault());
        LocalDate start = end.minusDays(6);
        Map<LocalDate, Integer> byDay = new LinkedHashMap<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            byDay.put(d, 0);
        }

        String sql = "SELECT DATE(created_at) AS d, COUNT(*) AS c FROM orders "
                + "WHERE created_at >= ? GROUP BY DATE(created_at)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date sqlD = rs.getDate("d");
                    if (sqlD != null) {
                        LocalDate ld = sqlD.toLocalDate();
                        if (byDay.containsKey(ld)) {
                            byDay.put(ld, rs.getInt("c"));
                        }
                    }
                }
            }
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH);
        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> e : byDay.entrySet()) {
            labels.add(e.getKey().format(fmt));
            values.add(e.getValue().doubleValue());
        }
        return new ChartSeries(labels, values);
    }

    private ChartSeries loadGroupedCount(Connection conn, String sql) throws SQLException {
        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String label = rs.getString(1);
                labels.add(label != null ? label : "—");
                values.add((double) rs.getInt(2));
            }
        }
        return new ChartSeries(labels, values);
    }

    private ChartSeries loadTopMenuByRevenue(Connection conn) throws SQLException {
        String sql = "SELECT menu_name, COALESCE(SUM(total_amount), 0) AS rev FROM orders "
                + "GROUP BY menu_id, menu_name ORDER BY rev DESC LIMIT 5";
        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                labels.add(rs.getString("menu_name"));
                values.add(rs.getDouble("rev"));
            }
        }
        return new ChartSeries(labels, values);
    }

    private ChartSeries loadSalesByCategory(Connection conn) throws SQLException {
        String sql = "SELECT m.category, COALESCE(SUM(o.total_amount), 0) AS rev "
                + "FROM orders o INNER JOIN menu m ON o.menu_id = m.menu_id "
                + "GROUP BY m.category ORDER BY rev DESC";
        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                labels.add(rs.getString("category"));
                values.add(rs.getDouble("rev"));
            }
        }
        return new ChartSeries(labels, values);
    }

    private int count(Connection conn, String query) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private int safeCount(Connection conn, String query) {
        try {
            return count(conn, query);
        } catch (SQLException e) {
            return 0;
        }
    }

    private double sumDouble(Connection conn, String query) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0.0;
    }
}
