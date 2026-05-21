/*
 * RegisterServlet.java
 * Displays the sign-up form and creates new customer accounts.
 * Profile image upload has been removed - customers register with name, phone, email and password only.
 */

package com.restaurant.controller; // Controller package for servlet endpoints

import com.restaurant.dao.CustomerDAO; // Contract for customer registration and lookups
import com.restaurant.dao.CustomerDaoImpl; // JDBC implementation of customer data access
import com.restaurant.entity.User; // New account model passed to the DAO
import com.restaurant.utils.ValidationUtil; // Email and password strength checks
import com.restaurant.utils.PasswordHasher; // Hashes password before saving
import jakarta.servlet.ServletException; // Thrown when servlet forwarding fails
import jakarta.servlet.annotation.WebServlet; // Maps this class to /register
import jakarta.servlet.http.HttpServlet; // Base class for HTTP request handling
import jakarta.servlet.http.HttpServletRequest; // Incoming request and form fields
import jakarta.servlet.http.HttpServletResponse; // Outgoing response and redirects
import jakarta.servlet.http.HttpSession; // Used for flash message after successful signup

import java.io.IOException; // Thrown on redirect or I/O errors
import java.sql.SQLException; // Thrown when the database call fails

@WebServlet("/register") // Handles registration page GET and form POST
public class RegisterServlet extends HttpServlet { // Creates new customer accounts

    private final CustomerDAO customerDAO =
            new CustomerDaoImpl(); // DAO for register and email checks

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException { // Show sign-up form
        request.getRequestDispatcher("/register.jsp")
                .forward(request, response); // Render the registration JSP
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException { // Process sign-up submit

        String name = request.getParameter("name"); // Display name from the form
        String phone = request.getParameter("phone"); // Phone number from the form
        String email = request.getParameter("email"); // Email used as login id
        String password = request.getParameter("password"); // Plain-text password to hash
        String confirmPassword = request.getParameter("confirmPassword"); // Must match password
        String ctx = request.getContextPath(); // App context prefix for redirects

        // Core fields must not be empty
        if (name == null || name.isEmpty() ||
                email == null || email.isEmpty() ||
                password == null || password.isEmpty()) {
            request.setAttribute("error",
                    "All fields are required.");
            request.getRequestDispatcher("/register.jsp")
                    .forward(request, response);
            return;
        }

        // Name must only contain letters and spaces - no numbers allowed
        if (!name.matches("[a-zA-Z ]+")) {
            request.setAttribute("error",
                    "Name can only contain letters.");
            request.getRequestDispatcher("/register.jsp")
                    .forward(request, response);
            return;
        }

        // Phone must only contain digits if provided
        if (phone != null && !phone.isEmpty() &&
                !phone.matches("[0-9]+")) {
            request.setAttribute("error",
                    "Phone number can only contain digits.");
            request.getRequestDispatcher("/register.jsp")
                    .forward(request, response);
            return;
        }

        // Validate email format
        if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("error",
                    "Please enter a valid email address.");
            request.getRequestDispatcher("/register.jsp")
                    .forward(request, response);
            return;
        }

        // Validate password strength
        if (!ValidationUtil.isStrongPassword(password)) {
            request.setAttribute("error",
                    ValidationUtil.getPasswordRequirements());
            request.getRequestDispatcher("/register.jsp")
                    .forward(request, response);
            return;
        }

        // Passwords must match before we save
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error",
                    "Passwords do not match.");
            request.getRequestDispatcher("/register.jsp")
                    .forward(request, response);
            return;
        }

        try {
            // Avoid duplicate accounts with same email
            if (customerDAO.isEmailExists(email)) {
                request.setAttribute("error",
                        "Email already registered.");
                request.getRequestDispatcher("/register.jsp")
                        .forward(request, response);
                return;
            }

            String hashedPassword =
                    PasswordHasher.hashPassword(password); // Never store plain password

            User user = new User(
                    name, phone, email, hashedPassword); // Build entity for the DAO

            if (customerDAO.registerUser(user)) { // Insert succeeded
                HttpSession session =
                        request.getSession(true); // Session for one-time flash message
                session.setAttribute(
                        "flashRegisterSuccess",
                        "Registration successful! " +
                                "Please wait for admin approval."); // Admin must approve before login
                response.sendRedirect(
                        ctx + "/login"); // Send them to login with success hint

            } else { // Insert returned false
                request.setAttribute("error",
                        "Registration failed. Try again.");
                request.getRequestDispatcher("/register.jsp")
                        .forward(request, response);
            }

        } catch (SQLException e) { // Database error during register or email check
            e.printStackTrace();
            request.setAttribute("error",
                    "Database error: " + e.getMessage());
            request.getRequestDispatcher("/register.jsp")
                    .forward(request, response);
        }
    }
}