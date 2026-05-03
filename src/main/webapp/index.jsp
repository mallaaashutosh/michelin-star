<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Michelin Star Restaurant</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: Arial, sans-serif;
            background-color: #1a1a1a;
            color: white;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
        }
        h1 { font-size: 48px; margin-bottom: 10px; }
        p  { font-size: 18px; color: #aaa; margin-bottom: 40px; }
        .buttons { display: flex; gap: 20px; }
        .btn {
            padding: 14px 40px;
            font-size: 16px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            text-decoration: none;
        }
        .btn-login    { background-color: #c8a84b; color: #000; }
        .btn-register { background-color: transparent; color: white;
            border: 2px solid white; }
        .btn:hover { opacity: 0.85; }
    </style>
</head>
<body>
<h1>Michelin Star</h1>
<p>Fine Dining Experience</p>
<div class="buttons">
    <a href="login.jsp" class="btn btn-login">Login</a>
    <a href="register.jsp" class="btn btn-register">Register</a>
</div>
</body>
</html>