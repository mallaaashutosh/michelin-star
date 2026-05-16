<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.restaurant.entity.Cart" %>
<%@ page import="com.restaurant.entity.Cart.CartItem" %>
<%@ page import="java.util.List" %>

<%
    // Get cart from session
    Cart cart = (Cart) session.getAttribute("cart");

    // If no cart exists, create a new one
    if (cart == null) {
        cart = new Cart();
        session.setAttribute("cart", cart);
    }

    // Get all items from cart
    List<CartItem> items = cart.getItems();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Your Shopping Cart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart.css">
</head>
<body>
<div class="cart-page">
    <h1>Shopping Cart</h1>

    <% if (items.isEmpty()) { %>
    <!-- Show this when cart is empty -->
    <div class="empty-cart">
        <p>Your cart is empty</p>
        <a href="${pageContext.request.contextPath}/menu" class="shop-btn">Continue Shopping</a>
    </div>
    <% } else { %>
    <!-- Show cart items -->
    <div class="cart-items">
        <%
            double grandTotal = 0;
            // Loop through each item in cart
            for (CartItem item : items) {
                // Calculate total for one item (price x quantity)
                double itemTotal = item.getPrice() * item.getQuantity();
                // Add to grand total
                grandTotal = grandTotal + itemTotal;
        %>
        <div class="cart-item">
            <!-- Item name and price -->
            <div class="item-info">
                <h3><%= item.getName() %></h3>
                <p class="item-price">Rs. <%= item.getPrice() %></p>
            </div>

            <!-- Quantity controls with - and + buttons -->
            <div class="item-quantity">
                <!-- Minus button -->
                <form action="${pageContext.request.contextPath}/cart" method="post" style="display: inline;">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="menuId" value="<%= item.getMenuId() %>">
                    <input type="hidden" name="quantity" value="<%= item.getQuantity() - 1 %>">
                    <button type="submit" class="qty-btn">-</button>
                </form>

                <!-- Quantity number -->
                <span class="qty-num"><%= item.getQuantity() %></span>

                <!-- Plus button -->
                <form action="${pageContext.request.contextPath}/cart" method="post" style="display: inline;">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="menuId" value="<%= item.getMenuId() %>">
                    <input type="hidden" name="quantity" value="<%= item.getQuantity() + 1 %>">
                    <button type="submit" class="qty-btn">+</button>
                </form>
            </div>

            <!-- Item total price (price x quantity) -->
            <div class="item-total">
                <p>Rs. <%= itemTotal %></p>
            </div>

            <!-- Remove button -->
            <div class="item-remove">
                <form action="${pageContext.request.contextPath}/cart" method="post">
                    <input type="hidden" name="action" value="remove">
                    <input type="hidden" name="menuId" value="<%= item.getMenuId() %>">
                    <button type="submit" class="remove-btn">✕</button>
                </form>
            </div>
        </div>
        <% } %>
    </div>

    <!-- Cart summary with totals -->
    <div class="cart-summary">
        <div class="summary-row">
            <span>Subtotal:</span>
            <span>Rs. <%= grandTotal %></span>
        </div>
        <div class="summary-row delivery">
            <span>Delivery Fee:</span>
            <span>Rs. 50</span>
        </div>
        <div class="summary-row total">
            <span>Total:</span>
            <span>Rs. <%= grandTotal + 50 %></span>
        </div>
    </div>

    <!-- Action buttons -->
    <div class="cart-buttons">
        <a href="${pageContext.request.contextPath}/menu" class="continue-btn">← Continue Shopping</a>
        <form action="${pageContext.request.contextPath}/cart" method="post" style="display: inline;">
            <input type="hidden" name="action" value="clear">
            <button type="submit" class="clear-btn">Clear Cart</button>
        </form>
        <a href="${pageContext.request.contextPath}/views/customer/checkout.jsp" class="checkout-btn">Proceed to Checkout →</a>
    </div>
    <% } %>
</div>
</body>
</html>