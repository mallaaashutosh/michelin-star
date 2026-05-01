<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.restaurant.entity.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("userPortal/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Home - Michelin Star</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f4f4f9; margin: 0; }
        .navbar { background: #333; color: white; padding: 1rem 2rem; display: flex; justify-content: space-between; align-items: center; }
        .navbar h1 { margin: 0; font-size: 1.5rem; }
        .user-info { display: flex; align-items: center; }
        .user-info img { width: 40px; height: 40px; border-radius: 50%; margin-right: 10px; background: #eee; }
        .container { padding: 2rem; max-width: 1200px; margin: 0 auto; }
        .card { background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        .logout-btn { color: #ff4d4d; text-decoration: none; font-weight: bold; }
    </style>
</head>
<body>
    <div class="navbar">
        <h1>Michelin Star Restaurant</h1>
        <div class="user-info">
            <% if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) { %>
                <img src="uploads/<%= user.getProfileImage() %>" alt="Profile">
            <% } else { %>
                <div style="width: 40px; height: 40px; border-radius: 50%; background: #007bff; color: white; display: flex; justify-content: center; align-items: center; margin-right: 10px;">
                    <%= user.getName().substring(0, 1).toUpperCase() %>
                </div>
            <% } %>
            <span>Welcome, <strong><%= user.getName() %></strong></span>
            &nbsp;&nbsp;|&nbsp;&nbsp;
            <a href="logout" class="logout-btn">Logout</a>
        </div>
    </div>

    <div class="container">
        <div class="card">
            <h2>Dashboard</h2>
            <p>Hello <%= user.getName() %>, you are successfully logged in!</p>
            <p>Your Email: <%= user.getEmail() %></p>
            <p>Your Phone: <%= user.getPhoneNumber() %></p>
        </div>
    </div>
</body>
</html>
