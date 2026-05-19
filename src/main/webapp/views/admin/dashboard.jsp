<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.restaurant.dto.DashboardAnalytics" %>
<%@ page import="com.restaurant.utils.ChartJsonUtil" %>
<%
    request.setAttribute("currentPage", "dashboard");
    Map<String, Integer> stats = (Map<String, Integer>) request.getAttribute("stats");
    if (stats == null) stats = java.util.Collections.emptyMap();
    DashboardAnalytics analytics = (DashboardAnalytics) request.getAttribute("analytics");
    if (analytics == null) {
        analytics = new DashboardAnalytics(stats, 0, 0, 0,
                com.restaurant.dto.ChartSeries.empty(),
                com.restaurant.dto.ChartSeries.empty(),
                com.restaurant.dto.ChartSeries.empty(),
                com.restaurant.dto.ChartSeries.empty(),
                com.restaurant.dto.ChartSeries.empty());
    }
    @SuppressWarnings("unchecked")
    List<String> insights = (List<String>) request.getAttribute("insights");
    if (insights == null) insights = java.util.Collections.emptyList();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard - Michelin Star</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:wght@600;700&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
</head>
<body class="admin-body">
<div class="admin-layout">
    <jsp:include page="/includes/admin-sidebar.jsp" />

    <main class="admin-main">
        <header class="admin-header">
            <h2>Dashboard</h2>
            <p>Key performance indicators, trends, and operational signals for Michelin Star.</p>
        </header>

        <% if (request.getAttribute("error") != null) { %>
        <div class="alert-error"><%= request.getAttribute("error") %></div>
        <% } %>

        <section class="kpi-strip" aria-label="Financial KPIs">
            <div class="kpi-card kpi-primary">
                <div class="kpi-label">Total revenue (completed)</div>
                <div class="kpi-value"><%= String.format("%,.2f", analytics.getTotalRevenue()) %></div>
                <div class="kpi-hint">Sum of line totals with status completed</div>
            </div>
            <div class="kpi-card">
                <div class="kpi-label">Avg. sale amount (completed)</div>
                <div class="kpi-value"><%= String.format("%,.2f", analytics.getAverageSaleAmount()) %></div>
                <div class="kpi-hint">Mean value per completed order line</div>
            </div>
            <div class="kpi-card">
                <div class="kpi-label">Completed order lines</div>
                <div class="kpi-value"><%= analytics.getCompletedOrderLines() %></div>
                <div class="kpi-hint">Lines marked completed</div>
            </div>
            <div class="kpi-card">
                <div class="kpi-label">All order lines</div>
                <div class="kpi-value"><%= stats.getOrDefault("totalOrders", 0) %></div>
                <div class="kpi-hint">Includes pending and completed</div>
            </div>
        </section>

        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon">&#9733;</div>
                <div class="stat-label">Menu Items</div>
                <div class="stat-value"><%= stats.getOrDefault("menuItems", 0) %></div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">&#9786;</div>
                <div class="stat-label">Customers</div>
                <div class="stat-value"><%= stats.getOrDefault("customers", 0) %></div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">&#9888;</div>
                <div class="stat-label">Pending Approvals</div>
                <div class="stat-value"><%= stats.getOrDefault("pendingUsers", 0) %></div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">&#128196;</div>
                <div class="stat-label">Pending Orders</div>
                <div class="stat-value"><%= stats.getOrDefault("pendingOrders", 0) %></div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">&#128274;</div>
                <div class="stat-label">Admins</div>
                <div class="stat-value"><%= stats.getOrDefault("admins", 0) %></div>
            </div>
        </div>

        <% if (!insights.isEmpty()) { %>
        <section class="insights-panel admin-panel" aria-label="Analysis">
            <h3>Analysis &amp; signals</h3>
            <ul class="insights-list">
                <% for (String line : insights) { %>
                <li><%= line %></li>
                <% } %>
            </ul>
        </section>
        <% } %>

        <div class="chart-grid">
            <div class="chart-card admin-panel">
                <h3>Order activity (last 7 days)</h3>
                <p class="chart-sub">Number of order lines recorded per day</p>
                <div class="chart-wrap">
                    <canvas id="chartOrders7" aria-label="Orders last 7 days"></canvas>
                </div>
            </div>
            <div class="chart-card admin-panel">
                <h3>Order status mix</h3>
                <p class="chart-sub">Share of lines by fulfillment status</p>
                <div class="chart-wrap chart-wrap-doughnut">
                    <canvas id="chartStatus" aria-label="Order status distribution"></canvas>
                </div>
            </div>
            <div class="chart-card admin-panel">
                <h3>Payment methods</h3>
                <p class="chart-sub">How guests paid</p>
                <div class="chart-wrap chart-wrap-doughnut">
                    <canvas id="chartPay" aria-label="Payment methods"></canvas>
                </div>
            </div>
            <div class="chart-card admin-panel">
                <h3>Top dishes by revenue</h3>
                <p class="chart-sub">Top five menu lines by total amount</p>
                <div class="chart-wrap">
                    <canvas id="chartTopItems" aria-label="Top menu by revenue"></canvas>
                </div>
            </div>
        </div>

        <div class="chart-card admin-panel chart-wide">
            <h3>Sales by menu category</h3>
            <p class="chart-sub">Revenue attributed to each category (all order lines)</p>
            <div class="chart-wrap chart-wrap-tall">
                <canvas id="chartCategory" aria-label="Sales by category"></canvas>
            </div>
        </div>

        <div class="admin-panel">
            <h3>Quick Actions</h3>
            <div class="quick-actions">
                <a href="${pageContext.request.contextPath}/admin/menu" class="btn-admin">Manage Menu</a>
                <a href="${pageContext.request.contextPath}/admin/users" class="btn-admin btn-admin-outline">Manage Users</a>
                <a href="${pageContext.request.contextPath}/menu" class="btn-admin btn-admin-outline">View Customer Menu</a>
            </div>
        </div>
    </main>
