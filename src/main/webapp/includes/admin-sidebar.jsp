<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.restaurant.entity.User" %>
<%
    User adminUser = (User) session.getAttribute("user");
    String currentPage = (String) request.getAttribute("currentPage");
    if (currentPage == null) currentPage = "";
    String ctx = request.getContextPath();
    String initial = adminUser != null && adminUser.getName() != null && !adminUser.getName().isEmpty()
            ? adminUser.getName().substring(0, 1).toUpperCase() : "A";
%>
<aside class="admin-sidebar">
    <div class="admin-brand">
        <h1>Michelin Star</h1>
        <p>Admin Panel</p>
    </div>
    <nav class="admin-nav">
        <a href="<%= ctx %>/admin/dashboard" class="<%= "dashboard".equals(currentPage) ? "active" : "" %>">
            <span class="nav-icon">&#9632;</span> Dashboard
        </a>
        <a href="<%= ctx %>/admin/menu" class="<%= "menu".equals(currentPage) ? "active" : "" %>">
            <span class="nav-icon">&#9733;</span> Menu
        </a>
        <a href="<%= ctx %>/admin/users" class="<%= "users".equals(currentPage) ? "active" : "" %>">
            <span class="nav-icon">&#9786;</span> Users
        </a>
        <a href="<%= ctx %>/site">
            <span class="nav-icon">&#8962;</span> View Site
        </a>
    </nav>
    <div class="admin-sidebar-footer">
        <div class="admin-user-chip">
            <div class="admin-avatar"><%= initial %></div>
            <div>
                <strong><%= adminUser != null ? adminUser.getName() : "Admin" %></strong>
                <span><%= adminUser != null ? adminUser.getEmail() : "" %></span>
            </div>
        </div>
        <a href="<%= ctx %>/logout" class="logout-link">Sign Out</a>
    </div>
</aside>
