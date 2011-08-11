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
    <c:choose>
    	<c:when test="${user_info.email == shop.email}">
		<input type="button" value="Modify" id="modify" name="modify"
        onClick="window.location='/shop/${shop.key.id}/modify/'">
    	<input type="button" value="Delete" id="delete" name="delete"
        onClick="window.location='/shop/${shop.key.id}/delete/'">
        </c:when>
    </c:choose>
    <br \>
	<br \>
	<div id="map" style="width: 450px; height: 250px"></div>
	<table>
	<tr><th><label>Nickname:</label></th><td>${shop.nickname}</td></tr>
	<tr><th><label>Email:</label></th><td>${shop.email}</td></tr>
	<tr><th><label for="title">Title:</label></th><td>${shop.title}</td></tr>
	<tr><th><label for="content">Content:</label></th><td>${shop.content.value}</td></tr>
	</table>
    </div>
</body>
<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAA8a0p5W3dAK7S-KRHJDDK3hTC9FakVEvh28vN8n0Q8Jnk0P11gRTyCkcP_HKaOXQoxUd-rNXmQDTFeA" type="text/javascript"></script>
<script src="http://www.google.com/jsapi"></script>
<script type="text/javascript">google.load("jquery", "1");</script>
<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAA8a0p5W3dAK7S-KRHJDDK3hTC9FakVEvh28vN8n0Q8Jnk0P11gRT
yCkcP_HKaOXQoxUd-rNXmQDTFeA" type="text/javascript"></script>
<script src="http://www.google.com/jsapi"></script>
<script type="text/javascript">google.load("jquery", "1");</script>
<script type="text/javascript">
function initialize() {
    if (GBrowserIsCompatible()) {
        latlng = new GLatLng(${shop.geopoint}); 
        marker = new GMarker(latlng);
        var mapOptions = {googleBarOptions : {style : "new"}}
        var map = new GMap2(document.getElementById("map"), mapOptions);
        map.setCenter(latlng, 15);
        map.setUIToDefault();
        map.addOverlay(marker);
        map.openInfoWindow(map.getCenter(), document.createTextNode(unescape("${shop.title}")));
    }
}

$(document).ready(function(){
    initialize();
});

$(document).unload(function(){
    GUnload();
});
</script>
</html>