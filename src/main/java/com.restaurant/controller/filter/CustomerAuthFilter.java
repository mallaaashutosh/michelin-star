/*
 * CustomerAuthFilter.java
 * Requires a signed-in user before /menu and /home; sets a flash message and redirects guests to login.
 */
package com.restaurant.controller.filter; // filters package

import com.restaurant.entity.User; // logged-in account from session
import jakarta.servlet.Filter; // filter interface
import jakarta.servlet.FilterChain; // pass request along when allowed
import jakarta.servlet.ServletException; // servlet pipeline errors
import jakarta.servlet.ServletRequest; // incoming request wrapper
import jakarta.servlet.ServletResponse; // outgoing response wrapper
import jakarta.servlet.annotation.WebFilter; // URL patterns to protect
import jakarta.servlet.http.HttpServletRequest; // HTTP request for session and path
import jakarta.servlet.http.HttpServletResponse; // HTTP redirect
import jakarta.servlet.http.HttpSession; // login session
import java.io.IOException; // I/O on redirect

/**
 * Requires a logged-in user for customer routes (menu, etc.).
 * Unauthenticated users are redirected to the login page.
 */
@WebFilter(urlPatterns = {"/menu", "/home"}) // only these customer entry points
public class CustomerAuthFilter implements Filter { // blocks guests from menu and home

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) // runs before menu/home servlets
            throws IOException, ServletException { // declared throws

        HttpServletRequest httpRequest = (HttpServletRequest) request; // cast for session and context path
        HttpServletResponse httpResponse = (HttpServletResponse) response; // cast for redirect
        HttpSession session = httpRequest.getSession(false); // read existing session without creating one

        User user = session != null ? (User) session.getAttribute("user") : null; // null means not logged in

        if (user == null) { // guest tried to open menu or home
            String ctx = httpRequest.getContextPath(); // app prefix for redirect URL
            HttpSession writeSession = httpRequest.getSession(true); // create session to store flash message
            writeSession.setAttribute("flashAuthRequired", "Please sign in to continue."); // shown once on login page
            httpResponse.sendRedirect(ctx + "/login"); // send to login
            return; // stop — do not serve protected page
        }

        chain.doFilter(request, response); // logged in — continue to MenuServlet or home
    }
}
