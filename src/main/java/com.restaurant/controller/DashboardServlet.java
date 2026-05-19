/*
 * DashboardServlet.java
 * Admin analytics home: loads KPIs and chart data, builds short human insights, and shows the dashboard JSP (with a safe empty state on DB errors).
 */
package com.restaurant.controller; // admin dashboard controller

import com.restaurant.dao.AdminDAO; // analytics queries interface
import com.restaurant.dao.AdminDaoImpl; // JDBC analytics implementation
import com.restaurant.dto.ChartSeries; // labels + values for charts
import com.restaurant.dto.DashboardAnalytics; // bundle of stats and chart series
import com.restaurant.entity.User; // admin shown on the page
import jakarta.servlet.ServletException; // forward failures
import jakarta.servlet.annotation.WebServlet; // /admin/dashboard
import jakarta.servlet.http.HttpServlet; // servlet base
import jakarta.servlet.http.HttpServletRequest; // request
import jakarta.servlet.http.HttpServletResponse; // response
import jakarta.servlet.http.HttpSession; // who is logged in
import java.io.IOException; // I/O
import java.sql.SQLException; // when analytics query fails
import java.util.ArrayList; // mutable insight strings
import java.util.Collections; // empty maps/lists
import java.util.List; // insight list type
import java.util.Map; // stat key → count

@WebServlet("/admin/dashboard") // main admin landing after login
public class DashboardServlet extends HttpServlet { // serves analytics dashboard

    private final AdminDAO adminDAO = new AdminDaoImpl(); // fetch aggregates from DB

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) // render dashboard
            throws ServletException, IOException { // throws

        HttpSession session = request.getSession(false); // do not create session just to read user
        User user = session != null ? (User) session.getAttribute("user") : null; // logged-in user or null

        if (user == null || !user.isAdmin()) { // must be admin
            response.sendRedirect(request.getContextPath() + "/login"); // send to login
            return; // stop
        }

        try { // happy path — load real analytics
            DashboardAnalytics analytics = adminDAO.getDashboardAnalytics(); // all KPIs and chart data
            request.setAttribute("analytics", analytics); // full object for JSP
            request.setAttribute("stats", analytics.getStats()); // quick access map for tiles
            request.setAttribute("insights", buildInsights(analytics)); // plain-language bullets
            request.setAttribute("adminUser", user); // greet admin by name
            request.getRequestDispatcher("/views/admin/dashboard.jsp").forward(request, response); // render dashboard
        } catch (SQLException e) { // DB error — still show page with placeholders
            e.printStackTrace(); // server log
            request.setAttribute("error", "Could not load dashboard data."); // banner on page
            request.setAttribute("analytics", emptyAnalytics()); // zeros and empty charts
            request.setAttribute("stats", Collections.emptyMap()); // no stat tiles
            request.setAttribute("insights", List.of("Analytics could not be loaded. Check the database connection.")); // one helpful line
            request.setAttribute("adminUser", user); // still show who is logged in
            request.getRequestDispatcher("/views/admin/dashboard.jsp").forward(request, response); // degraded dashboard
        }
    }

    private static DashboardAnalytics emptyAnalytics() { // safe defaults when DB fails
        return new DashboardAnalytics( // construct empty analytics bundle
                Collections.emptyMap(), // no stat counts
                0, // total revenue placeholder
                0, // average sale placeholder
                0, // another numeric slot used by DTO
                ChartSeries.empty(), // empty sales-by-day chart
                ChartSeries.empty(), // empty category chart
                ChartSeries.empty(), // empty additional series
                ChartSeries.empty(), // empty additional series
                ChartSeries.empty()); // empty additional series
    }

    private static List<String> buildInsights(DashboardAnalytics a) { // turn numbers into short tips for admins
        List<String> insights = new ArrayList<>(); // collect messages
        Map<String, Integer> stats = a.getStats(); // named counters from analytics
        int totalOrders = stats.getOrDefault("totalOrders", 0); // how many order lines exist
        int pendingOrders = stats.getOrDefault("pendingOrders", 0); // lines not completed
        int pendingUsers = stats.getOrDefault("pendingUsers", 0); // registrations awaiting approval

        if (totalOrders == 0) { // brand-new or quiet restaurant
            insights.add("No orders recorded yet. Revenue and trend charts will fill in as guests place orders."); // set expectations
        } else { // we have order history to interpret
            double pendingShare = 100.0 * pendingOrders / totalOrders; // percent of lines still pending
            if (pendingOrders > 0) { // backlog worth calling out
                insights.add(String.format( // formatted sentence with counts and percent
                        "%d order line(s) are pending (%.0f%% of all lines). Prioritize preparation or status updates when busy.",
                        pendingOrders, pendingShare)); // kitchen / ops reminder
            }
            if (a.getTotalRevenue() > 0) { // revenue exists
                insights.add(String.format( // revenue summary
                        "Reported revenue from completed lines: total %,.2f with an average line value of %,.2f.",
                        a.getTotalRevenue(), a.getAverageSaleAmount())); // money context
            }
            if (!a.getSalesByCategory().getLabels().isEmpty()) { // category chart has data
                int topIdx = 0; // index of best-performing category
                double maxRev = -1; // running max revenue
                for (int i = 0; i < a.getSalesByCategory().getValues().size(); i++) { // scan each category bar
                    double v = a.getSalesByCategory().getValues().get(i); // revenue for category i
                    if (v > maxRev) { // new winner
                        maxRev = v; // remember best value
                        topIdx = i; // remember its index
                    }
                }
                if (maxRev > 0) { // only mention if something sold
                    insights.add("Top category by revenue: "
                            + a.getSalesByCategory().getLabels().get(topIdx) + "."); // name the leader
                }
            }
        }

        if (pendingUsers > 0) { // registrations waiting
            insights.add(pendingUsers + " customer registration(s) await approval under Manage Users."); // nudge admin to users page
        }

        if (insights.isEmpty()) { // nothing alarming — default positive note
            insights.add("Operations look balanced. Keep an eye on the 7-day trend for demand shifts."); // generic healthy ops tip
        }
        return insights; // list for JSP
    }
}

