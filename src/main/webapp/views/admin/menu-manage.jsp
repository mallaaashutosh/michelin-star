<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%
    request.setAttribute("currentPage", "menu");
    com.restaurant.entity.MenuItem editItem = (com.restaurant.entity.MenuItem) request.getAttribute("editItem");
    boolean editing = editItem != null;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Menu - Michelin Star Admin</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:wght@600;700&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body class="admin-body">
<div class="admin-layout">
    <jsp:include page="/includes/admin-sidebar.jsp" />

    <main class="admin-main">
        <header class="admin-header">
            <h2>Menu Management</h2>
            <p>Add, edit, or remove items from the restaurant menu.</p>
        </header>

        <div class="admin-panel">
            <h3><%= editing ? "Edit Item" : "Add New Item" %></h3>
            <form class="admin-form" action="${pageContext.request.contextPath}/admin/menu" method="post">
                <input type="hidden" name="action" value="<%= editing ? "edit" : "add" %>">
                <% if (editing) { %>
                <input type="hidden" name="menuId" value="<%= editItem.getMenuId() %>">
                <% } %>
                <div class="form-group">
                    <label for="name">Name</label>
                    <input type="text" id="name" name="name" required
                           value="<%= editing ? editItem.getName() : "" %>">
                </div>
                <div class="form-group">
                    <label for="category">Category</label>
                    <select id="category" name="category" required>
                        <%
                            String[] categories = {"Nepali", "Chinese", "Indian", "Italian", "FastFood", "Beverages", "Japanese", "Turkish"};
                            String selectedCat = editing ? editItem.getCategory() : "";
                            for (String cat : categories) {
                        %>
                        <option value="<%= cat %>" <%= cat.equals(selectedCat) ? "selected" : "" %>><%= cat %></option>
                        <% } %>
                    </select>
                </div>
                <div class="form-group">
                    <label for="price">Price (Rs.)</label>
                    <input type="number" id="price" name="price" step="0.01" min="0" required
                           value="<%= editing ? editItem.getPrice() : "" %>">
                </div>
                <div class="form-group">
                    <label for="availability">Availability</label>
                    <select id="availability" name="availability" required>
                        <option value="available" <%= editing && "available".equals(editItem.getAvailability()) ? "selected" : (!editing ? "selected" : "") %>>Available</option>
                        <option value="unavailable" <%= editing && "unavailable".equals(editItem.getAvailability()) ? "selected" : "" %>>Unavailable</option>
                    </select>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn-admin"><%= editing ? "Update" : "Add Item" %></button>
                    <% if (editing) { %>
                    <a href="${pageContext.request.contextPath}/admin/menu" class="btn-admin btn-admin-outline" style="margin-left:0.5rem;">Cancel</a>
                    <% } %>
                </div>
            </form>
        </div>

        <div class="admin-panel">
            <h3>All Menu Items</h3>
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${menuItems}">
                        <tr>
                            <td>${item.menuId}</td>
                            <td>${item.name}</td>
                            <td>${item.category}</td>
                            <td>Rs. ${item.price}</td>
                            <td>
                                <span class="badge badge-${item.availability}">${item.availability}</span>
                            </td>
                            <td class="table-actions">
                                <a href="${pageContext.request.contextPath}/admin/menu?action=edit&id=${item.menuId}"
                                   class="btn-admin btn-admin-sm btn-admin-outline">Edit</a>
                                <a href="${pageContext.request.contextPath}/admin/menu?action=delete&id=${item.menuId}"
                                   class="btn-admin btn-admin-sm btn-admin-danger"
                                   onclick="return confirm('Delete this menu item?');">Delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty menuItems}">
                        <tr><td colspan="6" style="color: var(--admin-muted);">No menu items yet.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </main>
</div>
</body>
</html>
