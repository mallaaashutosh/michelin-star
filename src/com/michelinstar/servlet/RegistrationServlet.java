package com.michelinstar.servlet;

import com.michelinstar.dao.UserDAO;
import com.michelinstar.model.User;
import com.michelinstar.util.PasswordHasher;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50   // 50MB
)
public class RegistrationServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    private static final String UPLOAD_DIR = "uploads";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validation
        if (name == null || name.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("userPortal/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match.");
            request.getRequestDispatcher("userPortal/register.jsp").forward(request, response);
            return;
        }

        try {
            if (userDAO.isEmailExists(email)) {
                request.setAttribute("error", "Email already registered.");
                request.getRequestDispatcher("userPortal/register.jsp").forward(request, response);
                return;
            }

            // Handle file upload
            String fileName = "";
            Part filePart = request.getPart("profileImage");
            if (filePart != null && filePart.getSize() > 0) {
                String applicationPath = request.getServletContext().getRealPath("");
                String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
                
                File uploadDir = new File(uploadFilePath);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                fileName = System.currentTimeMillis() + "_" + getFileName(filePart);
                filePart.write(uploadFilePath + File.separator + fileName);
            }

            String hashedPassword = PasswordHasher.hashPassword(password);
            User user = new User(name, phone, email, hashedPassword, fileName);

            if (userDAO.registerUser(user)) {
                request.setAttribute("success", "Registration successful! Please login.");
                response.sendRedirect("login.jsp");
            } else {
                request.setAttribute("error", "Registration failed. Try again.");
                request.getRequestDispatcher("userPortal/register.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("userPortal/register.jsp").forward(request, response);
        }
    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }
}
