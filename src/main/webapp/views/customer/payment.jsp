<%--
  payment.jsp — Checkout payment screen. Shows order total, lets the customer pick a table
  (1–15) and payment method, then posts to PaymentServlet to place the dine-in order.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> <%-- UTF-8 page --%>
<%@ page import="com.restaurant.entity.Cart" %> <%-- Session cart for total --%>

<%
    // Get cart from session
    Cart cart = (Cart) session.getAttribute("cart"); // Cart built on menu/cart pages

    // If cart is empty, go back to menu
    if (cart == null || cart.getItems().isEmpty()) { // Nothing to pay for
        response.sendRedirect(request.getContextPath() + "/menu"); // Send user to menu
        return; // Stop rendering payment page
    }

    // Calculate total amount (no delivery fee for dine-in)
    double finalTotal = cart.getTotal(); // Sum of line items only
%>

<!DOCTYPE html> <!-- HTML5 -->
<html> <!-- Root -->
<head> <!-- Head -->
    <meta charset="UTF-8"> <!-- Encoding -->
    <title>Payment</title> <!-- Tab title -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/payment.css"> <!-- Payment styles -->
</head>
<body> <!-- Body -->
<div class="payment-container"> <!-- Centered payment card -->
    <h1>Payment Details</h1> <!-- Page heading -->

    <!-- Order Summary -->
    <div class="order-summary"> <!-- Bill recap -->
        <h3>Order Summary</h3> <!-- Section title -->
        <p>Subtotal: Rs. <%= finalTotal %></p> <!-- Same as total (no delivery) -->
        <p class="total">Total Amount: Rs. <%= finalTotal %></p> <!-- Amount to pay -->
    </div>

    <!-- Payment Form -->
    <form action="${pageContext.request.contextPath}/payment" method="post" id="paymentForm"> <!-- POST to PaymentServlet -->

        <!-- Table Number Selection - Buttons 1 to 15 -->
        <div class="form-group"> <!-- Table picker -->
            <label>Select Table Number:</label> <!-- Label for table buttons -->
            <div class="table-buttons"> <!-- Grid of table numbers -->
                <% for (int i = 1; i <= 15; i++) { %> <%-- Tables 1 through 15 --%>
                <button type="button" class="table-btn" data-table="<%= i %>" onclick="selectTable(<%= i %>, this)"> <!-- Pick table i -->
                    <%= i %> <!-- Button label -->
                </button>
                <% if (i % 5 == 0) { %> <%-- New row every 5 buttons --%>
                <br> <!-- Line break in button grid -->
                <% } %> <%-- end row break --%>
                <% } %> <%-- end table loop --%>
            </div>
            <!-- Hidden input to store selected table number -->
            <input type="hidden" name="tableNumber" id="selectedTable" required> <!-- Submitted with form -->
            <div id="selectedTableDisplay" class="selected-table"></div> <!-- Shows “Selected Table: N” -->
        </div>

        <!-- Payment Method Selection -->
        <div class="form-group"> <!-- How customer pays -->
            <label>Payment Method:</label> <!-- Label -->
            <select name="paymentMethod" required> <!-- Required dropdown -->
                <option value="">Select Payment Method</option> <!-- Placeholder -->
                <option value="Cash">Cash</option> <!-- Pay at table -->
                <option value="Card">Card</option> <!-- Card terminal -->
                <option value="Online">Online Payment</option> <!-- Digital wallet etc. -->
            </select>
        </div>

        <!-- Hidden field for total amount -->
        <input type="hidden" name="totalAmount" value="<%= finalTotal %>"> <!-- Server validates total -->

        <!-- Pay Button -->
        <button type="submit" class="pay-btn" id="payBtn" disabled>Pay Rs. <%= finalTotal %></button> <!-- Enabled after table pick -->
    </form>

    <!-- Back to Cart Link -->
    <a href="${pageContext.request.contextPath}/cart" class="back-link">← Back to Cart</a> <!-- Edit cart -->
</div>

<script>
    // Function to handle table selection
    function selectTable(tableNum, buttonElement) { // User clicked a table button
        // Set hidden input value
        document.getElementById('selectedTable').value = tableNum; // Form will submit this

        // Show selected table message
        document.getElementById('selectedTableDisplay').innerHTML = '✓ Selected Table: ' + tableNum; // Visual feedback

        // Remove active class from all buttons
        var btns = document.getElementsByClassName('table-btn'); // All table buttons
        for (var i = 0; i < btns.length; i++) { // Loop each button
            btns[i].classList.remove('active'); // Clear highlight
        }

        // Add active class to clicked button
        buttonElement.classList.add('active'); // Highlight chosen table

        // Enable pay button
        document.getElementById('payBtn').disabled = false; // Allow submit once table chosen
    }
</script>

</body>
</html>
