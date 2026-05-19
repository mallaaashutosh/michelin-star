/*
 * MenuServlet.java
 * Customer-facing menu: list all items, filter by category, search by name, and (legacy paths) add/edit/delete via the same servlet.
 */
package com.restaurant.controller; // HTTP controllers

import com.restaurant.dao.MenuDaoImpl; // loads and mutates menu rows
import com.restaurant.entity.MenuItem; // one dish on the menu
import jakarta.servlet.ServletException; // forward/dispatch failures
import jakarta.servlet.annotation.WebServlet; // maps to /menu
import jakarta.servlet.http.HttpServlet; // servlet base
import jakarta.servlet.http.HttpServletRequest; // request with params
import jakarta.servlet.http.HttpServletResponse; // redirects and forwards
import java.io.IOException; // I/O errors
import java.util.ArrayList; // list of menu items for the JSP

@WebServlet("/menu") // public menu URL
public class MenuServlet extends HttpServlet { // serves menu browsing (and some CRUD hooks)

    private static final String MENU_VIEW = "/views/customer/menu.jsp"; // JSP path for the menu page

    private MenuDaoImpl menuDao; // database access for menu items

    @Override
    public void init() { // servlet startup
        menuDao = new MenuDaoImpl(); // wire DAO once
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) // read menu or delete
            throws IOException, ServletException { // declared throws

        String action = request.getParameter("action"); // optional: category, search, delete
        String ctx = request.getContextPath(); // app context prefix for redirects

        if (action == null) { // no action — show full menu
            forwardMenu(request, response, menuDao.fetchAllMenuItems(), null, null); // all items, no filters
        } else if ("category".equals(action)) { // filter by category tab/link
            String category = request.getParameter("category"); // e.g. Nepali, Drinks
            forwardMenu(request, response, menuDao.fetchMenuItemsByCategory(category), category, null); // filtered list
        } else if ("search".equals(action)) { // name search
            String keyword = request.getParameter("keyword"); // user typed this
            forwardMenu(request, response, menuDao.searchMenuItemsByName(keyword), null, keyword); // search results
        } else if ("delete".equals(action)) { // remove item (if exposed on customer menu)
            int menuId = Integer.parseInt(request.getParameter("id")); // which row to delete
            menuDao.deleteMenuItem(menuId); // delete in DB
            response.sendRedirect(ctx + "/menu"); // reload menu list
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) // add or edit menu item
            throws IOException, ServletException { // servlet exceptions

        String action = request.getParameter("action"); // add or edit
        String ctx = request.getContextPath(); // for redirect after POST

        if ("add".equals(action)) { // new dish
            MenuItem newItem = new MenuItem( // build entity from form fields
                    request.getParameter("name"), // dish name
                    request.getParameter("category"), // category label
                    Double.parseDouble(request.getParameter("price")), // price as number
                    nullToEmpty(request.getParameter("image")), // image path, never null
                    request.getParameter("availability")); // in stock / unavailable
            menuDao.insertMenuItem(newItem); // INSERT
        } else if ("edit".equals(action)) { // update existing
            int menuId = Integer.parseInt(request.getParameter("menuId")); // primary key
            MenuItem existing = menuDao.findMenuItemById(menuId); // load current row for image fallback
            String image = request.getParameter("image"); // new image path from form
            if (image == null || image.isBlank()) { // user left image blank
                image = existing != null ? existing.getImage() : ""; // keep old image or empty
            }
            MenuItem item = new MenuItem( // full row for update
                    menuId, // id
                    request.getParameter("name"), // name
                    request.getParameter("category"), // category
                    Double.parseDouble(request.getParameter("price")), // price
                    image, // resolved image path
                    request.getParameter("availability")); // availability flag
            menuDao.updateMenuItem(item); // UPDATE
        }

        response.sendRedirect(ctx + "/menu"); // PRG back to menu after POST
    }

    private void forwardMenu(HttpServletRequest request, HttpServletResponse response, // shared forward helper
                             ArrayList<MenuItem> menuItems, String category, String keyword) // items plus optional UI state
            throws ServletException, IOException { // forward can fail
        request.setAttribute("menuItems", menuItems); // JSP iterates this list
        if (category != null) { // highlight active category when filtering
            request.setAttribute("selectedCategory", category); // which tab is active
        }
        if (keyword != null) { // repopulate search box
            request.setAttribute("searchKeyword", keyword); // echo search term
        }
        request.getRequestDispatcher(MENU_VIEW).forward(request, response); // render customer menu page
    }

    private static String nullToEmpty(String value) { // avoid null image strings in DB
        return value == null ? "" : value; // null becomes empty string
    }
}
