<%--
    File: register.jsp
    Luxury Register Page for Michelin-Star Restaurant
--%>

<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Register | Michelin-Star</title>

    <!-- GOOGLE FONTS -->

    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:wght@500;600;700&family=Inter:wght@300;400;500;600&display=swap"
          rel="stylesheet">

    <!-- FONT AWESOME -->

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

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

        /* ================= CONTAINER ================= */

        .register-container{

            width:100%;
            max-width:550px;

            background:white;

            padding:60px 50px;

            border-radius:35px;

            box-shadow:0 20px 60px rgba(0,0,0,0.08);
        }

        /* ================= LOGO ================= */

        .logo{

            text-align:center;

            font-size:46px;

            font-family:'Cormorant Garamond',serif;

            color:#7b5e45;

            font-weight:700;

            margin-bottom:10px;
        }

        /* ================= HEADING ================= */

        .heading-section{

            text-align:center;

            margin-bottom:35px;
        }

        .heading-section h1{

            font-size:50px;

            font-family:'Cormorant Garamond',serif;

            color:#2d2d2d;

            margin-bottom:15px;
        }

        .heading-section p{

            color:#777;

            line-height:1.8;

            font-size:15px;
        }

        /* ================= ERROR ================= */

        .server-error{

            background:#ffe5e5;

            color:#d9534f;

            padding:14px;

            border-radius:12px;

            margin-bottom:20px;

            text-align:center;
        }

        /* ================= FORM ================= */

        .form-group{
            margin-bottom:22px;
        }

        label{

            display:block;

            margin-bottom:10px;

            color:#444;

            font-weight:500;
        }

        input{

            width:100%;

            padding:16px 18px;

            border:none;

            background:#f5f2ec;

            border-radius:15px;

            font-size:15px;

            outline:none;

            transition:0.3s;
        }

        input:focus{

            border:1px solid #b58b65;

            background:white;
        }

        /* ================= PASSWORD ================= */

        .password-wrapper{
            position:relative;
        }

        .toggle-password{

            position:absolute;

            right:18px;

            top:50%;

            transform:translateY(-50%);

            cursor:pointer;

            color:#777;

            font-size:18px;
        }

        /* ================= VALIDATION ================= */

        .validation-message{

            font-size:13px;

            margin-top:8px;

            display:none;
        }

        .error{
            color:#d9534f;
        }

        .success{
            color:#28a745;
        }

        /* ================= PASSWORD RULES ================= */

        .password-rules{

            margin-top:12px;

            font-size:13px;

            color:#666;

            line-height:1.8;
        }

        .rule.valid{
            color:#28a745;
        }

        .rule.invalid{
            color:#d9534f;
        }

        /* ================= BUTTON ================= */

        .register-btn{

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

            margin-top:10px;
        }

        .register-btn:hover{

            background:#8c6c4d;

            transform:translateY(-3px);
        }

        /* ================= FOOTER ================= */

        .footer{

            text-align:center;

            margin-top:30px;

            color:#777;
        }

        .footer a{

            color:#b58b65;

            text-decoration:none;

            font-weight:600;
        }

        .footer a:hover{
            text-decoration:underline;
        }

        /* ================= RESPONSIVE ================= */

        @media(max-width:600px){

            .register-container{
                padding:45px 30px;
            }

            .heading-section h1{
                font-size:38px;
            }

            .logo{
                font-size:38px;
            }
        }

    </style>

</head>

<body>

