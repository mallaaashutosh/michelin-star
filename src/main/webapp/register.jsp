<%--
    File: register.jsp
    Purpose: New customer registration page for Michelin Star
    Collects name, phone, email, and password, then POSTs to /register
    Client-side validates email, password strength, and matching confirmation
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register - Michelin Star</title>
    <style>
        /* Center everything - signup form should be the focus */
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f4f4f9;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            padding: 2rem 0;
        }

        /* Clean white card for the form */
        .register-container {
            background: white;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 500px;
        }

        h2 {
            text-align: center;
            color: #333;
            margin-top: 0;
        }

        /* Spacing between form fields */
        .form-group {
            margin-bottom: 1rem;
        }

        label {
            display: block;
            margin-bottom: 0.5rem;
            color: #666;
            font-weight: 600;
        }

        /* Full width inputs with consistent padding */
        input[type="text"],
        input[type="email"],
        input[type="password"],
        input[type="tel"],
        input[type="file"] {
            width: 100%;
            padding: 0.75rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }

        /* Password field with show/hide toggle */
        .password-wrapper {
            position: relative;
            width: 100%;
        }

        .password-toggle {
            position: absolute;
            right: 12px;
            top: 50%;
            transform: translateY(-50%);
            cursor: pointer;
            font-size: 18px;
            user-select: none;
            color: #666;
            transition: color 0.3s;
        }

        .password-toggle:hover {
            color: #333;
        }

        /* Make room for the eye icon */
        .password-wrapper input[type="password"],
        #password,
        #confirmPassword {
            padding-right: 40px;
        }

        /* Primary submit button - green means go */
        .btn {
            width: 100%;
            padding: 0.75rem;
            border: none;
            border-radius: 4px;
            background: #28a745;
            color: white;
            font-size: 1rem;
            cursor: pointer;
            transition: background 0.3s;
            margin-top: 1rem;
        }

        .btn:hover {
            background: #218838;
        }

        /* Error and success message styles */
        .error {
            color: #d9534f;
            background: #f2dede;
            padding: 0.5rem;
            border-radius: 4px;
            margin-bottom: 1rem;
            font-size: 0.9rem;
            text-align: center;
        }

        .footer {
            text-align: center;
            margin-top: 1rem;
            font-size: 0.9rem;
            color: #777;
        }

        .footer a {
            color: #007bff;
            text-decoration: none;
        }

        /* Validation feedback messages */
        .validation-error {
            color: #d9534f;
            font-size: 12px;
            margin-top: 5px;
            display: none;
        }

        .validation-error.show {
            display: block;
        }

        .validation-success {
            color: #28a745;
            font-size: 12px;
            margin-top: 5px;
            display: none;
        }

        .validation-success.show {
            display: block;
        }

        /* Password requirements checklist */
        .validation-info {
            color: #666;
            font-size: 12px;
            margin-top: 5px;
            line-height: 1.4;
        }

        .requirement {
            margin: 3px 0;
        }

        .requirement.met {
            color: #28a745;
        }

        .requirement.unmet {
            color: #d9534f;
        }
    </style>

    <script>
        // Basic email format check - prevents obvious typos
        function validateEmail(email) {
            const emailPattern = /^[A-Za-z0-9+_.-]+@(.+)$/;
            return emailPattern.test(email);
        }

        // Check password against all our security rules
        function validatePasswordStrength(password) {
            const requirements = {
                length: password.length >= 8,           // Minimum 8 characters
                uppercase: /[A-Z]/.test(password),      // At least one capital letter
                lowercase: /[a-z]/.test(password),      // At least one lowercase
                number: /\d/.test(password),            // At least one number
                special: /[@$!%*?&]/.test(password)     // At least one special character
            };
            return requirements;
        }

        // Update the password rules checklist as user types - instant feedback
        function updatePasswordRequirements() {
            const password = document.getElementById('password').value;
            const requirements = validatePasswordStrength(password);

            // Update each requirement indicator
            const reqLength = document.getElementById('reqLength');
            const reqUppercase = document.getElementById('reqUppercase');
            const reqLowercase = document.getElementById('reqLowercase');
            const reqNumber = document.getElementById('reqNumber');
            const reqSpecial = document.getElementById('reqSpecial');

            updateRequirement(reqLength, requirements.length, '8+ characters');
            updateRequirement(reqUppercase, requirements.uppercase, 'Uppercase letter (A-Z)');
            updateRequirement(reqLowercase, requirements.lowercase, 'Lowercase letter (a-z)');
            updateRequirement(reqNumber, requirements.number, 'Number (0-9)');
            updateRequirement(reqSpecial, requirements.special, 'Special character (@$!%*?&)');

            checkPasswordMatch(); // Also update the confirm password message
        }

        // Helper to style each requirement line
        function updateRequirement(element, met, text) {
            element.innerText = text;
            if (met) {
                element.classList.remove('unmet');
                element.classList.add('met');
            } else {
                element.classList.remove('met');
                element.classList.add('unmet');
            }
        }

        // Make sure password and confirm password match
        function checkPasswordMatch() {
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            const passwordMatchError = document.getElementById('passwordMatchError');

            // Don't show match UI until user starts typing confirmation
            if (confirmPassword === '') {
                passwordMatchError.classList.remove('show');
                return;
            }

            if (password === confirmPassword) {
                passwordMatchError.innerHTML = '✓ Passwords match';
                passwordMatchError.classList.remove('validation-error');
                passwordMatchError.classList.add('validation-success');
                passwordMatchError.classList.add('show');
            } else {
                passwordMatchError.innerHTML = '✗ Passwords do not match';
                passwordMatchError.classList.remove('validation-success');
                passwordMatchError.classList.add('validation-error');
                passwordMatchError.classList.add('show');
            }
        }

        // Toggle password field between visible and hidden
        function togglePasswordVisibility(inputId, toggleId) {
            const input = document.getElementById(inputId);
            const toggle = document.getElementById(toggleId);

            if (input.type === 'password') {
                input.type = 'text';
                toggle.innerText = '👁️';      // Open eye icon
            } else {
                input.type = 'password';
                toggle.innerText = '👁️‍🗨️';    // Closed/slashed eye icon
            }
        }

        // Phone numbers should only contain digits - strip everything else
        function validatePhoneNumber(event) {
            const input = event.target;
            input.value = input.value.replace(/[^0-9]/g, '');
        }

        // Final validation before submitting - blocks submission if anything's wrong
        function validateRegisterForm(event) {
            const email = document.getElementById('email').value.trim();
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            const emailError = document.getElementById('emailError');
            const passwordError = document.getElementById('passwordError');

            let isValid = true;

            // Check email format
            if (!validateEmail(email)) {
                emailError.innerText = "Please enter a valid email address";
                emailError.classList.add('show');
                isValid = false;
            } else {
                emailError.classList.remove('show');
            }

            // Check password meets all requirements
            const requirements = validatePasswordStrength(password);
            const allMet = requirements.length && requirements.uppercase &&
                requirements.lowercase && requirements.number && requirements.special;
            if (!allMet) {
                passwordError.innerText = "Password does not meet all requirements";
                passwordError.classList.add('show');
                isValid = false;
            } else {
                passwordError.classList.remove('show');
            }

            // Check passwords match each other
            if (password !== confirmPassword) {
                passwordError.innerText = "Passwords do not match";
                passwordError.classList.add('show');
                isValid = false;
            }

            if (!isValid) {
                event.preventDefault(); // Stop the form from submitting
            }
        }

        // Set up all event listeners once the page is ready
        document.addEventListener('DOMContentLoaded', function() {
            // Form submission validation
            const registerForm = document.querySelector('form');
            if (registerForm) {
                registerForm.addEventListener('submit', validateRegisterForm);
            }

            // Validate email when user leaves the field (blur)
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

            // Phone number - numbers only
            const phoneInput = document.getElementById('phone');
            if (phoneInput) {
                phoneInput.addEventListener('input', validatePhoneNumber);
            }

            // Live password strength checking
            const passwordInput = document.getElementById('password');
            if (passwordInput) {
                passwordInput.addEventListener('input', updatePasswordRequirements);
            }

            // Live confirm password checking
            const confirmPasswordInput = document.getElementById('confirmPassword');
            if (confirmPasswordInput) {
                confirmPasswordInput.addEventListener('input', checkPasswordMatch);
            }

            // Password show/hide toggles
            const passwordToggle = document.getElementById('passwordToggle');
            if (passwordToggle) {
                passwordToggle.addEventListener('click', function() {
                    togglePasswordVisibility('password', 'passwordToggle');
                });
            }

            const confirmPasswordToggle = document.getElementById('confirmPasswordToggle');
            if (confirmPasswordToggle) {
                confirmPasswordToggle.addEventListener('click', function() {
                    togglePasswordVisibility('confirmPassword', 'confirmPasswordToggle');
                });
            }
        });
    </script>
