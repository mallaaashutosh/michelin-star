<%--
  Top navigation bar for public pages — logo, links, and sign-in or user menu.
  Parent JSP must import com.restaurant.entity.User; reads the logged-in user from session.
--%>
<%
    User navUser = (User) session.getAttribute("user"); // Logged-in user, null if guest
    String navCtx = request.getContextPath();           // Base path for links, e.g. /michelin-star
%>

<nav class="navbar">

    <div class="logo">Michelin-Star</div> <!-- Restaurant logo / home brand -->

    <div class="nav-links"> <!-- Primary page links -->
        <a href="<%= navCtx %>/">Home</a>
        <a href="<%= navCtx %>/menu">Menu</a>
        <a href="<%= navCtx %>/#contact">Contact</a>
    </div>

    <% if (navUser != null) { %> <!-- Logged-in: greeting, optional admin link, browse, logout -->
    <div class="nav-user" style="display:flex;align-items:center;gap:16px;">
        <span style="font-size:14px;color:#555;">Hi, <%= navUser.getName() != null ? navUser.getName().split(" ")[0] : "guest" %></span>
        <% if (navUser.isAdmin()) { %>
        <a href="<%= navCtx %>/admin/dashboard" class="btn" style="padding:10px 22px;font-size:14px;">Admin</a>
        <% } %>
        <a href="<%= navCtx %>/menu" class="btn">Browse menu</a>
        <a href="<%= navCtx %>/logout" style="font-size:14px;color:#8c6c4d;font-weight:600;">Logout</a>
    </div>
    <% } else { %> <!-- Guest: show sign-in button -->
    <a href="<%= navCtx %>/login" class="btn">Sign In</a>
    <% } %>

</nav>
