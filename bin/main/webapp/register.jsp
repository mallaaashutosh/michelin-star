<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register - Michelin Star</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f4f4f9; display: flex; justify-content: center; align-items: center; min-height: 100vh; margin: 0; padding: 2rem 0; }
        .register-container { background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); width: 100%; max-width: 500px; }
        h2 { text-align: center; color: #333; margin-top: 0; }
        .form-group { margin-bottom: 1rem; }
        label { display: block; margin-bottom: 0.5rem; color: #666; font-weight: 600; }
        input[type="text"], input[type="email"], input[type="password"], input[type="tel"], input[type="file"] {
            width: 100%; padding: 0.75rem; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box;
        }
        .btn { width: 100%; padding: 0.75rem; border: none; border-radius: 4px; background: #28a745; color: white; font-size: 1rem; cursor: pointer; transition: background 0.3s; margin-top: 1rem; }
        .btn:hover { background: #218838; }
        .error { color: #d9534f; background: #f2dede; padding: 0.5rem; border-radius: 4px; margin-bottom: 1rem; font-size: 0.9rem; text-align: center; }
        .footer { text-align: center; margin-top: 1rem; font-size: 0.9rem; color: #777; }
        .footer a { color: #007bff; text-decoration: none; }
    </style>
</head>
<body>
<div class="register-container">
    <h2>Create Account</h2>

    <% if (request.getAttribute("error") != null) { %>
    <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/register" method="post">
        <div class="form-group">
            <label for="name">Full Name</label>
            <input type="text" id="name" name="name" required>
        </div>
        <div class="form-group">
            <label for="phone">Phone Number</label>
            <input type="tel" id="phone" name="phone">
        </div>
        <div class="form-group">
            <label for="email">Email Address</label>
            <input type="email" id="email" name="email" required>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" required>
        </div>
        <div class="form-group">
            <label for="confirmPassword">Confirm Password</label>
            <input type="password" id="confirmPassword" name="confirmPassword" required>
        </div>

        <button type="submit" class="btn">Register</button>
    </form>

    <div class="footer">
        Already have an account? <a href="${pageContext.request.contextPath}/login">Login here</a>
    </div>
</div>
</body>
</html>
