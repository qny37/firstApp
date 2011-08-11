<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<jsp:useBean id="settings" class="javashop.SettingsBean"/>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link href="${settings.cssPath}/base.css" type="text/css" rel="stylesheet">
    <title>Welcome, Coffee-shop</title>
</head>
<body>
    <div id="logo">Coffee-Shop</div>
    <div id="menu">
    Home    
    <a href="/shop/new/">Create</a>
    </div>
    <div id="content">
    Hello,
    <c:choose>
        <c:when test="${user_info == null}">
            <a href="/user/login/">Login</a><br \>    
        </c:when>
        <c:otherwise>
            Hello, <a href="/user/">${user_info.nickname}</a> /
            <a href="${settings.logoutPath}">Logout</a><br \>
        </c:otherwise>
    </c:choose>
    <br \>
    Search a nice coffee-shop, Post your favorite coffee-shop.<br \>
    <br \>
    <b>[Coffee Shops]</b><br \>
    <ul>
    	<c:forEach items="${shops}" var="s">
        <li><a href="/shop/<c:out value="${s.key.id}"/>/read/"><c:out value="${s.title}"/></a> - <c:out value="${s.posttime}"/></li>
        </c:forEach>
    </ul>
    <br\>
    </div>
</body>
</html>
