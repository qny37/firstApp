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
    <a href="/search/">Search</a>
    </div>
    <div id="content">
    <c:choose>
        <c:when test="${user_info == null}">
            <a href="/user/login/">login</a><br \>
        </c:when>
        <c:otherwise>
            Hello, ${user_info.nickname} /
            <a href="${user_info.logoutPath}">logout</a><br \>
        </c:otherwise>
    </c:choose>
    <br \>
	Regist your favorite coffee-shop.<br \>
	<br \>
	<div id="map" style="width: 450px; height: 250px"></div>
	<form action="./" method="post">
	<table>
	<tr><th><label>Nickname:</label></th><td>${user_info.nickname}</td></tr>
	<tr><th><label>Email:</label></th><td>${user_info.email}</td></tr>
	<tr><th><label for="title">Title:</label></th><td><input id="title" size="54" type="text" name="title" maxlength="100" value="${shop.title}" /></td></tr>
	<tr><th><label for="content">Content:</label></th><td><textarea rows="10" limit="2048" name="content" wrap="hard" id="content" size="2048" cols="52" class="required">${shop.content.value}</textarea><input type="hidden" name="geo_point" id="geo_point" /></td></tr>
	</table>
	<input type="submit" name="submit" id="submit" value="Save" />
	<input type="button" name="go_home" id="go_home" value="Cancel"
	onClick="history.go(-1)"/>
	</form>    
    </div>
</body>
<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAA8a0p5W3dAK7S-KRHJDDK3hTC9FakVEvh28vN8n0Q8Jnk0P11gRT
yCkcP_HKaOXQoxUd-rNXmQDTFeA" type="text/javascript"></script>
<script src="http://www.google.com/jsapi"></script>
<script type="text/javascript">google.load("jquery", "1");</script>
<script type="text/javascript">
$(document).ready(function(){
    if (GBrowserIsCompatible()) {
        latlng = new GLatLng(${shop.geopoint}); 
        var marker = new GMarker(latlng, {draggable:true});
        var mapOptions = {googleBarOptions : {style : "new"}}
        var map = new GMap2(document.getElementById("map"), mapOptions);
        map.setCenter(latlng, 15);
        map.setUIToDefault();
        map.enableGoogleBar();
        map.addOverlay(marker);
        map.openInfoWindow(map.getCenter(), document.createTextNode(unescape("${shop.title}")));
        GEvent.addListener(marker, "mouseup", function() {
            var latlng = marker.getLatLng();
            $('#geo_point').attr('value', latlng);
        });
        GEvent.addListener(map, "click", function(overlay, latlng) {
            if (latlng) {
                map.clearOverlays();
                marker.setLatLng(latlng);
                $('#geo_point').attr('value', latlng);
                map.setCenter(latlng, map.getZoom());
                map.addOverlay(marker);
            }
        });
    }
});

$(document).unload(function(){
    GUnload();
});
</script>
</html>