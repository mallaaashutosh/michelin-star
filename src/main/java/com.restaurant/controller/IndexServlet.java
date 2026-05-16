package com.restaurant.controller;

import com.restaurant.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"", "/site"})
public class IndexServlet extends HttpServlet {

    private static final String HOME_VIEW = "/WEB-INF/views/home.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;
        String ctx = request.getContextPath();

        if (user == null) {
            response.sendRedirect(ctx + "/login");
            return;
        }

        request.getRequestDispatcher(HOME_VIEW).forward(request, response);
    }
}