</div>

<script>
(function() {
    var accent = '#b58b65';
    var palette = ['#b58b65', '#8c6c4d', '#7b5e45', '#c4a882', '#27ae60', '#5d8aa8', '#9b7bb8'];

    var commonOptions = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: { labels: { color: '#7a7a7a', font: { family: 'Inter' } } }
        },
        scales: {
            x: {
                ticks: { color: '#7a7a7a' },
                grid: { color: 'rgba(0,0,0,0.06)' }
            },
            y: {
                ticks: { color: '#7a7a7a' },
                grid: { color: 'rgba(0,0,0,0.06)' },
                beginAtZero: true
            }
        }
    };

    var orders7 = <%= ChartJsonUtil.toJsonObject(analytics.getOrdersLast7Days()) %>;
    new Chart(document.getElementById('chartOrders7'), {
        type: 'bar',
        data: {
            labels: orders7.labels,
            datasets: [{
                label: 'Order lines',
                data: orders7.values,
                backgroundColor: 'rgba(181, 139, 101, 0.45)',
                borderColor: accent,
                borderWidth: 1
            }]
        },
        options: Object.assign({}, commonOptions, {
            plugins: { legend: { display: false } }
        })
    });

    var statusMix = <%= ChartJsonUtil.toJsonObject(analytics.getOrderStatusMix()) %>;
    new Chart(document.getElementById('chartStatus'), {
        type: 'doughnut',
        data: {
            labels: statusMix.labels,
            datasets: [{
                data: statusMix.values,
                backgroundColor: palette,
                borderWidth: 0
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { position: 'bottom', labels: { color: '#7a7a7a', padding: 14 } }
            }
        }
    });

    var payMix = <%= ChartJsonUtil.toJsonObject(analytics.getPaymentMethods()) %>;
    new Chart(document.getElementById('chartPay'), {
        type: 'doughnut',
        data: {
            labels: payMix.labels,
            datasets: [{
                data: payMix.values,
                backgroundColor: palette,
                borderWidth: 0
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { position: 'bottom', labels: { color: '#7a7a7a', padding: 14 } }
            }
        }
    });

    var topItems = <%= ChartJsonUtil.toJsonObject(analytics.getTopMenuItems()) %>;
    new Chart(document.getElementById('chartTopItems'), {
        type: 'bar',
        data: {
            labels: topItems.labels,
            datasets: [{
                label: 'Revenue',
                data: topItems.values,
                backgroundColor: 'rgba(181, 139, 101, 0.35)',
                borderColor: accent,
                borderWidth: 1
            }]
        },
        options: {
            indexAxis: 'y',
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false },
                tooltip: {
                    callbacks: {
                        label: function(ctx) {
                            var v = ctx.raw;
                            return ' ' + (typeof v === 'number' ? v.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 }) : v);
                        }
                    }
                }
            },
            scales: {
                x: {
                    ticks: { color: '#7a7a7a' },
                    grid: { color: 'rgba(0,0,0,0.06)' },
                    beginAtZero: true
                },
                y: {
                    ticks: { color: '#7a7a7a' },
                    grid: { color: 'rgba(0,0,0,0.06)' }
                }
            }
        }
    });

    var cat = <%= ChartJsonUtil.toJsonObject(analytics.getSalesByCategory()) %>;
    new Chart(document.getElementById('chartCategory'), {
        type: 'bar',
        data: {
            labels: cat.labels,
            datasets: [{
                label: 'Revenue',
                data: cat.values,
                backgroundColor: cat.labels.map(function(_, i) {
                    var a = 0.35 + (i % 6) * 0.1;
                    return 'rgba(181, 139, 101, ' + a + ')';
                }),
                borderColor: accent,
                borderWidth: 1
            }]
        },
        options: Object.assign({}, commonOptions, {
            plugins: {
                legend: { display: false },
                tooltip: {
                    callbacks: {
                        label: function(ctx) {
                            var v = ctx.raw;
                            return ' ' + (typeof v === 'number' ? v.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 }) : v);
                        }
                    }
                }
            }
        })
    });
})();
</script>
</body>
</html>
