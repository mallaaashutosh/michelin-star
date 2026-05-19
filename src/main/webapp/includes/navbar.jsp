<!-- ================= NAVBAR ================= -->
<%
    User navUser = (User) session.getAttribute("user");
    String navCtx = request.getContextPath();
%>
<nav class="navbar">

    <div class="logo">
        Michelin-Star
    </div>

    <div class="nav-links">
        <a href="<%= navCtx %>/">Home</a>
        <a href="<%= navCtx %>/menu">Menu</a>
        <a href="<%= navCtx %>/">About</a>
        <a href="<%= navCtx %>/">Gallery</a>
        <a href="<%= navCtx %>/">Contact</a>
    </div>

    <% if (navUser != null) { %>
    <div class="nav-user" style="display:flex;align-items:center;gap:16px;">
        <span style="font-size:14px;color:#555;">Hi, <%= navUser.getName() != null ? navUser.getName().split(" ")[0] : "guest" %></span>
        <% if (navUser.isAdmin()) { %>
        <a href="<%= navCtx %>/admin/dashboard" class="btn" style="padding:10px 22px;font-size:14px;">Admin</a>
        <% } %>
        <a href="<%= navCtx %>/menu" class="btn">Browse menu</a>
        <a href="<%= navCtx %>/logout" style="font-size:14px;color:#8c6c4d;font-weight:600;">Logout</a>
    </div>
    <% } else { %>
    <a href="<%= navCtx %>/login" class="btn">
        Sign In
    </a>
    <% } %>

</nav>