<div class="register-container">

    <!-- ================= LOGO ================= -->

    <div class="logo">
        Michelin-Star
    </div>

    <!-- ================= HEADING ================= -->

    <div class="heading-section">

        <h1>
            Create Your Account
        </h1>

        <p>
            Join Michelin-Star and enjoy luxury dining,
            premium reservations, and unforgettable experiences.
        </p>

    </div>

    <!-- ================= SERVER ERROR ================= -->

    <% if(request.getAttribute("error") != null){ %>

    <div class="server-error">
        <%= request.getAttribute("error") %>
    </div>

    <% } %>

    <!-- ================= FORM ================= -->

    <form action="<%= request.getContextPath() %>/register"
          method="post"
          onsubmit="return validateForm()">

        <!-- FULL NAME -->

        <div class="form-group">

            <label>Full Name</label>

            <input type="text"
                   name="name"
                   placeholder="Enter your full name"
                   required>

        </div>

        <!-- PHONE -->

        <div class="form-group">

            <label>Phone Number</label>

            <input type="tel"
                   id="phone"
                   name="phone"
                   placeholder="Enter phone number"
                   required>

        </div>

        <!-- EMAIL -->

        <div class="form-group">

            <label>Email Address</label>

            <input type="email"
                   id="email"
                   name="email"
                   placeholder="Enter your email"
                   required>

            <div id="emailError"
                 class="validation-message error">
            </div>

        </div>

        <!-- PASSWORD -->

        <div class="form-group">

            <label>Password</label>

            <div class="password-wrapper">

                <input type="password"
                       id="password"
                       name="password"
                       placeholder="Create password"
                       required>

                <span class="toggle-password"
                      onclick="togglePassword('password')">

                    <i class="fa-solid fa-eye"></i>

                </span>

            </div>

            <!-- PASSWORD RULES -->

            <div class="password-rules">

                <div id="lengthRule"
                     class="rule invalid">

                    ✓ Minimum 8 characters

                </div>

                <div id="upperRule"
                     class="rule invalid">

                    ✓ One uppercase letter

                </div>

                <div id="numberRule"
                     class="rule invalid">

                    ✓ One number

                </div>

                <div id="specialRule"
                     class="rule invalid">

                    ✓ One special character

                </div>

            </div>

        </div>

        <!-- CONFIRM PASSWORD -->

        <div class="form-group">

            <label>Confirm Password</label>

            <div class="password-wrapper">

                <input type="password"
                       id="confirmPassword"
                       name="confirmPassword"
                       placeholder="Confirm password"
                       required>

                <span class="toggle-password"
                      onclick="togglePassword('confirmPassword')">

                    <i class="fa-solid fa-eye"></i>

                </span>

            </div>

            <div id="passwordMatch"
                 class="validation-message">
            </div>

        </div>

        <!-- BUTTON -->

        <button type="submit"
                class="register-btn">

            Register

        </button>

    </form>

    <!-- ================= FOOTER ================= -->

    <div class="footer">

        Already have an account?

        <a href="<%= request.getContextPath() %>/login.jsp">

            Login Here

        </a>

    </div>

</div>

<!-- ================= JAVASCRIPT ================= -->

<script>

    // SHOW / HIDE PASSWORD

    function togglePassword(id){

        let input =
            document.getElementById(id);

        if(input.type === "password"){

            input.type = "text";

        }else{

            input.type = "password";
        }
    }

    // PASSWORD RULES

    document.getElementById("password")
        .addEventListener("input", function(){

            let password = this.value;

            validateRule(
                "lengthRule",
                password.length >= 8
            );

            validateRule(
                "upperRule",
                /[A-Z]/.test(password)
            );

            validateRule(
                "numberRule",
                /[0-9]/.test(password)
            );

            validateRule(
                "specialRule",
                /[!@#$%^&*]/.test(password)
            );

        });

    function validateRule(id, valid){

        let element =
            document.getElementById(id);

        if(valid){

            element.classList.remove("invalid");
            element.classList.add("valid");

        }else{

            element.classList.remove("valid");
            element.classList.add("invalid");
        }
    }

    // PASSWORD MATCH CHECK

    document.getElementById("confirmPassword")
        .addEventListener("keyup", function(){

            let password =
                document.getElementById("password").value;

            let confirmPassword =
                this.value;

            let message =
                document.getElementById("passwordMatch");

            if(confirmPassword === ""){

                message.style.display = "none";

                return;
            }

            message.style.display = "block";

            if(password === confirmPassword){

                message.innerHTML =
                    "✓ Passwords match";

                message.className =
                    "validation-message success";

            }else{

                message.innerHTML =
                    "✗ Passwords do not match";

                message.className =
                    "validation-message error";
            }
        });

    // EMAIL VALIDATION

    function validateEmail(email){

        let pattern =
            /^[^ ]+@[^ ]+\.[a-z]{2,3}$/;

        return pattern.test(email);
    }

    // FORM VALIDATION

    function validateForm(){

        let email =
            document.getElementById("email").value;

        let password =
            document.getElementById("password").value;

        let confirmPassword =
            document.getElementById("confirmPassword").value;

        let emailError =
            document.getElementById("emailError");

        emailError.style.display = "none";

        // EMAIL CHECK

        if(!validateEmail(email)){

            emailError.innerHTML =
                "Enter valid email address";

            emailError.style.display =
                "block";

            return false;
        }

        // PASSWORD MATCH CHECK

        if(password !== confirmPassword){

            alert("Passwords do not match");

            return false;
        }

        return true;
    }

</script>

</body>
</html>