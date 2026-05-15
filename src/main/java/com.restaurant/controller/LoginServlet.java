package com.restaurant.controller;

import com.restaurant.dao.CustomerDAO;
import com.restaurant.dao.CustomerDaoImpl;
import com.restaurant.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private CustomerDAO customerDAO = new CustomerDaoImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("userPortal/login.jsp").forward(request, response);
    }

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
            User user = customerDAO.loginUser(email, password);

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
