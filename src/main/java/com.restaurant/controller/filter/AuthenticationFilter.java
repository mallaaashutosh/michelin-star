/*
 * AuthenticationFilter.java
 * Guards every /admin/* URL: guests go to login; logged-in non-admins are sent to the customer home page.
 */
package com.restaurant.controller.filter; // servlet filters package

import com.restaurant.entity.User; // session user with admin flag
import jakarta.servlet.Filter; // filter contract
import jakarta.servlet.FilterChain; // continue to servlet or next filter
import jakarta.servlet.ServletException; // filter/servlet errors
import jakarta.servlet.ServletRequest; // generic request (cast to HTTP)
import jakarta.servlet.ServletResponse; // generic response (cast to HTTP)
import jakarta.servlet.annotation.WebFilter; // apply to URL pattern without web.xml
import jakarta.servlet.http.HttpServletRequest; // HTTP-specific request
import jakarta.servlet.http.HttpServletResponse; // redirects need HTTP response
import jakarta.servlet.http.HttpSession; // where login stores User
import java.io.IOException; // I/O on redirect

@WebFilter("/admin/*") // runs before any admin servlet or JSP under /admin
public class AuthenticationFilter implements Filter { // admin-area gatekeeper

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) // entry for each admin request
            throws IOException, ServletException { // redirect or chain may throw

        HttpServletRequest httpRequest = (HttpServletRequest) request; // need HTTP APIs
        HttpServletResponse httpResponse = (HttpServletResponse) response; // for sendRedirect
        HttpSession session = httpRequest.getSession(false); // do not create session on anonymous hits

        User user = session != null ? (User) session.getAttribute("user") : null; // null if not logged in

        if (user == null) { // anonymous user hitting /admin
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login"); // must sign in first
            return; // do not continue filter chain
        }

        if (!user.isAdmin()) { // customer logged in but not staff
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/home"); // customer area instead
            return; // block admin access
        }

        chain.doFilter(request, response); // admin OK — proceed to servlet/JSP
    }
}
