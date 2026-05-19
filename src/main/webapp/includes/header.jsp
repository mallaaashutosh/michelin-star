<%--
  Shared HTML head and opening body — included by public-facing pages.
  Parent JSP must declare page directives and imports; this fragment only supplies layout chrome.
  Note: footer.jsp closes </body> and </html>.
--%>

<html>
<head>
    <title>Michelin-Star Restaurant</title> <!-- Browser tab title -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"> <!-- Site-wide stylesheet -->
</head>
<body> <!-- footer.jsp closes body and html -->
