/*
 * RegisterServlet.java
 * Displays the sign-up form and creates new customer accounts with optional profile photo.
 */

package com.restaurant.controller; // Controller package for servlet endpoints

import com.restaurant.dao.CustomerDAO; // Contract for customer registration and lookups
import com.restaurant.dao.CustomerDaoImpl; // JDBC implementation of customer data access
import com.restaurant.entity.User; // New account model passed to the DAO
import com.restaurant.utils.ValidationUtil; // Email and password strength checks
import jakarta.servlet.ServletException; // Thrown when servlet forwarding fails
import jakarta.servlet.annotation.MultipartConfig; // Allows file upload on registration
import jakarta.servlet.annotation.WebServlet; // Maps this class to /register
import jakarta.servlet.http.HttpServlet; // Base class for HTTP request handling
import jakarta.servlet.http.HttpServletRequest; // Incoming request and form fields
import jakarta.servlet.http.HttpServletResponse; // Outgoing response and redirects
import jakarta.servlet.http.HttpSession; // Used for flash message after successful signup
import jakarta.servlet.http.Part; // One uploaded file part from the form
import com.restaurant.utils.PasswordHasher; // Hashes password before saving

import java.io.File; // Paths and directories for profile image storage
import java.io.IOException; // Thrown on redirect, write, or I/O errors
import java.sql.SQLException; // Thrown when the database call fails

@WebServlet("/register") // Handles registration page GET and form POST
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // Spill to disk after 2 MB in memory
        maxFileSize = 1024 * 1024 * 10, // Single file capped at 10 MB
        maxRequestSize = 1024 * 1024 * 50 // Entire multipart request capped at 50 MB
)
public class RegisterServlet extends HttpServlet { // Creates new customer accounts
    private final CustomerDAO customerDAO = new CustomerDaoImpl(); // DAO for register and email checks
    private static final String UPLOAD_DIR = "uploads"; // Subfolder under webapp for profile images

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { // Show sign-up form
        request.getRequestDispatcher("/register.jsp").forward(request, response); // Render the registration JSP
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { // Process sign-up submit
        String name = request.getParameter("name"); // Display name from the form
        String phone = request.getParameter("phone"); // Phone number (optional in validation below)
        String email = request.getParameter("email"); // Email used as login id
        String password = request.getParameter("password"); // Plain-text password to hash
        String confirmPassword = request.getParameter("confirmPassword"); // Must match password
        String ctx = request.getContextPath(); // App context prefix for redirects

        if (name == null || name.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty()) { // Core fields required
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Validate email format
        if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("error", "Please enter a valid email address.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Validate password strength
        if (!ValidationUtil.isStrongPassword(password)) {
            request.setAttribute("error", ValidationUtil.getPasswordRequirements());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) { // Passwords must match before we save
            request.setAttribute("error", "Passwords do not match.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        try {
            if (customerDAO.isEmailExists(email)) { // Avoid duplicate accounts
                request.setAttribute("error", "Email already registered.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            String fileName = ""; // Empty until user uploads a profile image
            Part filePart = request.getPart("profileImage"); // Optional image from multipart form
            if (filePart != null && filePart.getSize() > 0) { // User chose a file
                String applicationPath = request.getServletContext().getRealPath(""); // Webapp root on disk
                String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR; // Full path to uploads folder

                File uploadDir = new File(uploadFilePath); // Directory object for uploads
                if (!uploadDir.exists()) { // First upload may need the folder
                    uploadDir.mkdirs(); // Create uploads and any parent dirs
                }

                fileName = System.currentTimeMillis() + "_" + getFileName(filePart); // Unique name to avoid clashes
                filePart.write(uploadFilePath + File.separator + fileName); // Save file to disk
            }

            String hashedPassword = PasswordHasher.hashPassword(password); // Never store plain password
            User user = new User(name, phone, email, hashedPassword); // Build entity for the DAO
            user.setProfileImage(fileName); // Store filename only; file lives under uploads

            if (customerDAO.registerUser(user)) { // Insert succeeded
                HttpSession session = request.getSession(true); // Session for one-time flash message
                session.setAttribute("flashRegisterSuccess", "Registration successful! Please sign in.");
                response.sendRedirect(ctx + "/login"); // Send them to login with success hint
            } else { // Insert returned false
                request.setAttribute("error", "Registration failed. Try again.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        } catch (SQLException e) { // Database error during register or email check
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    private String getFileName(Part part) { // Pull original filename from multipart headers
        String contentDisp = part.getHeader("content-disposition"); // Raw Content-Disposition header
        String[] tokens = contentDisp.split(";"); // Split into key=value pieces
        for (String token : tokens) { // Find the filename parameter
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1); // Strip quotes around name
            }
        }
        return ""; // No filename found in header
    }
}
