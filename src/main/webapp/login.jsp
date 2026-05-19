<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.Cookie" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Michelin Star</title>
    <style>
        *{
            margin:0;
            padding:0;
            box-sizing:border-box;
        }

        body{
            font-family:'Inter',sans-serif;
            background:#f9f6f1;

            min-height:100vh;

            display:flex;
            justify-content:center;
            align-items:center;

            padding:40px;
        }

        .login-container{
            width:100%;
            max-width:1200px;

            display:flex;

            background:white;

            border-radius:35px;

            overflow:hidden;

            box-shadow:0 20px 60px rgba(0,0,0,0.08);
        }

        /* ================= LEFT SIDE ================= */



        .login-image{
            width:50%;
            position:relative;
            min-height:700px;
        }

        .login-image img{
            width:100%;
            height:100%;
            object-fit:cover;
        }

        .overlay{
            position:absolute;
            inset:0;

            background:linear-gradient(
                    rgba(0,0,0,0.25),
                    rgba(0,0,0,0.45)
            );

            display:flex;
            flex-direction:column;
            justify-content:flex-end;

            padding:60px;
            color:white;
        }

        .overlay h1{
            font-size:60px;
            font-family:'Cormorant Garamond',serif;
            margin-bottom:20px;
        }

        .overlay p{
            line-height:1.9;
            color:#f1f1f1;
        }

        /* ================= RIGHT SIDE ================= */

        .login-form{
            width:50%;

            padding:80px 60px;

            display:flex;
            flex-direction:column;
            justify-content:center;
        }


        .logo{
            font-size:42px;
            font-family:'Cormorant Garamond',serif;
            color:#7b5e45;
            font-weight:700;

            margin-bottom:15px;
        }

        .login-form h2{
            font-size:42px;
            font-family:'Cormorant Garamond',serif;
            margin-bottom:15px;
            color:#2d2d2d;
        }

        .login-form p{
            color:#777;
            margin-bottom:40px;
            line-height:1.8;
        }

        .input-group{
            margin-bottom:25px;
        }

        .input-group label{
            display:block;
            margin-bottom:10px;
            color:#444;
            font-weight:500;
        }

        .input-group input{
            width:100%;

            padding:16px 18px;

            border:none;

            background:#f5f2ec;

            border-radius:15px;

            font-size:15px;

            outline:none;

            transition:0.3s;
        }

        .password-wrapper { position: relative; width: 100%; }
        .password-wrapper .password-toggle {
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
        .password-wrapper .password-toggle:hover { color: #333; }
        .password-wrapper input[type="password"] { padding-right: 52px; }

        .input-group input:focus{
            border:1px solid #b58b65;
            background:white;
        }

        .forgot{
            text-align:right;
            margin-bottom:30px;
        }

        .forgot a{
            color:#b58b65;
            font-size:14px;
        }

        .login-btn{
            width:100%;

            padding:16px;

            border:none;

            background:#b58b65;

            color:white;

            font-size:16px;
            font-weight:600;

            border-radius:50px;

            cursor:pointer;

            transition:0.3s;
        }

        .login-btn:hover{
            background:#8c6c4d;
            transform:translateY(-3px);
        }

        .divider{
            text-align:center;
            margin:35px 0;
            color:#999;
            position:relative;
        }

        .divider::before,
        .divider::after{
            content:"";
            position:absolute;
            top:50%;
            width:40%;
            height:1px;
            background:#ddd;
        }

        .divider::before{
            left:0;
        }

        .divider::after{
            right:0;
        }

        .social-login{
            display:flex;
            gap:15px;
        }

        .social-btn{
            flex:1;

            padding:14px;

            border-radius:15px;

            border:none;

            background:#f5f2ec;

            cursor:pointer;

            transition:0.3s;

            font-size:16px;
        }

        .social-btn:hover{
            background:#ebe4d8;
        }

        .register{
            margin-top:35px;
            text-align:center;
            color:#777;
        }

        .register a{
            color:#b58b65;
            font-weight:600;
        }

        /* ================= RESPONSIVE ================= */

        @media(max-width:900px){
            .overlay h1{
                font-size:42px;
            }

            .login-form h2{
                font-size:34px;
            }

            .login-container{
                flex-direction:column;
            }

            .login-image,
            .login-form{
                width:100%;
            }

            .login-image{
                min-height:400px;
            }

            .login-form{
                padding:60px 35px;
            }
        }

        .validation-error {
            color: #d9534f;
            font-size: 12px;
            margin-top: 5px;
            display: none;
        }

        .validation-error.show {
            display: block;
        }
    </style>
    <script>
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

        document.addEventListener('DOMContentLoaded', function() {
            const loginForm = document.querySelector('form');
            if (loginForm) {
                loginForm.addEventListener('submit', validateLoginForm);
            }

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
<div class="login-container">

    <% if (flashAuthRequired != null) { %>
    <div class="info"><%= flashAuthRequired %></div>
    <% } %>

    <% if (flashRegSuccess != null) { %>
    <div class="success"><%= flashRegSuccess %></div>
    <% } %>

    <% if (request.getAttribute("error") != null) { %>
    <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <% if (request.getAttribute("success") != null) { %>
    <div class="success"><%= request.getAttribute("success") %></div>
    <% } %>


        <!-- ================= IMAGE SIDE ================= -->

        <div class="login-image">

            <img src="https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?q=80&w=2070&auto=format&fit=crop">

            <div class="overlay">

                <h1>
                    Welcome Back
                </h1>

                <p>
                    Experience luxury dining with timeless elegance,
                    handcrafted cuisine, and unforgettable moments.
                </p>

            </div>

        </div>

        <!-- ================= FORM SIDE ================= -->

        <div class="login-form">

            <div class="logo">
                Michelin-Star
            </div>

            <h2>
                Sign In
            </h2>

            <p>
                Login to reserve your table and explore our premium dining experience.
            </p>

            <form action="<%= request.getContextPath() %>/login" method="post">

                <div class="input-group">

                    <label>Email Address</label>

                    <input type="email"
                           placeholder="Enter your email" id="email" name="email"
                           value="<%= !rememberedEmail.isEmpty() ? rememberedEmail : (request.getAttribute("email") != null ? request.getAttribute("email") : "") %>" required>

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

            <div class="divider">
                OR
            </div>

            <div class="social-login">

                <button class="social-btn">
                    <i class="fab fa-google"></i>
                </button>

                <button class="social-btn">
                    <i class="fab fa-facebook-f"></i>
                </button>

                <button class="social-btn">
                    <i class="fab fa-apple"></i>
                </button>

            </div>

            <div class="register">

                Don’t have an account?
                <a href="${pageContext.request.contextPath}/register">Register Here</a>

            </div>

        </div>

</div>
</body>
</html>


