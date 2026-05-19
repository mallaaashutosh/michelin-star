<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<% request.setAttribute("currentPage", "users"); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Users - Michelin Star Admin</title>
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
            <h2>User Management</h2>
            <p>Review accounts and approve pending customer registrations.</p>
        </header>

        <% if (request.getAttribute("error") != null) { %>
        <div class="alert-error"><%= request.getAttribute("error") %></div>
        <% } %>

        <div class="admin-panel">
            <h3>All Users</h3>
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Role</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="u" items="${users}">
                        <tr>
                            <td>${u.id}</td>
                            <td>${u.name}</td>
                            <td>${u.email}</td>
                            <td>${u.phoneNumber}</td>
                            <td><span class="badge badge-${u.role}">${u.role}</span></td>
                            <td><span class="badge badge-${u.status}">${u.status}</span></td>
                            <td class="table-actions">
                                <c:if test="${u.role == 'customer' && u.status == 'pending'}">
                                    <form action="${pageContext.request.contextPath}/admin/users" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="updateStatus">
                                        <input type="hidden" name="userId" value="${u.id}">
                                        <input type="hidden" name="status" value="active">
                                        <button type="submit" class="btn-admin btn-admin-sm">Approve</button>
                                    </form>
                                </c:if>
                                <c:if test="${u.role == 'customer' && u.status == 'active'}">
                                    <form action="${pageContext.request.contextPath}/admin/users" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="updateStatus">
                                        <input type="hidden" name="userId" value="${u.id}">
                                        <input type="hidden" name="status" value="pending">
                                        <button type="submit" class="btn-admin btn-admin-sm btn-admin-outline">Suspend</button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty users}">
                        <tr><td colspan="7" style="color: var(--admin-muted);">No users found.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </main>
</div>
</body>
</html>
