package com.restaurant.controller.filter;

import com.restaurant.entity.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Requires a logged-in user for customer routes (menu, etc.).
 * Unauthenticated users are redirected to the login page.
 */
@WebFilter(urlPatterns = {"/menu", "/home"})
public class CustomerAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        User user = session != null ? (User) session.getAttribute("user") : null;

        if (user == null) {
            String ctx = httpRequest.getContextPath();
            HttpSession writeSession = httpRequest.getSession(true);
            writeSession.setAttribute("flashAuthRequired", "Please sign in to continue.");
            httpResponse.sendRedirect(ctx + "/login");
            return;
        }

        chain.doFilter(request, response);
    }
}
