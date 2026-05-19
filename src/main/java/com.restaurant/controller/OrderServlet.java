/*
 * OrderServlet.java
 * Runs after checkout: pulls payment details from the session, saves the order to the database,
 * clears the cart on success, and forwards to the order-success page (or back to the cart if save fails).
 */
package com.restaurant.controller; // servlet lives in the controller package

import com.restaurant.dao.OrderDAO; // contract for saving orders
import com.restaurant.dao.OrderDaoImpl; // JDBC implementation we wire up at startup
import com.restaurant.entity.Cart; // in-memory cart held in the HTTP session
import com.restaurant.entity.User; // logged-in customer from the session
import jakarta.servlet.ServletException; // thrown when forwarding or dispatch fails
import jakarta.servlet.annotation.WebServlet; // maps this class to a URL without web.xml
import jakarta.servlet.http.HttpServlet; // base class for HTTP endpoints
import jakarta.servlet.http.HttpServletRequest; // incoming browser request
import jakarta.servlet.http.HttpServletResponse; // outgoing response we send back
import jakarta.servlet.http.HttpSession; // per-user session storage for cart and payment
import java.io.IOException; // I/O errors during redirect or forward

@WebServlet("/order") // reachable at /order after payment
public class OrderServlet extends HttpServlet { // handles order finalization

    private OrderDAO orderDAO; // data access for persisting orders

    @Override
    public void init() { // called once when the servlet is loaded
        orderDAO = new OrderDaoImpl(); // create the DAO instance
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) // GET triggers checkout completion
            throws ServletException, IOException { // servlet container may throw these

        HttpSession session = request.getSession(); // reuse or create the user's session

        // Get logged in user
        User user = (User) session.getAttribute("user"); // who is placing the order
        int customerId = (user != null) ? user.getId() : 1; // fallback id when no user (guest-style default)

        // Get payment details from session
        String totalAmount = (String) session.getAttribute("paymentAmount"); // amount shown on success page
        String paymentMethod = (String) session.getAttribute("paymentMethod"); // cash, card, etc.
        String tableNumberStr = (String) session.getAttribute("tableNumber"); // dine-in table as text

        int tableNumber = 0; // numeric table, 0 if not set
        if (tableNumberStr != null) { // only parse when we stored a value
            tableNumber = Integer.parseInt(tableNumberStr); // convert table string to int
        }

        // Get cart
        Cart cart = (Cart) session.getAttribute("cart"); // line items to persist

        // Save order
        boolean saved = orderDAO.saveOrder(cart, customerId, paymentMethod, tableNumber); // write order to DB

        if (saved) { // happy path
            // Clear cart
            if (cart != null) { // avoid NPE if session had no cart
                cart.clear(); // remove all line items
                session.setAttribute("cart", cart); // put empty cart back in session
            }

            // Clean session
            session.removeAttribute("paymentAmount"); // payment flow is done
            session.removeAttribute("paymentMethod"); // no longer needed in session
            session.removeAttribute("tableNumber"); // table choice consumed

            // Show success page
            request.setAttribute("totalAmount", totalAmount); // JSP can display total
            request.setAttribute("paymentMethod", paymentMethod); // JSP can show how they paid
            request.setAttribute("tableNumber", tableNumber); // JSP can show table
            request.getRequestDispatcher("/views/customer/orderSuccess.jsp").forward(request, response); // render confirmation
        } else { // save failed — send them back to fix or retry
            response.sendRedirect(request.getContextPath() + "/cart"); // back to cart page
        }
    }
}
