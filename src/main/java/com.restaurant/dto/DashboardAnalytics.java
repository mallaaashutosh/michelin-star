/*
 * Bundles everything the admin dashboard JSP needs into one object.
 * Includes headline stats (order counts, revenue) plus several ChartSeries for graphs.
 * Built once in DashboardServlet and forwarded to the view so the JSP stays mostly markup.
 */
package com.restaurant.dto;

import java.util.Map;

/**
 * Read-only snapshot of all dashboard numbers and chart data.
 * Think of this as a single package the admin page can unpack and display.
 */
public class DashboardAnalytics {

    // Core business metrics
    private final Map<String, Integer> stats;        // Named counters for stat cards (e.g., "totalOrders" -> 47)
    private final double totalRevenue;               // Sum of all completed order amounts
    private final double averageSaleAmount;          // Average ticket size (revenue ÷ order count)
    private final int completedOrderLines;           // Total line items fulfilled (used for averages)

    // Chart data for visualizations
    private final ChartSeries ordersLast7Days;       // Bar/line chart: orders per day (last week)
    private final ChartSeries orderStatusMix;        // Pie chart: pending vs completed vs cancelled
    private final ChartSeries paymentMethods;        // How customers paid (cash, card, etc.)
    private final ChartSeries topMenuItems;          // Best-selling dishes (quantity or revenue)
    private final ChartSeries salesByCategory;       // Revenue broken down by menu category

    /**
     * Full constructor - all dashboard data passed in one go.
     * Defensive null checks prevent charts from breaking the JSP.
     *
     * @param stats Key-value pairs for stat cards (totalOrders, pendingCount, etc.)
     * @param totalRevenue Total money from completed orders
     * @param averageSaleAmount Average order value
     * @param completedOrderLines Count of line items in completed orders
     * @param ordersLast7Days Daily order counts (last 7 days)
     * @param orderStatusMix Distribution of order statuses
     * @param paymentMethods Payment method distribution
     * @param topMenuItems Most popular menu items
     * @param salesByCategory Revenue by category
     */
    public DashboardAnalytics(Map<String, Integer> stats,
                              double totalRevenue,
                              double averageSaleAmount,
                              int completedOrderLines,
                              ChartSeries ordersLast7Days,
                              ChartSeries orderStatusMix,
                              ChartSeries paymentMethods,
                              ChartSeries topMenuItems,
                              ChartSeries salesByCategory) {

        // Make an immutable copy - we don't want the servlet modifying stats after creating this object
        this.stats = stats != null ? Map.copyOf(stats) : Map.of();

        this.totalRevenue = totalRevenue;
        this.averageSaleAmount = averageSaleAmount;
        this.completedOrderLines = completedOrderLines;

        // Guard against null charts - better to show empty chart than crash the JSP
        this.ordersLast7Days = ordersLast7Days != null ? ordersLast7Days : ChartSeries.empty();
        this.orderStatusMix = orderStatusMix != null ? orderStatusMix : ChartSeries.empty();
        this.paymentMethods = paymentMethods != null ? paymentMethods : ChartSeries.empty();
        this.topMenuItems = topMenuItems != null ? topMenuItems : ChartSeries.empty();
        this.salesByCategory = salesByCategory != null ? salesByCategory : ChartSeries.empty();
    }

    // ==================== Getters ====================
    // Simple accessors - the JSP calls these to display everything

    public Map<String, Integer> getStats() {
        return stats;  // Stat card numbers (totalOrders, pendingCount, completedCount, etc.)
    }

    public double getTotalRevenue() {
        return totalRevenue;  // All-time or filtered revenue (depending on what servlet queried)
    }

    public double getAverageSaleAmount() {
        return averageSaleAmount;  // Average order value - good for spotting trends
    }

    public int getCompletedOrderLines() {
        return completedOrderLines;  // Used internally for calculating averages
    }

    public ChartSeries getOrdersLast7Days() {
        return ordersLast7Days;  // Recent order trend - helpful for spotting peak days
    }

    public ChartSeries getOrderStatusMix() {
        return orderStatusMix;  // What percentage of orders are pending vs completed
    }

    public ChartSeries getPaymentMethods() {
        return paymentMethods;  // Shows which payment methods customers prefer
    }

    public ChartSeries getTopMenuItems() {
        return topMenuItems;  // Best-sellers - useful for inventory planning
    }

    public ChartSeries getSalesByCategory() {
        return salesByCategory;  // Which menu categories drive the most revenue
    }
}