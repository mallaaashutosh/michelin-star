<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.restaurant.entity.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    if (user.isAdmin()) {
        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        return;
    }
    String ctx = request.getContextPath();
    String initial = user.getName() != null && !user.getName().isEmpty()
            ? user.getName().substring(0, 1).toUpperCase() : "U";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Home - Michelin Star</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #0f0f0f; color: #f5f5f5; margin: 0; }
        .navbar { background: #1a1a1a; border-bottom: 1px solid #333; padding: 1rem 2rem; display: flex; justify-content: space-between; align-items: center; }
        .navbar h1 { margin: 0; font-size: 1.25rem; color: #d6be38; }
        .user-info { display: flex; align-items: center; gap: 0.75rem; }
        .avatar { width: 40px; height: 40px; border-radius: 50%; background: #d6be38; color: #000; display: flex; align-items: center; justify-content: center; font-weight: bold; }
        .container { padding: 2rem; max-width: 900px; margin: 0 auto; }
        .card { background: #222; border: 1px solid #333; padding: 2rem; border-radius: 10px; }
        .card h2 { color: #d6be38; margin-top: 0; }
        .card p { color: #9a9a9a; line-height: 1.6; }
        .actions { margin-top: 1.5rem; display: flex; gap: 1rem; flex-wrap: wrap; }
        .btn { display: inline-block; padding: 0.65rem 1.25rem; background: #d6be38; color: #000; text-decoration: none; border-radius: 6px; font-weight: 600; }
        .btn-outline { background: transparent; color: #d6be38; border: 1px solid #d6be38; }
        .logout-btn { color: #e74c3c; text-decoration: none; font-weight: 600; }
    </style>
</head>
<body>
    <div class="navbar">
        <h1>Michelin Star Restaurant</h1>
        <div class="user-info">
            <div class="avatar"><%= initial %></div>
            <span>Welcome, <strong><%= user.getName() %></strong></span>
            <a href="<%= ctx %>/logout" class="logout-btn">Logout</a>
        </div>
    </div>

    <div class="container">
        <div class="card">
            <h2>Your Dashboard</h2>
            <p>Hello <%= user.getName() %>, you are signed in as a customer.</p>
            <p>Email: <%= user.getEmail() %><br>Phone: <%= user.getPhoneNumber() != null ? user.getPhoneNumber() : "—" %><br>Status: <%= user.getStatus() != null ? user.getStatus() : "active" %></p>
            <div class="actions">
                <a href="<%= ctx %>/menu" class="btn">Browse Menu</a>
                <a href="<%= ctx %>/index.jsp" class="btn btn-outline">Back to Home</a>
            </div>
        </div>
    </div>
</body>
</html>
