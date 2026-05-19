/*
 * LoginServlet.java
 * Shows the login form and authenticates customers and admins against the database.
 */

package com.restaurant.controller; // Controller package for servlet endpoints

import com.restaurant.dao.CustomerDAO; // Contract for customer persistence and login
import com.restaurant.dao.CustomerDaoImpl; // JDBC implementation of customer data access
import com.restaurant.entity.User; // Represents a logged-in account
import com.restaurant.utils.ValidationUtil; // Shared email and input validation helpers
import jakarta.servlet.ServletException; // Thrown when servlet forwarding fails
import jakarta.servlet.annotation.WebServlet; // Maps this class to /login
import jakarta.servlet.http.Cookie; // Optional "remember me" email cookie
import jakarta.servlet.http.HttpServlet; // Base class for HTTP request handling
import jakarta.servlet.http.HttpServletRequest; // Incoming request data
import jakarta.servlet.http.HttpServletResponse; // Outgoing response to the browser
import jakarta.servlet.http.HttpSession; // Stores the user after successful login
import java.io.IOException; // Thrown on redirect or I/O errors
import java.sql.SQLException; // Thrown when the database call fails

@WebServlet("/login") // Handles login page GET and form POST
public class LoginServlet extends HttpServlet { // Authenticates users and routes by role
    private final CustomerDAO customerDAO = new CustomerDaoImpl(); // DAO used to look up credentials

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { // Show login form
        request.getRequestDispatcher("/login.jsp").forward(request, response); // Render the login JSP
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { // Process login submit
        String email = request.getParameter("email"); // Email from the form
        String password = request.getParameter("password"); // Plain-text password from the form
        String rememberMe = request.getParameter("rememberMe"); // Checkbox value for persistent email cookie
        String ctx = request.getContextPath(); // App context prefix for redirects and cookie path

        HttpSession session = request.getSession(true); // Create or reuse session for storing the user

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) { // Both fields required
            request.setAttribute("error", "Email and Password are required."); // Message shown on the form
            request.getRequestDispatcher("/login.jsp").forward(request, response); // Back to login with error
            return; // Stop processing
        }

        // Validate email format
        if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("error", "Please enter a valid email address.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
            User user = customerDAO.loginUser(email, password); // Check credentials in the database

            if (user != null) { // Login succeeded
                session.setAttribute("user", user); // Keep user in session for later pages

                if ("on".equals(rememberMe)) { // User checked "remember me"
                    Cookie emailCookie = new Cookie("userEmail", email); // Store email in a browser cookie
                    emailCookie.setMaxAge(60 * 60 * 24 * 7); // Keep cookie for seven days
                    emailCookie.setPath(ctx.isEmpty() ? "/" : ctx); // Scope cookie to this app context
                    response.addCookie(emailCookie); // Send cookie to the browser
                } else { // User did not want to be remembered
                    Cookie emailCookie = new Cookie("userEmail", ""); // Clear any previous remember-me value
                    emailCookie.setMaxAge(0); // Expire the cookie immediately
                    emailCookie.setPath(ctx.isEmpty() ? "/" : ctx); // Same path as when it was set
                    response.addCookie(emailCookie); // Apply the cleared cookie
                }

                if (user.isAdmin()) { // Admin accounts go to the dashboard
                    response.sendRedirect(ctx + "/admin/dashboard");
                } else { // Regular customers go to the site home
                    response.sendRedirect(ctx + "/site");
                }
            } else { // Wrong email or password
                request.setAttribute("error", "Invalid email or password.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (SQLException e) { // Database unreachable or query failed
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
