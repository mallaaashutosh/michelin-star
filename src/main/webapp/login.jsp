<%--
    File: login.jsp
    Purpose: Customer sign-in page for Michelin Star restaurant
    Shows a split layout with hero imagery and login form
    Pre-fills email from remember-me cookie and displays flash messages
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.Cookie" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Michelin Star</title>
    <style>
        /* Reset everything - no surprises */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        /* Main page background - warm and inviting */
        body {
            font-family: 'Inter', sans-serif;
            background: #f9f6f1;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 40px;
        }

        /* The main card container - split screen layout */
        .login-container {
            width: 100%;
            max-width: 1200px;
            display: flex;
            background: white;
            border-radius: 35px;
            overflow: hidden;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.08);
        }

        /* ================= LEFT SIDE - HERO IMAGE ================= */
        .login-image {
            width: 50%;
            position: relative;
            min-height: 700px;
        }

        .login-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        /* Dark gradient overlay so text is readable */
        .overlay {
            position: absolute;
            inset: 0;
            background: linear-gradient(
                    rgba(0, 0, 0, 0.25),
                    rgba(0, 0, 0, 0.45)
            );
            display: flex;
            flex-direction: column;
            justify-content: flex-end;
            padding: 60px;
            color: white;
        }

        .overlay h1 {
            font-size: 60px;
            font-family: 'Cormorant Garamond', serif;
            margin-bottom: 20px;
        }

        .overlay p {
            line-height: 1.9;
            color: #f1f1f1;
        }

        /* ================= RIGHT SIDE - LOGIN FORM ================= */
        .login-form {
            width: 50%;
            padding: 80px 60px;
            display: flex;
            flex-direction: column;
            justify-content: center;
        }

        /* Brand name at the top - keep it classy */
        .logo {
            font-size: 42px;
            font-family: 'Cormorant Garamond', serif;
            color: #7b5e45;
            font-weight: 700;
            margin-bottom: 15px;
        }

        .login-form h2 {
            font-size: 42px;
            font-family: 'Cormorant Garamond', serif;
            margin-bottom: 15px;
            color: #2d2d2d;
        }

        .login-form p {
            color: #777;
            margin-bottom: 40px;
            line-height: 1.8;
        }

        /* Form field styling */
        .input-group {
            margin-bottom: 25px;
        }

        .input-group label {
            display: block;
            margin-bottom: 10px;
            color: #444;
            font-weight: 500;
        }

        .input-group input {
            width: 100%;
            padding: 16px 18px;
            border: none;
            background: #f5f2ec;
            border-radius: 15px;
            font-size: 15px;
            outline: none;
            transition: 0.3s;
        }

        /* Password show/hide toggle button */
        .password-wrapper {
            position: relative;
            width: 100%;
        }

        .password-toggle {
            position: absolute;
            right: 18px;
            top: 50%;
            transform: translateY(-50%);
            cursor: pointer;
            font-size: 18px;
            color: #777;
            user-select: none;
            transition: color 0.3s;
        }

        .password-toggle:hover {
            color: #333;
        }

        .password-wrapper input[type="password"] {
            padding-right: 52px;
        }

        .input-group input:focus {
            border: 1px solid #b58b65;
            background: white;
        }

        /* Forgot password link - right aligned */
        .forgot {
            text-align: right;
            margin-bottom: 30px;
        }

        .forgot a {
            color: #b58b65;
            font-size: 14px;
            text-decoration: none;
        }

        .forgot a:hover {
            text-decoration: underline;
        }

        /* Primary login button */
        .login-btn {
            width: 100%;
            padding: 16px;
            border: none;
            background: #b58b65;
            color: white;
            font-size: 16px;
            font-weight: 600;
            border-radius: 50px;
            cursor: pointer;
            transition: 0.3s;
        }

        .login-btn:hover {
            background: #8c6c4d;
            transform: translateY(-3px);
        }

        /* Register link at the bottom */
        .register {
            margin-top: 35px;
            text-align: center;
            color: #777;
        }

        .register a {
            color: #b58b65;
            font-weight: 600;
            text-decoration: none;
        }

        .register a:hover {
            text-decoration: underline;
        }

        /* ================= FLASH MESSAGE STYLES ================= */
        .flash-container {
            position: fixed;
            top: 20px;
            left: 50%;
            transform: translateX(-50%);
            z-index: 1000;
            width: auto;
            max-width: 90%;
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        .flash-message {
            padding: 12px 24px;
            border-radius: 8px;
            font-size: 14px;
            font-weight: 500;
            text-align: center;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
            animation: slideDown 0.3s ease-out;
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 15px;
        }

        .flash-message.info {
            background: #17a2b8;
            color: white;
        }

        .flash-message.success {
            background: #28a745;
            color: white;
        }

        .flash-message.error {
            background: #dc3545;
            color: white;
        }

        .flash-close {
            background: none;
            border: none;
            color: white;
            cursor: pointer;
            font-size: 18px;
            font-weight: bold;
            opacity: 0.8;
            transition: opacity 0.3s;
        }

        .flash-close:hover {
            opacity: 1;
        }

        @keyframes slideDown {
            from {
                opacity: 0;
                transform: translateX(-50%) translateY(-20px);
            }
            to {
                opacity: 1;
                transform: translateX(-50%) translateY(0);
            }
        }

        /* Validation error message styling */
        .validation-error {
            color: #d9534f;
            font-size: 12px;
            margin-top: 5px;
            display: none;
        }

        .validation-error.show {
            display: block;
        }

        /* ================= RESPONSIVE DESIGN ================= */
        @media(max-width: 900px) {
            .overlay h1 {
                font-size: 42px;
            }

            .login-form h2 {
                font-size: 34px;
            }

            .login-container {
                flex-direction: column;
            }

            .login-image,
            .login-form {
                width: 100%;
            }

            .login-image {
                min-height: 400px;
            }

            .login-form {
                padding: 60px 35px;
            }
        }
    </style>

    <script>
        // Quick email validation before sending to server - saves a round trip
        function validateEmail(email) {
            const emailPattern = /^[A-Za-z0-9+_.-]+@(.+)$/;
            return emailPattern.test(email);
        }

        function validateLoginForm(event) {
            const email = document.getElementById('email').value.trim();
            const emailError = document.getElementById('emailError');
            let isValid = true;

            if (!validateEmail(email)) {
                emailError.innerText = "Please enter a valid email address (e.g., user@example.com)";
                emailError.classList.add('show');
                isValid = false;
            } else {
                emailError.classList.remove('show');
            }

            if (!isValid) {
                event.preventDefault();
            }
        }

        // Auto-hide flash messages after 5 seconds
        function autoHideFlashMessages() {
            const flashMessages = document.querySelectorAll('.flash-message');
            flashMessages.forEach(function(message) {
                setTimeout(function() {
                    message.style.opacity = '0';
                    message.style.transition = 'opacity 0.5s';
                    setTimeout(function() {
                        if (message.parentNode) {
                            message.remove();
                        }
                    }, 500);
                }, 5000);
            });
        }

        // Close flash message manually
        function closeFlashMessage(element) {
            const message = element.parentElement;
            message.style.opacity = '0';
            message.style.transition = 'opacity 0.3s';
            setTimeout(function() {
                if (message.parentNode) {
                    message.remove();
                }
            }, 300);
        }

        // Toggle password visibility
        function togglePasswordVisibility(inputId, toggleId) {
            const input = document.getElementById(inputId);
            const toggle = document.getElementById(toggleId);

            if (input.type === 'password') {
                input.type = 'text';
                toggle.textContent = '👁️';
            } else {
                input.type = 'password';
                toggle.textContent = '👁️‍🗨️';
            }
        }

        // Set up event listeners once the page is ready
        document.addEventListener('DOMContentLoaded', function() {
            // Auto-hide flash messages
            autoHideFlashMessages();

            // Validate on form submit
            const loginForm = document.querySelector('form');
            if (loginForm) {
                loginForm.addEventListener('submit', validateLoginForm);
            }

            // Validate on blur (when user leaves the email field)
            const emailInput = document.getElementById('email');
            if (emailInput) {
                emailInput.addEventListener('blur', function() {
                    const emailError = document.getElementById('emailError');
                    if (!validateEmail(this.value.trim())) {
                        emailError.innerText = "Please enter a valid email address";
                        emailError.classList.add('show');
                    } else {
                        emailError.classList.remove('show');
                    }
                });
            }

            // Password toggle functionality
            const passwordToggle = document.getElementById('passwordToggle');
            if (passwordToggle) {
                passwordToggle.addEventListener('click', function() {
                    togglePasswordVisibility('password', 'passwordToggle');
                });
            }
        });
    </script>
</head>
<body>

<%
    String rememberedEmail = "";
    String flashRegSuccess = (String) session.getAttribute("flashRegisterSuccess");
    if (flashRegSuccess != null) {
        session.removeAttribute("flashRegisterSuccess");
    }

    String flashAuthRequired = (String) session.getAttribute("flashAuthRequired");
    if (flashAuthRequired != null) {
        session.removeAttribute("flashAuthRequired");
    }

    // Check for saved email cookie
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

<!-- Flash Messages Container - Fixed position at top center -->
<div class="flash-container">
    <% if (flashAuthRequired != null) { %>
    <div class="flash-message info">
        <%= flashAuthRequired %>
        <button class="flash-close" onclick="closeFlashMessage(this)">✕</button>
    </div>
    <% } %>

    <% if (flashRegSuccess != null) { %>
    <div class="flash-message success">
        <%= flashRegSuccess %>
        <button class="flash-close" onclick="closeFlashMessage(this)">✕</button>
    </div>
    <% } %>

    <% if (request.getAttribute("error") != null) { %>
    <div class="flash-message error">
        <%= request.getAttribute("error") %>
        <button class="flash-close" onclick="closeFlashMessage(this)">✕</button>
    </div>
    <% } %>

    <% if (request.getAttribute("success") != null) { %>
    <div class="flash-message success">
        <%= request.getAttribute("success") %>
        <button class="flash-close" onclick="closeFlashMessage(this)">✕</button>
    </div>
    <% } %>
</div>

<div class="login-container">
    <!-- ================= LEFT COLUMN - RESTAURANT IMAGE ================= -->
    <div class="login-image">
        <img src="https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?q=80&w=2070&auto=format&fit=crop"
             alt="Elegant restaurant interior">
        <div class="overlay">
            <h1>Welcome Back</h1>
            <p>
                Experience luxury dining with timeless elegance,
                handcrafted cuisine, and unforgettable moments.
            </p>
        </div>
    </div>

    <!-- ================= RIGHT COLUMN - LOGIN FORM ================= -->
    <div class="login-form">
        <div class="logo">Michelin-Star</div>

        <h2>Sign In</h2>

        <p>
            Login to order your food and explore our premium dining experience.
        </p>

        <form action="<%= request.getContextPath() %>/login" method="post">

            <div class="input-group">
                <label>Email Address</label>
                <input type="email"
                       placeholder="Enter your email"
                       id="email"
                       name="email"
                       value="<%= !rememberedEmail.isEmpty() ? rememberedEmail : (request.getAttribute("email") != null ? request.getAttribute("email") : "") %>"
                       required>
                <div id="emailError" class="validation-error"></div>
            </div>

            <div class="input-group">
                <label>Password</label>
                <div class="password-wrapper">
                    <input type="password"
                           id="password"
                           name="password"
                           placeholder="Enter your password"
                           required>
                    <span id="passwordToggle" class="password-toggle">👁️‍🗨️</span>
                </div>
            </div>

            <div class="forgot">
                <a href="#">Forgot Password?</a>
            </div>

            <button type="submit" class="login-btn">
                Login
            </button>
        </form>

        <div class="register">
            Don't have an account?
            <a href="${pageContext.request.contextPath}/register">Register Here</a>
        </div>
    </div>
</div>

</body>
</html>