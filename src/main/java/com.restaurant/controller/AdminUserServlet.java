/*
 * AdminUserServlet.java
 * Admin user management: list registered customers and approve or change account status.
 */
package com.restaurant.controller; // controller package

import com.restaurant.dao.CustomerDAO; // user listing and status updates
import com.restaurant.dao.CustomerDaoImpl; // JDBC implementation
import com.restaurant.entity.User; // customer/admin account model
import jakarta.servlet.ServletException; // servlet layer errors
import jakarta.servlet.annotation.WebServlet; // /admin/users URL
import jakarta.servlet.http.HttpServlet; // base servlet
import jakarta.servlet.http.HttpServletRequest; // HTTP request
import jakarta.servlet.http.HttpServletResponse; // HTTP response
import jakarta.servlet.http.HttpSession; // session for admin check
import java.io.IOException; // I/O on redirect/forward
import java.sql.SQLException; // database errors from DAO
import java.util.List; // list of users for the JSP

@WebServlet("/admin/users") // manage users under admin area
public class AdminUserServlet extends HttpServlet { // list and update customer accounts

    private final CustomerDAO customerDAO = new CustomerDaoImpl(); // created once per servlet instance

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) // show user table
            throws ServletException, IOException { // declared throws

        if (!isAdmin(request)) { // only admins
            response.sendRedirect(request.getContextPath() + "/login"); // redirect strangers
            return; // stop
        }

        try { // load users from DB
            List<User> users = customerDAO.findAllUsers(); // every registered user
            request.setAttribute("users", users); // JSP renders the table
            request.getRequestDispatcher("/views/admin/users.jsp").forward(request, response); // success view
        } catch (SQLException e) { // DB unreachable or query failed
            e.printStackTrace(); // log for server console
            request.setAttribute("error", "Could not load users."); // friendly message on page
            request.getRequestDispatcher("/views/admin/users.jsp").forward(request, response); // still show page with error
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) // status changes from form
            throws ServletException, IOException { // throws

        if (!isAdmin(request)) { // guard POST too
            response.sendRedirect(request.getContextPath() + "/login"); // login required
            return; // end
        }

        String action = request.getParameter("action"); // e.g. updateStatus
        if ("updateStatus".equals(action)) { // approve / reject / activate
            int userId = Integer.parseInt(request.getParameter("userId")); // which account
            String status = request.getParameter("status"); // new status value
            try { // persist change
                customerDAO.updateUserStatus(userId, status); // UPDATE status column
            } catch (SQLException e) { // failed update
                e.printStackTrace(); // log error
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/users"); // PRG back to user list
    }

    private boolean isAdmin(HttpServletRequest request) { // same pattern as other admin servlets
        HttpSession session = request.getSession(false); // existing session only
        if (session == null) { // no session
            return false; // not admin
        }
        User user = (User) session.getAttribute("user"); // current user
        return user != null && user.isAdmin(); // admin role required
    }
}
