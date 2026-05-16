package com.restaurant.controller;

import com.restaurant.dao.AdminDAO;
import com.restaurant.dao.AdminDaoImpl;
import com.restaurant.dto.ChartSeries;
import com.restaurant.dto.DashboardAnalytics;
import com.restaurant.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/dashboard")
public class DashboardServlet extends HttpServlet {

    private final AdminDAO adminDAO = new AdminDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;

        if (user == null || !user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            DashboardAnalytics analytics = adminDAO.getDashboardAnalytics();
            request.setAttribute("analytics", analytics);
            request.setAttribute("stats", analytics.getStats());
            request.setAttribute("insights", buildInsights(analytics));
            request.setAttribute("adminUser", user);
            request.getRequestDispatcher("/views/admin/dashboard.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Could not load dashboard data.");
            request.setAttribute("analytics", emptyAnalytics());
            request.setAttribute("stats", Collections.emptyMap());
            request.setAttribute("insights", List.of("Analytics could not be loaded. Check the database connection."));
            request.setAttribute("adminUser", user);
            request.getRequestDispatcher("/views/admin/dashboard.jsp").forward(request, response);
        }
    }

    private static DashboardAnalytics emptyAnalytics() {
        return new DashboardAnalytics(
                Collections.emptyMap(),
                0,
                0,
                0,
                ChartSeries.empty(),
                ChartSeries.empty(),
                ChartSeries.empty(),
                ChartSeries.empty(),
                ChartSeries.empty());
    }

    private static List<String> buildInsights(DashboardAnalytics a) {
        List<String> insights = new ArrayList<>();
        Map<String, Integer> stats = a.getStats();
        int totalOrders = stats.getOrDefault("totalOrders", 0);
        int pendingOrders = stats.getOrDefault("pendingOrders", 0);
        int pendingUsers = stats.getOrDefault("pendingUsers", 0);

        if (totalOrders == 0) {
            insights.add("No orders recorded yet. Revenue and trend charts will fill in as guests place orders.");
        } else {
            double pendingShare = 100.0 * pendingOrders / totalOrders;
            if (pendingOrders > 0) {
                insights.add(String.format(
                        "%d order line(s) are pending (%.0f%% of all lines). Prioritize preparation or status updates when busy.",
                        pendingOrders, pendingShare));
            }
            if (a.getTotalRevenue() > 0) {
                insights.add(String.format(
                        "Reported revenue from completed lines: total %,.2f with an average line value of %,.2f.",
                        a.getTotalRevenue(), a.getAverageSaleAmount()));
            }
            if (!a.getSalesByCategory().getLabels().isEmpty()) {
                int topIdx = 0;
                double maxRev = -1;
                for (int i = 0; i < a.getSalesByCategory().getValues().size(); i++) {
                    double v = a.getSalesByCategory().getValues().get(i);
                    if (v > maxRev) {
                        maxRev = v;
                        topIdx = i;
                    }
                }
                if (maxRev > 0) {
                    insights.add("Top category by revenue: "
                            + a.getSalesByCategory().getLabels().get(topIdx) + ".");
                }
            }
        }

        if (pendingUsers > 0) {
            insights.add(pendingUsers + " customer registration(s) await approval under Manage Users.");
        }

        if (insights.isEmpty()) {
            insights.add("Operations look balanced. Keep an eye on the 7-day trend for demand shifts.");
        }
        return insights;
    }
}
