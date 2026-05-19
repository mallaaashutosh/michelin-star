/*
 * AdminMenuServlet.java
 * Back-office menu management: list items, add/edit/delete dishes; only admins may use these routes.
 */
package com.restaurant.controller; // admin controllers

import com.restaurant.dao.MenuDaoImpl; // menu persistence
import com.restaurant.entity.MenuItem; // dish row
import com.restaurant.entity.User; // session user for role check
import jakarta.servlet.ServletException; // servlet errors
import jakarta.servlet.annotation.WebServlet; // /admin/menu mapping
import jakarta.servlet.http.HttpServlet; // HTTP servlet
import jakarta.servlet.http.HttpServletRequest; // request
import jakarta.servlet.http.HttpServletResponse; // response
import jakarta.servlet.http.HttpSession; // login session
import java.io.IOException; // I/O
import java.util.ArrayList; // all menu items for the manage page

@WebServlet("/admin/menu") // admin menu CRUD UI
public class AdminMenuServlet extends HttpServlet { // manage menu under /admin/menu

    private MenuDaoImpl menuDao; // DAO for menu table

    @Override
    public void init() { // once at deploy
        menuDao = new MenuDaoImpl(); // instantiate DAO
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) // show list, edit form, or delete
            throws ServletException, IOException { // throws

        if (!isAdmin(request)) { // must be logged-in admin
            response.sendRedirect(request.getContextPath() + "/login"); // send to login
            return; // stop here
        }

        String action = request.getParameter("action"); // delete, edit, or default list

        if ("delete".equals(action)) { // remove one item
            int menuId = Integer.parseInt(request.getParameter("id")); // id from query string
            menuDao.deleteMenuItem(menuId); // DELETE
            response.sendRedirect(request.getContextPath() + "/admin/menu"); // back to manage page
            return; // done
        }

        if ("edit".equals(action)) { // preload form for one item
            int menuId = Integer.parseInt(request.getParameter("id")); // which item to edit
            MenuItem item = menuDao.findMenuItemById(menuId); // load from DB
            request.setAttribute("editItem", item); // JSP shows edit fields
        }

        ArrayList<MenuItem> menuItems = menuDao.fetchAllMenuItems(); // full list for table
        request.setAttribute("menuItems", menuItems); // pass to JSP
        request.getRequestDispatcher("/views/admin/menu-manage.jsp").forward(request, response); // render admin menu page
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) // create or update item
            throws ServletException, IOException { // throws

        if (!isAdmin(request)) { // guard again on POST
            response.sendRedirect(request.getContextPath() + "/login"); // not allowed
            return; // abort
        }

        String action = request.getParameter("action"); // add or edit
        String ctx = request.getContextPath(); // redirect prefix

        if ("add".equals(action)) { // new menu row
            MenuItem newItem = new MenuItem( // from admin form
                    request.getParameter("name"), // name
                    request.getParameter("category"), // category
                    Double.parseDouble(request.getParameter("price")), // price
                    nullToEmpty(request.getParameter("image")), // image filename/path
                    request.getParameter("availability")); // availability
            menuDao.insertMenuItem(newItem); // INSERT
        } else if ("edit".equals(action)) { // update row
            int menuId = Integer.parseInt(request.getParameter("menuId")); // id
            MenuItem existing = menuDao.findMenuItemById(menuId); // old row for image
            String image = request.getParameter("image"); // submitted image
            if (image == null || image.isBlank()) { // blank means keep previous
                image = existing != null ? existing.getImage() : ""; // fallback
            }
            MenuItem item = new MenuItem( // updated entity
                    menuId, // id
                    request.getParameter("name"), // name
                    request.getParameter("category"), // category
                    Double.parseDouble(request.getParameter("price")), // price
                    image, // final image path
                    request.getParameter("availability")); // availability
            menuDao.updateMenuItem(item); // UPDATE
        }

        response.sendRedirect(ctx + "/admin/menu"); // PRG to list after save
    }

    private boolean isAdmin(HttpServletRequest request) { // session role check
        HttpSession session = request.getSession(false); // do not create session just to check
        if (session == null) { // not logged in
            return false; // deny
        }
        User user = (User) session.getAttribute("user"); // logged-in user
        return user != null && user.isAdmin(); // must exist and have admin flag
    }

    private static String nullToEmpty(String value) { // safe string for optional image field
        return value == null ? "" : value; // never pass null to DAO
    }
}
