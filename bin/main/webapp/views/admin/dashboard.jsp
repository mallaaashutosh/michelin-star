<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%
    request.setAttribute("currentPage", "dashboard");
    Map<String, Integer> stats = (Map<String, Integer>) request.getAttribute("stats");
    if (stats == null) stats = java.util.Collections.emptyMap();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard - Michelin Star</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body class="admin-body">
<div class="admin-layout">
    <jsp:include page="/includes/admin-sidebar.jsp" />

    <main class="admin-main">
        <header class="admin-header">
            <h2>Dashboard</h2>
            <p>Overview of your restaurant at a glance.</p>
        </header>

        <% if (request.getAttribute("error") != null) { %>
        <div class="alert-error"><%= request.getAttribute("error") %></div>
        <% } %>

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
                <div class="stat-icon">&#128200;</div>
                <div class="stat-label">Total Orders</div>
                <div class="stat-value"><%= stats.getOrDefault("totalOrders", 0) %></div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">&#128274;</div>
                <div class="stat-label">Admins</div>
                <div class="stat-value"><%= stats.getOrDefault("admins", 0) %></div>
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
</body>
</html>
