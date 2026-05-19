/*
 * Read-only admin dashboard data: headline counts and fuller analytics for charts.
 * Callers use this from staff servlets without writing SQL in the web layer.
 */
package com.restaurant.dao; // admin-specific persistence contracts live here

import com.restaurant.dto.DashboardAnalytics; // structured payload for charts and summaries
import java.sql.SQLException; // database errors bubble up to the servlet layer
import java.util.Map; // simple name-to-count map for quick stat tiles

public interface AdminDAO { // implemented by the JDBC class that queries orders and related tables

    Map<String, Integer> getDashboardStats() throws SQLException; // headline numbers like order counts

    DashboardAnalytics getDashboardAnalytics() throws SQLException; // richer breakdown for dashboard widgets
}
