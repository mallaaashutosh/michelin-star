<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.restaurant.entity.Cart" %>

<%
    // Get cart from session
    Cart cart = (Cart) session.getAttribute("cart");

    // If cart is empty, go back to menu
    if (cart == null || cart.getItems().isEmpty()) {
        response.sendRedirect(request.getContextPath() + "/menu");
        return;
    }

    // Calculate total amount (no delivery fee for dine-in)
    double finalTotal = cart.getTotal();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Payment</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/payment.css">
</head>
<body>
<div class="payment-container">
    <h1>Payment Details</h1>

    <!-- Order Summary -->
    <div class="order-summary">
        <h3>Order Summary</h3>
        <p>Subtotal: Rs. <%= finalTotal %></p>
        <p class="total">Total Amount: Rs. <%= finalTotal %></p>
    </div>

    <!-- Payment Form -->
    <form action="${pageContext.request.contextPath}/payment" method="post" id="paymentForm">

        <!-- Table Number Selection - Buttons 1 to 15 -->
        <div class="form-group">
            <label>Select Table Number:</label>
            <div class="table-buttons">
                <% for (int i = 1; i <= 15; i++) { %>
                <button type="button" class="table-btn" data-table="<%= i %>" onclick="selectTable(<%= i %>, this)">
                    <%= i %>
                </button>
                <% if (i % 5 == 0) { %>
                <br>
                <% } %>
                <% } %>
            </div>
            <!-- Hidden input to store selected table number -->
            <input type="hidden" name="tableNumber" id="selectedTable" required>
            <div id="selectedTableDisplay" class="selected-table"></div>
        </div>

        <!-- Payment Method Selection -->
        <div class="form-group">
            <label>Payment Method:</label>
            <select name="paymentMethod" required>
                <option value="">Select Payment Method</option>
                <option value="Cash">Cash</option>
                <option value="Card">Card</option>
                <option value="Online">Online Payment</option>
            </select>
        </div>

        <!-- Hidden field for total amount -->
        <input type="hidden" name="totalAmount" value="<%= finalTotal %>">

        <!-- Pay Button -->
        <button type="submit" class="pay-btn" id="payBtn" disabled>Pay Rs. <%= finalTotal %></button>
    </form>

    <!-- Back to Cart Link -->
    <a href="${pageContext.request.contextPath}/cart" class="back-link">← Back to Cart</a>
</div>

<script>
    // Function to handle table selection
    function selectTable(tableNum, buttonElement) {
        // Set hidden input value
        document.getElementById('selectedTable').value = tableNum;

        // Show selected table message
        document.getElementById('selectedTableDisplay').innerHTML = '✓ Selected Table: ' + tableNum;

        // Remove active class from all buttons
        var btns = document.getElementsByClassName('table-btn');
        for (var i = 0; i < btns.length; i++) {
            btns[i].classList.remove('active');
        }

        // Add active class to clicked button
        buttonElement.classList.add('active');

        // Enable pay button
        document.getElementById('payBtn').disabled = false;
    }
</script>

</body>
</html>