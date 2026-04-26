package com.michelinstar.servlet;

import com.michelinstar.dao.UserDAO;
import com.michelinstar.model.User;
import com.michelinstar.util.PasswordHasher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "Email and Password are required.");
            request.getRequestDispatcher("userPortal/login.jsp").forward(request, response);
            return;
        }

        try {
            String hashedPassword = PasswordHasher.hashPassword(password);
            User user = userDAO.loginUser(email, hashedPassword);

            if (user != null) {
                // Session management
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                // Cookie management
                if ("on".equals(rememberMe)) {
                    Cookie emailCookie = new Cookie("userEmail", email);
                    emailCookie.setMaxAge(60 * 60 * 24 * 7); // 1 week
                    response.addCookie(emailCookie);
                } else {
                    Cookie emailCookie = new Cookie("userEmail", "");
                    emailCookie.setMaxAge(0);
                    response.addCookie(emailCookie);
                }

                response.sendRedirect("home.jsp");
            } else {
                request.setAttribute("error", "Invalid email or password.");
                request.getRequestDispatcher("userPortal/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("userPortal/login.jsp").forward(request, response);
        }
    }
}
