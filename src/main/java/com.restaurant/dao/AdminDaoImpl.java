/**
 * AdminDaoImpl.java
 *
 * JDBC implementation of AdminDAO — loads dashboard counts, revenue KPIs,
 * and chart series used on the admin analytics page.
 */
package com.restaurant.dao; // DAO package for database access classes

import com.restaurant.dto.ChartSeries; // Label + value pairs for Chart.js on the dashboard
import com.restaurant.dto.DashboardAnalytics; // Bundles stats, KPIs, and all chart series together
import com.restaurant.utils.DBConnection; // Shared helper to open a MySQL connection
import java.sql.Connection; // JDBC connection handle
import java.sql.Date; // SQL DATE for filtering orders by calendar day
import java.sql.PreparedStatement; // Parameterized SQL for counts and aggregates
import java.sql.ResultSet; // Cursor over aggregate and grouped query results
import java.sql.SQLException; // Propagated to servlet when a required query fails
import java.time.LocalDate; // Java date without time zone noise for chart buckets
import java.time.ZoneId; // System default zone when converting SQL dates
import java.time.format.DateTimeFormatter; // Formats axis labels like "May 19"
import java.util.ArrayList; // Parallel list of chart labels and values
import java.util.HashMap; // Key-value map for dashboard count tiles
import java.util.LinkedHashMap; // Preserves day order for the last-7-days chart
import java.util.List; // Generic list type for chart data
import java.util.Locale; // English month abbreviations on chart labels
import java.util.Map; // Return type for simple stat key → count maps

public class AdminDaoImpl implements AdminDAO { // Concrete DAO used by admin dashboard servlet

    @Override // Fulfills AdminDAO contract
    public Map<String, Integer> getDashboardStats() throws SQLException { // Tile counts only (menu, users, orders)
        try (Connection conn = DBConnection.getConnection()) { // Auto-close connection when block exits
            return loadDashboardStats(conn); // Delegate to shared loader on open connection
        }
    }

    @Override // Fulfills AdminDAO contract
    public DashboardAnalytics getDashboardAnalytics() throws SQLException { // Full dashboard payload with charts
        try (Connection conn = DBConnection.getConnection()) { // One connection for the whole analytics build
            Map<String, Integer> stats = loadDashboardStats(conn); // Base counts reused from stats endpoint

            double totalRevenue = 0.0; // Sum of completed order line totals
            double averageSale = 0.0; // Average line total among completed orders
            int completedLines = 0; // Row count where status is completed
            try { // Revenue KPIs are optional if orders schema differs
                totalRevenue = sumDouble(conn,
                        "SELECT COALESCE(SUM(total_amount), 0) FROM orders WHERE status = 'completed'"); // All completed revenue
                averageSale = sumDouble(conn,
                        "SELECT COALESCE(AVG(total_amount), 0) FROM orders WHERE status = 'completed'"); // Mean completed line
                completedLines = count(conn, "SELECT COUNT(*) FROM orders WHERE status = 'completed'"); // How many completed rows
            } catch (SQLException e) {
                // orders table or column mismatch — keep KPIs at zero
            }

            ChartSeries last7 = ChartSeries.empty(); // Orders per day for the last week
            ChartSeries statusMix = ChartSeries.empty(); // Pie-style breakdown by order status
            ChartSeries payMix = ChartSeries.empty(); // Breakdown by payment method
            ChartSeries topItems = ChartSeries.empty(); // Top five menu items by revenue
            ChartSeries byCategory = ChartSeries.empty(); // Revenue grouped by menu category

            try { // Each chart loads independently so one failure does not break the page
                last7 = loadOrdersLast7Days(conn); // Daily order counts
            } catch (SQLException ignored) {
                // empty chart
            }
            try {
                statusMix = loadGroupedCount(conn,
                        "SELECT status, COUNT(*) AS c FROM orders GROUP BY status ORDER BY c DESC"); // Status distribution
            } catch (SQLException ignored) {
            }
            try {
                payMix = loadGroupedCount(conn,
                        "SELECT COALESCE(NULLIF(TRIM(payment_method), ''), 'unknown') AS g, COUNT(*) AS c "
                                + "FROM orders GROUP BY COALESCE(NULLIF(TRIM(payment_method), ''), 'unknown') "
                                + "ORDER BY c DESC"); // Payment method distribution (blank → unknown)
            } catch (SQLException ignored) {
            }
            try {
                topItems = loadTopMenuByRevenue(conn); // Best sellers by summed total_amount
            } catch (SQLException ignored) {
            }
            try {
                byCategory = loadSalesByCategory(conn); // Join orders to menu for category revenue
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
                    byCategory); // Immutable bundle for JSP / JSON
        }
    }

    private Map<String, Integer> loadDashboardStats(Connection conn) throws SQLException { // Shared stat tiles logic
        Map<String, Integer> stats = new HashMap<>(); // Keys match what the dashboard JSP expects
        stats.put("menuItems", count(conn, "SELECT COUNT(*) FROM menu")); // Total dishes on the menu
        stats.put("customers", count(conn, "SELECT COUNT(*) FROM user WHERE role = 'customer'")); // Registered customers
        stats.put("admins", count(conn, "SELECT COUNT(*) FROM user WHERE role = 'admin'")); // Admin accounts
        stats.put("pendingUsers", count(conn, "SELECT COUNT(*) FROM user WHERE status = 'pending'")); // Awaiting approval
        stats.put("pendingOrders", safeCount(conn, "SELECT COUNT(*) FROM orders WHERE status = 'pending'")); // Open orders (0 if table missing)
        stats.put("totalOrders", safeCount(conn, "SELECT COUNT(*) FROM orders")); // All order rows ever
        return stats; // Map consumed by dashboard tiles
    }

