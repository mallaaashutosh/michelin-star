<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Michelin Star</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f4f4f9; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
        .login-container { background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); width: 100%; max-width: 400px; }
        h2 { text-align: center; color: #333; }
        .form-group { margin-bottom: 1rem; }
        label { display: block; margin-bottom: 0.5rem; color: #666; }
        input[type="email"], input[type="password"] { width: 100%; padding: 0.75rem; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
        .btn { width: 100%; padding: 0.75rem; border: none; border-radius: 4px; background: #007bff; color: white; font-size: 1rem; cursor: pointer; transition: background 0.3s; }
        .btn:hover { background: #0056b3; }
        .error { color: #d9534f; background: #f2dede; padding: 0.5rem; border-radius: 4px; margin-bottom: 1rem; font-size: 0.9rem; text-align: center; }
        .success { color: #3c763d; background: #dff0d8; padding: 0.5rem; border-radius: 4px; margin-bottom: 1rem; font-size: 0.9rem; text-align: center; }
        .footer { text-align: center; margin-top: 1rem; font-size: 0.9rem; color: #777; }
        .footer a { color: #007bff; text-decoration: none; }
    </style>
</head>
<body>
<div class="login-container">
    <h2>Login</h2>

    <% if (request.getAttribute("error") != null) { %>
    <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <% if (request.getAttribute("success") != null) { %>
    <div class="success"><%= request.getAttribute("success") %></div>
    <% } %>

    <%
        String rememberedEmail = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userEmail")) {
                    rememberedEmail = cookie.getValue();
                    break;
                }
            }
        }
    %>

    <form action="<%= request.getContextPath() %>/login" method="post">
        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" value="<%= !rememberedEmail.isEmpty() ? rememberedEmail : (request.getAttribute("email") != null ? request.getAttribute("email") : "") %>" required>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" required>
        </div>
        <div class="form-group" style="display: flex; align-items: center;">
            <input type="checkbox" id="rememberMe" name="rememberMe" style="margin-right: 0.5rem;">
            <label for="rememberMe" style="margin-bottom: 0;">Remember Me</label>
        </div>
        <button type="submit" class="btn">Login</button>
    </form>

    <div class="footer">
        Don't have an account? <a href="register.jsp">Register here</a>
    </div>
</div>
</body>
</html>
