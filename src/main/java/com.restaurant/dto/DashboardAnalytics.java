package com.restaurant.dto;

import java.util.Map;

public class DashboardAnalytics {

    private final Map<String, Integer> stats;
    private final double totalRevenue;
    private final double averageSaleAmount;
    private final int completedOrderLines;
    private final ChartSeries ordersLast7Days;
    private final ChartSeries orderStatusMix;
    private final ChartSeries paymentMethods;
    private final ChartSeries topMenuItems;
    private final ChartSeries salesByCategory;

    public DashboardAnalytics(Map<String, Integer> stats,
            double totalRevenue,
            double averageSaleAmount,
            int completedOrderLines,
            ChartSeries ordersLast7Days,
            ChartSeries orderStatusMix,
            ChartSeries paymentMethods,
            ChartSeries topMenuItems,
            ChartSeries salesByCategory) {
        this.stats = stats != null ? Map.copyOf(stats) : Map.of();
        this.totalRevenue = totalRevenue;
        this.averageSaleAmount = averageSaleAmount;
        this.completedOrderLines = completedOrderLines;
        this.ordersLast7Days = ordersLast7Days != null ? ordersLast7Days : ChartSeries.empty();
        this.orderStatusMix = orderStatusMix != null ? orderStatusMix : ChartSeries.empty();
        this.paymentMethods = paymentMethods != null ? paymentMethods : ChartSeries.empty();
        this.topMenuItems = topMenuItems != null ? topMenuItems : ChartSeries.empty();
        this.salesByCategory = salesByCategory != null ? salesByCategory : ChartSeries.empty();
    }

    public Map<String, Integer> getStats() {
        return stats;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public double getAverageSaleAmount() {
        return averageSaleAmount;
    }

    public int getCompletedOrderLines() {
        return completedOrderLines;
    }

    public ChartSeries getOrdersLast7Days() {
        return ordersLast7Days;
    }

    public ChartSeries getOrderStatusMix() {
        return orderStatusMix;
    }

    public ChartSeries getPaymentMethods() {
        return paymentMethods;
    }

    public ChartSeries getTopMenuItems() {
        return topMenuItems;
    }

    public ChartSeries getSalesByCategory() {
        return salesByCategory;
    }
}
