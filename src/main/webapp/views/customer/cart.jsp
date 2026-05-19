<%--
  cart.jsp — Shopping cart page. Reads the session Cart, lists line items with quantity
  controls, shows the dine-in total (no delivery fee), and links to checkout or back to menu.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> <%-- UTF-8 HTML page --%>
<%@ page import="com.restaurant.entity.Cart" %> <%-- Cart model in session --%>
<%@ page import="com.restaurant.entity.Cart.CartItem" %> <%-- Single line inside cart --%>
<%@ page import="java.util.List" %> <%-- Type for cart item list --%>

<%
    // Get cart from session
    Cart cart = (Cart) session.getAttribute("cart"); // May be null on first visit

    // If no cart exists, create a new one
    if (cart == null) { // Nothing in session yet
        cart = new Cart(); // Fresh empty cart
        session.setAttribute("cart", cart); // Store for this customer
    }

    // Get all items from cart
    List<CartItem> items = cart.getItems(); // Lines to render below
%>

<!DOCTYPE html> <!-- HTML5 -->
<html> <!-- Document root -->
<head> <!-- Head section -->
    <meta charset="UTF-8"> <!-- Encoding -->
    <title>Your Shopping Cart</title> <!-- Tab title -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart.css"> <!-- Cart styles -->
</head>
<body> <!-- Page body -->
<div class="cart-page"> <!-- Main cart layout -->
    <h1>Shopping Cart</h1> <!-- Page title -->

    <% if (items.isEmpty()) { %> <%-- No lines in cart --%>
    <!-- Show this when cart is empty -->
    <div class="empty-cart"> <!-- Empty state -->
        <p>Your cart is empty</p> <!-- Message -->
        <a href="${pageContext.request.contextPath}/menu" class="shop-btn">Continue Shopping</a> <!-- Back to menu -->
    </div>
    <% } else { %> <%-- At least one item --%>
    <!-- Show cart items -->
    <div class="cart-items"> <!-- Scrollable list of lines -->
        <%
            double grandTotal = 0; // Sum of all line totals
            // Loop through each item in cart
            for (CartItem item : items) { // One row per cart line
                // Calculate total for one item (price x quantity)
                double itemTotal = item.getPrice() * item.getQuantity(); // Line subtotal
                // Add to grand total
                grandTotal = grandTotal + itemTotal; // Running bill
        %>
        <div class="cart-item"> <!-- Single cart row -->
            <!-- Item name and price -->
            <div class="item-info"> <!-- Left column -->
                <h3><%= item.getName() %></h3> <!-- Dish name -->
                <p class="item-price">Rs. <%= item.getPrice() %></p> <!-- Unit price -->
            </div>

            <!-- Quantity controls with - and + buttons -->
            <div class="item-quantity"> <!-- Middle column -->
                <!-- Minus button -->
                <form action="${pageContext.request.contextPath}/cart" method="post" style="display: inline;"> <!-- POST decrement -->
                    <input type="hidden" name="action" value="update"> <!-- CartServlet update -->
                    <input type="hidden" name="menuId" value="<%= item.getMenuId() %>"> <!-- Which line -->
                    <input type="hidden" name="quantity" value="<%= item.getQuantity() - 1 %>"> <!-- One less -->
                    <button type="submit" class="qty-btn">-</button> <!-- Submit decrease -->
                </form>

                <!-- Quantity number -->
                <span class="qty-num"><%= item.getQuantity() %></span> <!-- Current qty -->

                <!-- Plus button -->
                <form action="${pageContext.request.contextPath}/cart" method="post" style="display: inline;"> <!-- POST increment -->
                    <input type="hidden" name="action" value="update"> <!-- CartServlet update -->
                    <input type="hidden" name="menuId" value="<%= item.getMenuId() %>"> <!-- Which line -->
                    <input type="hidden" name="quantity" value="<%= item.getQuantity() + 1 %>"> <!-- One more -->
                    <button type="submit" class="qty-btn">+</button> <!-- Submit increase -->
                </form>
            </div>

            <!-- Item total price (price x quantity) -->
            <div class="item-total"> <!-- Line total column -->
                <p>Rs. <%= itemTotal %></p> <!-- price × quantity -->
            </div>

            <!-- Remove button -->
            <div class="item-remove"> <!-- Remove column -->
                <form action="${pageContext.request.contextPath}/cart" method="post"> <!-- POST remove line -->
                    <input type="hidden" name="action" value="remove"> <!-- CartServlet remove -->
                    <input type="hidden" name="menuId" value="<%= item.getMenuId() %>"> <!-- Which line -->
                    <button type="submit" class="remove-btn">✕</button> <!-- Delete this item -->
                </form>
            </div>
        </div>
        <% } %> <%-- end for each cart item --%>
    </div>

    <!-- Cart summary - NO delivery fee (dine-in only) -->
    <div class="cart-summary"> <!-- Totals block -->
        <div class="summary-row total"> <!-- Grand total row -->
            <span>Total Amount:</span> <!-- Label -->
            <span>Rs. <%= grandTotal %></span> <!-- Dine-in total -->
        </div>
    </div>

    <!-- Action buttons -->
    <div class="cart-buttons"> <!-- Footer actions -->
        <a href="${pageContext.request.contextPath}/menu" class="continue-btn">← Continue Shopping</a> <!-- More items -->
        <form action="${pageContext.request.contextPath}/cart" method="post" style="display: inline;"> <!-- Clear entire cart -->
            <input type="hidden" name="action" value="clear"> <!-- CartServlet clear -->
            <button type="submit" class="clear-btn">Clear Cart</button> <!-- Empty cart -->
        </form>
        <a href="${pageContext.request.contextPath}/payment" class="checkout-btn">Proceed to Checkout →</a> <!-- Go to payment -->
    </div>
    <% } %> <%-- end empty vs non-empty --%>
</div>
</body>
</html>
