<%--
  orderlist.jsp — Customer order history. Lists past order lines (one row per item) loaded
  by OrderServlet from the database; shows empty state when the customer has no orders yet.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> <%-- UTF-8 page --%>
<%@ page import="java.util.List" %> <%-- Order list type --%>
<%@ page import="java.util.Map" %> <%-- Each order row as key/value map --%>

<%
    List<Map<String, Object>> orders = (List<Map<String, Object>>) request.getAttribute("orders"); // Set by OrderServlet
%>

<!DOCTYPE html> <!-- HTML5 -->
<html> <!-- Root -->
<head> <!-- Head -->
    <meta charset="UTF-8"> <!-- Encoding -->
    <title>My Orders</title> <!-- Tab title -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/orderlist.css"> <!-- Order list styles -->
</head>
<body> <!-- Body -->
<div class="container"> <!-- Page wrapper -->
    <a href="${pageContext.request.contextPath}/menu" class="back-btn">← Back to Menu</a> <!-- Return to browsing -->
    <h1>My Orders</h1> <!-- Page title -->

    <% if (orders == null || orders.isEmpty()) { %> <%-- No orders for this user --%>
    <div class="empty-message"> <!-- Empty state -->
        <p>You haven't placed any orders yet.</p> <!-- Friendly message -->
        <a href="${pageContext.request.contextPath}/menu" class="btn">Start Shopping</a> <!-- Go order food -->
    </div>
    <% } else { %> <%-- Has at least one order line --%>
    <div class="table-responsive"> <!-- Horizontal scroll on small screens -->
        <table> <!-- Order history table -->
            <thead> <!-- Column headers -->
            <tr> <!-- Header row -->
                <th>Order ID</th> <!-- Order id column -->
                <th>Item Name</th> <!-- Dish name -->
                <th>Quantity</th> <!-- How many -->
                <th>Price</th> <!-- Unit price -->
                <th>Total</th> <!-- Line total -->
                <th>Table No</th> <!-- Dine-in table -->
                <th>Payment</th> <!-- Cash, Card, etc. -->
                <th>Status</th> <!-- pending, completed, etc. -->
                <th>Date</th> <!-- When ordered -->
            </tr>
            </thead>
            <tbody> <!-- Data rows -->
            <% for (Map<String, Object> order : orders) { %> <%-- One row per order line --%>
            <tr> <!-- Single order line -->
                <td><%= order.get("order_id") %></td> <!-- Order id -->
                <td><%= order.get("menu_name") %></td> <!-- Menu item name -->
                <td><%= order.get("quantity") %></td> <!-- Qty ordered -->
                <td>Rs. <%= order.get("price") %></td> <!-- Unit price -->
                <td>Rs. <%= order.get("total_amount") %></td> <!-- Line total -->
                <td><%= order.get("table_number") %></td> <!-- Table number -->
                <td><%= order.get("payment_method") %></td> <!-- Payment type -->
                <td><%= order.get("status") %></td> <!-- Order status -->
                <td><%= order.get("created_at") %></td> <!-- Timestamp -->
            </tr>
            <% } %> <%-- end order loop --%>
            </tbody>
        </table>
    </div>
    <% } %> <%-- end empty vs has orders --%>
</div>
</body>
</html>