    private ChartSeries loadOrdersLast7Days(Connection conn) throws SQLException { // Line/bar chart for recent volume
        LocalDate end = LocalDate.now(ZoneId.systemDefault()); // Today in server timezone
        LocalDate start = end.minusDays(6); // Inclusive seven-day window
        Map<LocalDate, Integer> byDay = new LinkedHashMap<>(); // Every day pre-filled with zero
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) { // Walk each calendar day
            byDay.put(d, 0); // Placeholder until query overwrites with real count
        }

        String sql = "SELECT DATE(created_at) AS d, COUNT(*) AS c FROM orders "
                + "WHERE created_at >= ? GROUP BY DATE(created_at)"; // Aggregate orders since start date
        try (PreparedStatement ps = conn.prepareStatement(sql)) { // Bind start as SQL DATE
            ps.setDate(1, Date.valueOf(start)); // Lower bound for created_at filter
            try (ResultSet rs = ps.executeQuery()) { // One row per day that had orders
                while (rs.next()) { // Merge DB counts into our fixed day map
                    Date sqlD = rs.getDate("d"); // Day bucket from MySQL
                    if (sqlD != null) { // Skip null dates defensively
                        LocalDate ld = sqlD.toLocalDate(); // Convert to java.time
                        if (byDay.containsKey(ld)) { // Only count days inside the 7-day window
                            byDay.put(ld, rs.getInt("c")); // Replace zero with actual count
                        }
                    }
                }
            }
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH); // Short label for chart axis
        List<String> labels = new ArrayList<>(); // Parallel to values for Chart.js
        List<Double> values = new ArrayList<>(); // Order counts as doubles for chart API
        for (Map.Entry<LocalDate, Integer> e : byDay.entrySet()) { // Stable iteration order from LinkedHashMap
            labels.add(e.getKey().format(fmt)); // e.g. "May 13"
            values.add(e.getValue().doubleValue()); // Count as double for chart library
        }
        return new ChartSeries(labels, values); // Series ready for the front end
    }

    private ChartSeries loadGroupedCount(Connection conn, String sql) throws SQLException { // Generic GROUP BY → chart
        List<String> labels = new ArrayList<>(); // Group key (status, payment method, etc.)
        List<Double> values = new ArrayList<>(); // Matching counts
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) { // Caller supplies full GROUP BY SQL
            while (rs.next()) { // One slice per group
                String label = rs.getString(1); // First column is the group name
                labels.add(label != null ? label : "—"); // Fallback label for null group keys
                values.add((double) rs.getInt(2)); // Second column is COUNT(*)
            }
        }
        return new ChartSeries(labels, values); // Pie/doughnut friendly series
    }

    private ChartSeries loadTopMenuByRevenue(Connection conn) throws SQLException { // Horizontal bar of best sellers
        String sql = "SELECT menu_name, COALESCE(SUM(total_amount), 0) AS rev FROM orders "
                + "GROUP BY menu_id, menu_name ORDER BY rev DESC LIMIT 5"; // Top five by summed line revenue
        List<String> labels = new ArrayList<>(); // Dish names
        List<Double> values = new ArrayList<>(); // Revenue per dish
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                labels.add(rs.getString("menu_name")); // Display name on chart
                values.add(rs.getDouble("rev")); // Total revenue for that menu_id/name pair
            }
        }
        return new ChartSeries(labels, values);
    }

    private ChartSeries loadSalesByCategory(Connection conn) throws SQLException { // Revenue rolled up by menu category
        String sql = "SELECT m.category, COALESCE(SUM(o.total_amount), 0) AS rev "
                + "FROM orders o INNER JOIN menu m ON o.menu_id = m.menu_id "
                + "GROUP BY m.category ORDER BY rev DESC"; // Join ties each order line to its category
        List<String> labels = new ArrayList<>(); // Category names (Nepali, Chinese, …)
        List<Double> values = new ArrayList<>(); // Sum of total_amount per category
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                labels.add(rs.getString("category")); // Category label
                values.add(rs.getDouble("rev")); // Category revenue
            }
        }
        return new ChartSeries(labels, values);
    }

    private int count(Connection conn, String query) throws SQLException { // Run SELECT COUNT(*) and return first column
        try (PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) { // Single-row aggregate expected
            return rs.next() ? rs.getInt(1) : 0; // Zero when result set is empty
        }
    }

    private int safeCount(Connection conn, String query) { // Same as count but never throws to caller
        try {
            return count(conn, query); // Delegate to strict count
        } catch (SQLException e) {
            return 0; // Missing orders table or bad column → treat as zero
        }
    }

    private double sumDouble(Connection conn, String query) throws SQLException { // SUM or AVG returning one double
        try (PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) { // Aggregate queries always return one row when successful
                return rs.getDouble(1); // First column is the numeric result
            }
        }
        return 0.0; // No row → treat as zero revenue/average
    }
} // end AdminDaoImpl
