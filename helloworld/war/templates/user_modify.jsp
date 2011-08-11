<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link href="${settings.cssPath}/base.css" type="text/css" rel="stylesheet">
    <title>Welcome, Coffee-shop</title>
</head>
<body>
    <div id="logo">Coffee-Shop</div>
    <div id="menu">
    <a href="/">Home</a>
    <a href="/shop/new/">Create</a>
    </div>
    <div id="content">
    <c:choose>
        <c:when test="${user_info == null}">
            <a href="/user/login/">login</a><br \>
        </c:when>
        <c:otherwise>
            Hello, ${user_info.nickname} /
            <a href="${settings.logoutPath}">logout</a><br \>
        </c:otherwise>
    </c:choose>
    <br \>
	<form action="./" method="post" enctype="multipart/form-data">
	<table>
	<tr><th><label>Nickname:</label></th><td>${user.nickname}</td></tr>
	<tr><th><label>Email:</label></th><td>${user.email}</td></tr>
	<tr><th><label>OpenAPI Key:</label></th><td>${user.apikey}</td></tr>
	<tr><th><label for="id_realname">Real Name:</label></th><td><input id="id_realname" type="text" name="realname" size="20" value="${user.realname}" /></td></tr>
	<tr><th><label for="id_phone">Phone:</label></th><td><input id="id_phone" type="text" name="phone" size="20" value="${user.phone}"/></td></tr>
	<tr><th><label for="id_address">Address:</label></th><td><input id="id_address" type="text" name="address" size="40" value="${user.address}"/></td></tr>
	<tr><th><label for="id_photofile">Photo:</label></th><td><input type="file" name="photofile" id="id_photofile" /></td></tr>
	<c:choose>
        <c:when test="${user.photofile != null}">
		<tr><th></th><td><img src="/user/photo/"></td></tr>
		</c:when>
	</c:choose>
	</table>
	<input type="submit" name="submit" id="submit" value="Save" />
	<input type="button" name="go_home" id="go_home" value="Go home"
	onClick="parent.location='/'"/>
	</form>    
    </div>
</body>
</html>