</head>
<body>

<div class="register-container">
    <h2>Create Account</h2>

    <!-- Display any server-side errors (duplicate email, validation failures, etc.) -->
    <% if (request.getAttribute("error") != null) { %>
    <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/register" method="post">

        <!-- Full name - required -->
        <div class="form-group">
            <label for="name">Full Name</label>
            <input type="text" id="name" name="name" required>
        </div>

        <!-- Phone number - optional but nice to have for reservations -->
        <div class="form-group">
            <label for="phone">Phone Number</label>
            <input type="tel" id="phone" name="phone" placeholder="Enter only numbers" inputmode="numeric">
        </div>

        <!-- Email address - used for login -->
        <div class="form-group">
            <label for="email">Email Address</label>
            <input type="email" id="email" name="email" required>
            <div id="emailError" class="validation-error"></div>
        </div>

        <!-- Password with strength meter -->
        <div class="form-group">
            <label for="password">Password</label>
            <div class="password-wrapper">
                <input type="password" id="password" name="password" required>
                <span id="passwordToggle" class="password-toggle">👁️‍🗨️</span>
            </div>
            <div id="passwordError" class="validation-error"></div>

            <!-- Live password requirements checklist -->
            <div class="validation-info">
                Password must contain:
                <div id="reqLength" class="requirement unmet">✓ 8+ characters</div>
                <div id="reqUppercase" class="requirement unmet">✓ Uppercase letter (A-Z)</div>
                <div id="reqLowercase" class="requirement unmet">✓ Lowercase letter (a-z)</div>
                <div id="reqNumber" class="requirement unmet">✓ Number (0-9)</div>
                <div id="reqSpecial" class="requirement unmet">✓ Special character (@$!%*?&)</div>
            </div>
        </div>

        <!-- Confirm password - catches typos -->
        <div class="form-group">
            <label for="confirmPassword">Confirm Password</label>
            <div class="password-wrapper">
                <input type="password" id="confirmPassword" name="confirmPassword" required>
                <span id="confirmPasswordToggle" class="password-toggle">👁️‍🗨️</span>
            </div>
            <div id="passwordMatchError" class="validation-error"></div>
        </div>

        <button type="submit" class="btn">Register</button>
    </form>

    <!-- Link back to login for existing users -->
    <div class="footer">
        Already have an account? <a href="${pageContext.request.contextPath}/login">Login here</a>
    </div>
</div>

</body>
</html>